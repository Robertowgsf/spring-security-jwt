CREATE TABLE token
(
    refresh_token uuid primary key DEFAULT gen_random_uuid(),
    jwt           text      not null,
    created_at    timestamp not null
)