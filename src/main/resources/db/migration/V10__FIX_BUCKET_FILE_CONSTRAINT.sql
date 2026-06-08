ALTER TABLE files
DROP
CONSTRAINT files_s3_check;

ALTER TABLE files
    ADD CONSTRAINT files_bucket_check check (
        storage_provider <> 'BUCKET'
            or (
            bucket is not null
                and storage_key is not null
                and url is not null
            )
        );
