ALTER TABLE users.users
    ADD COLUMN created_by  VARCHAR(50),
    ADD COLUMN created_at  TIMESTAMPTZ DEFAULT now() NOT NULL,
    ADD COLUMN modified_by VARCHAR(50),
    ADD COLUMN modified_at TIMESTAMPTZ,
    ADD COLUMN deleted_by  VARCHAR(50),
    ADD COLUMN deleted_at  TIMESTAMPTZ;

UPDATE users.users u
SET CREATED_BY = u.id,
    CREATED_AT = now();

ALTER TABLE users.users
    ALTER COLUMN created_by SET NOT NULL;
