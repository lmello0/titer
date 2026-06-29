CREATE TABLE auth.refresh_tokens
(
    id          uuid primary key     DEFAULT uuidv7() NOT NULL,
    user_id     uuid        not null,
    token_hash  varchar(64) not null unique,
    issued_at   timestamptz not null default now(),
    expires_at  timestamptz not null,
    revoked_at  timestamptz,
    replaced_by uuid,

    constraint uq_refresh_tokens_hash unique (token_hash),

    constraint fk_refresh_tokens_on_users
        foreign key (user_id)
            references users.users (id)
);

CREATE INDEX idx_refresh_tokens_user_id ON auth.refresh_tokens (user_id);

CREATE INDEX idx_refresh_tokens_user_active
    ON auth.refresh_tokens (user_id) WHERE revoked_at IS NULL;