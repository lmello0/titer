ALTER TABLE users.users
    DROP COLUMN profile_picture_file_id;

DROP TABLE public.files;

CREATE TABLE public.files
(
    id                  UUID PRIMARY KEY DEFAULT uuidv7(),
    filename            VARCHAR(255)                NOT NULL,
    content_type        VARCHAR(100)                NOT NULL,
    size_bytes          BIGINT                      NOT NULL,
    original_size_bytes BIGINT                      NOT NULL,
    status              VARCHAR(255)                NOT NULL,
    provider            VARCHAR(30)                 NOT NULL,
    public_url          VARCHAR(2048),
    storage_key         VARCHAR(255)                NOT NULL,
    media_width         INTEGER,
    media_height        INTEGER,
    media_duration_ms   BIGINT,
    media_codec         VARCHAR(255),
    media_color_space   VARCHAR(255),
    media_has_alpha     BOOLEAN,
    custom_metadata     JSONB,
    created_at          TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    processed_at        TIMESTAMP WITHOUT TIME ZONE,
    failure_reason      VARCHAR(255)
);

ALTER TABLE users.users
    ADD COLUMN profile_picture_file_id uuid null;

ALTER TABLE users.users
    ADD CONSTRAINT fk_users_profile_picture_on_files
        FOREIGN KEY (profile_picture_file_id)
            REFERENCES public.files (id);