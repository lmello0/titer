CREATE SCHEMA IF NOT EXISTS plays;

-- pg_trgm powers the fuzzy search on submit
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- btree_gist lets a GiST index mix scalar columns with a range column,
-- which the stagings duplicate-guard exclusion constraint needs
CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE TYPE plays.submission_status AS ENUM (
    'pending',
    'approved',
    'rejected'
    );

CREATE TYPE plays.record_badge AS ENUM (
    'none',
    'verified', -- approved by trusty
    'official' -- submitted by a verified producer
    );

CREATE TYPE plays.work_credit_role AS ENUM (
    'author',
    'translator',
    'adapter',
    'composer',
    'lyricist',
    'librettist',
    'other'
    );

CREATE TYPE plays.production_credit_role AS ENUM (
    'director',
    'actor',
    'understudy',
    'set_designer',
    'costume_designer',
    'lighting_designer',
    'sound_designer',
    'choreographer',
    'music_director',
    'other'
    );

CREATE TABLE plays.submissions
(
    id           uuid primary key                 default uuidv7(),
    status       plays.submission_status not null default 'pending',
    badge        plays.record_badge      not null default 'none',

    submitted_by uuid                    not null,
    reviewed_by  uuid,
    reviewed_at  timestamptz,
    note         text,

    created_at   timestamptz             not null default now(),
    modified_at  timestamptz             not null default now(),

    constraint chk_submissions_review_consistent check (
        (status = 'pending' and reviewed_by is null and reviewed_at is null)
            or
        (status != 'pending' and reviewed_by is not null and reviewed_at is not null)
        ),

    constraint fk_submissions_submitted_by_on_users
        foreign key (submitted_by)
            references users.users (id),

    constraint fk_submissions_reviewed_by_on_users
        foreign key (reviewed_by)
            references users.users (id)
);

create index idx_submissions_status on plays.submissions (status);
create index idx_submissions_submitted_by on plays.submissions (submitted_by);

create table plays.works
(
    id                uuid primary key      default uuidv7(),
    title             varchar(255) not null,
    original_title    varchar(255), -- for translated works
    slug              varchar(255) not null unique,
    synopsis          text,
    year_written      integer,
    year_premiered    integer,
    original_language varchar(50),  -- ISO 639 code
    duration_minutes  integer,
    poster_url        varchar(2048),

    submission_id     uuid         not null,

    created_at        timestamptz  not null default now(),
    modified_at       timestamptz  not null default now(),

    constraint fk_works_on_submissions
        foreign key (submission_id)
            references plays.submissions (id)
);

create index idx_works_slug on plays.works (slug);
create index idx_works_submission_id on plays.works (submission_id);
create index idx_works_title_trgm on plays.works using gin (title gin_trgm_ops);

create table plays.productions
(
    id            uuid primary key      default uuidv7(),
    title         varchar(255),
    slug          varchar(255) not null unique,
    details       text,
    company_id    uuid, -- companies module (not created yet)
    poster_url    varchar(2048),

    work_id       uuid         not null,
    submission_id uuid         not null,

    created_at    timestamptz  not null default now(),
    modified_at   timestamptz  not null default now(),

    constraint fk_productions_on_work
        foreign key (work_id)
            references plays.works (id),

    constraint fk_productions_on_submission
        foreign key (submission_id)
            references plays.submissions (id)
);

create index idx_productions_work_id on plays.productions (work_id);
create index idx_productions_slug on plays.productions (slug);
create index idx_productions_company_id on plays.productions (company_id);
create index idx_productions_submission_id on plays.productions (submission_id);

create table plays.stagings
(
    id            uuid primary key     default uuidv7(),
    title         varchar(255),

    venue_id      uuid,   -- fk -> venues module (not created yet)
    city_id       bigint, -- fk -> address module (not created yet)
    country_id    bigint, -- fk -> address module (not created yet)

    run           daterange,
    opening_date  date generated always as ( lower(run) ) stored,
    closing_date  date generated always as ( upper(run) ) stored,

    production_id uuid        not null,
    submission_id uuid        not null,

    created_at    timestamptz not null default now(),
    modified_at   timestamptz not null default now(),

    constraint fk_stagings_on_production
        foreign key (production_id)
            references plays.productions (id),

    constraint fk_stagings_on_submission
        foreign key (submission_id)
            references plays.submissions (id)
);

create index idx_stagings_production_id on plays.stagings (production_id);
create index idx_stagings_venue_id on plays.stagings (venue_id);
create index idx_stagings_submission_id on plays.stagings (submission_id);
create index idx_stagings_run_gist on plays.stagings using gist (run);

alter table plays.stagings
    add constraint stagings_no_overlap
        exclude using gist (
        production_id with =,
        venue_id with =,
        run with &&
        );

create table plays.genres
(
    id   bigint generated always as identity primary key,
    name varchar(30)  not null unique,
    slug varchar(255) not null unique
);

create table plays.work_genres
(
    work_id  uuid   not null,
    genre_id bigint not null,

    primary key (work_id, genre_id),

    constraint fk_work_genres_on_works
        foreign key (work_id)
            references plays.works (id),

    constraint fk_work_genres_on_genres
        foreign key (genre_id)
            references plays.genres (id)
);

create table plays.characters
(
    id          bigint generated always as identity primary key,
    work_id     uuid         not null,
    name        varchar(255) not null,
    description text,

    constraint fk_characters_on_works
        foreign key (work_id)
            references plays.works (id)
            on delete cascade
);

create index idx_characters_work_id on plays.characters (work_id);

create table plays.work_credits
(
    id        bigint generated always as identity primary key,
    work_id   uuid                   not null,
    person_id bigint                 not null, -- fk -> people module (not created yet)
    role      plays.work_credit_role not null,

    constraint fk_work_credits_on_works
        foreign key (work_id)
            references plays.works (id),

    constraint uq_work_credits unique (work_id, person_id, role)
);

create index idx_work_credits_work_id on plays.work_credits (work_id);
create index idx_work_credits_id on plays.work_credits (person_id);

create table plays.production_credits
(
    id            bigint generated always as identity primary key,
    production_id uuid                         not null,
    person_id     bigint                       not null, -- fk -> people module (not created yet)
    role          plays.production_credit_role not null,
    character_id  bigint,

    constraint fk_production_credits_on_production
        foreign key (production_id)
            references plays.productions (id)
            on delete cascade,

    constraint fk_production_credits_on_character_id
        foreign key (character_id)
            references plays.characters (id)
            on delete set null
);

create index idx_production_credits_production_id on plays.production_credits (production_id);
create index idx_production_credits_person_id on plays.production_credits (person_id);
