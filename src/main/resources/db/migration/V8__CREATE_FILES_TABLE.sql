CREATE TABLE files
(
    id               UUID primary key default uuidv7(),

    original_name    varchar(255),
    stored_name      varchar(255)                   not null,
    content_type     varchar(100)                   not null,
    size_bytes       bigint                         not null,

    storage_provider varchar(30)                    not null check (storage_provider in ('BUCKET', 'LOCAL', 'DATABASE')),

    bucket           varchar(255),
    storage_key      varchar(1024),
    url              varchar(2048),

    path             varchar(2048),

    data             bytea,

    created_by       varchar(50)                    not null,
    created_at       timestamptz      default now() not null,

    constraint files_s3_check check (
        storage_provider <> 'S3'
            or (
            bucket is not null
                and storage_key is not null
                and url is not null
            )
        ),

    constraint files_local_check check (
        storage_provider <> 'LOCAL'
            or path is not null
        ),

    constraint files_database_check check (
        storage_provider <> 'DATABASE'
            or data is not null
        )
);