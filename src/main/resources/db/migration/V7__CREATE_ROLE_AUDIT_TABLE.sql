CREATE TABLE users.user_role_audits
(
    id           uuid primary key not null default uuidv7(),
    user_id      uuid             not null,
    role_id      bigint           not null,
    action       varchar(20)      not null,
    performed_by varchar(50)      not null,
    reason       text,
    created_at   timestamptz      not null default now(),
    constraint fk_user_role_audits_on_users foreign key (user_id) references users.users (id),
    constraint fk_user_role_audits_on_roles foreign key (role_id) references users.roles (id),
    constraint check_user_role_audits_action check (action in ('GRANTED', 'REVOKED'))
);