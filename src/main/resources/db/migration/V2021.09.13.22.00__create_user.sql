CREATE TABLE system_user
(
    id                      serial primary key,
    username                varchar(255) not null,
    password                varchar(255) not null,
    email                   varchar(255) not null,
    authorities             varchar(255) not null,
    account_non_expired     boolean      not null,
    account_non_locked      boolean      not null,
    credentials_non_expired boolean      not null,
    enabled                 boolean      not null
);

INSERT INTO system_user (account_non_expired, account_non_locked, authorities, credentials_non_expired, enabled,
                         password, username, email)
VALUES (true, true, 'ROLE_USER', true, true, 'password', 'user', 'user@email.com'),
       (true, true, 'ROLE_ADMIN', true, true, 'password', 'admin', 'admin@email.com');