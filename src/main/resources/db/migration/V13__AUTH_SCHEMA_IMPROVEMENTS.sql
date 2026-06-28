ALTER TABLE users.users
    ADD COLUMN email_verified boolean NOT NULL DEFAULT false;

ALTER TABLE users.users
    ALTER COLUMN username TYPE varchar(30);

ALTER TABLE users.users
    DROP CONSTRAINT users_email_key;
ALTER TABLE users.users
    DROP CONSTRAINT users_username_key;

CREATE UNIQUE INDEX uq_users_email_active
    ON users.users (lower(email)) WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX uq_users_username_active
    ON users.users (lower(username)) WHERE deleted_at IS NULL;


ALTER TABLE users.user_auths
    ADD COLUMN created_at  timestamptz NOT NULL DEFAULT now(),
    ADD COLUMN modified_at timestamptz NULL;

ALTER TABLE users.user_auths
    ADD CONSTRAINT check_user_auths_provider
        CHECK (provider IN ('LOCAL', 'GOOGLE'));

ALTER TABLE users.user_auths
    ADD CONSTRAINT check_local_has_password
        CHECK (provider <> 'LOCAL' OR password_hash IS NOT NULL),
    ADD CONSTRAINT check_social_has_provider_id
        CHECK (provider = 'LOCAL' OR provider_id IS NOT NULL);

ALTER TABLE users.user_auths
    ADD CONSTRAINT uq_user_auths_provider_identity
        UNIQUE (provider, provider_id);


CREATE INDEX idx_user_roles_user_id
    ON users.user_roles (user_id);

CREATE INDEX idx_user_role_audits_user_id
    ON users.user_role_audits (user_id);


CREATE TABLE users.email_verification_tokens
(
    id         uuid        DEFAULT uuidv7() NOT NULL,
    user_id    uuid                         NOT NULL,
    email      varchar(255)                 NOT NULL, -- address being verified
    token_hash varchar(255)                 NOT NULL, -- hash of the token, not the token
    expires_at timestamptz                  NOT NULL,
    used_at    timestamptz                  NULL,     -- non-null = already consumed
    created_at timestamptz DEFAULT now()    NOT NULL,
    CONSTRAINT pk_email_verification_tokens PRIMARY KEY (id),
    CONSTRAINT fk_evt_on_users
        FOREIGN KEY (user_id) REFERENCES users.users (id) ON DELETE CASCADE
);


CREATE UNIQUE INDEX uq_evt_token_hash
    ON users.email_verification_tokens (token_hash);
CREATE INDEX idx_evt_user_id
    ON users.email_verification_tokens (user_id);

CREATE SCHEMA IF NOT EXISTS AUTH;

ALTER TABLE users.user_auths
    SET schema AUTH;