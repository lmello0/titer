ALTER TABLE users.users
    ADD COLUMN profile_picture_file_id uuid null;

ALTER TABLE users.users
    ADD CONSTRAINT fk_users_profile_picture_on_files
        FOREIGN KEY (profile_picture_file_id)
            REFERENCES public.files (id);

ALTER TABLE users.users
    DROP COLUMN profile_picture;