ALTER TABLE users.users
    ADD COLUMN deactivated_at timestamptz;
ALTER TABLE users.users
    ADD COLUMN deactivated_by varchar(50);