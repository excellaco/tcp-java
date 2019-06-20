CREATE TABLE oauth_access_token
(
    token_id          varchar(255) DEFAULT NULL,
    token             BYTEA,
    authentication_id varchar(255) DEFAULT NULL,
    user_name         varchar(255) DEFAULT NULL,
    client_id         varchar(255) DEFAULT NULL,
    authentication    BYTEA,
    refresh_token     varchar(255) DEFAULT NULL
);

CREATE TABLE oauth_refresh_token
(
    token_id       varchar(256) DEFAULT NULL,
    token          BYTEA,
    authentication BYTEA
);

create table oauth_client_details
(
    client_id               VARCHAR(256) PRIMARY KEY,
    resource_ids            VARCHAR(256),
    client_secret           VARCHAR(256),
    scope                   VARCHAR(256),
    authorized_grant_types  VARCHAR(256),
    web_server_redirect_uri VARCHAR(256),
    authorities             VARCHAR(256),
    access_token_validity   INTEGER,
    refresh_token_validity  INTEGER,
    additional_information  VARCHAR(4096),
    autoapprove             VARCHAR(256)
);


CREATE TABLE IF NOT EXISTS oauth_client_token
(
    token_id          VARCHAR(256),
    token             BYTEA,
    authentication_id VARCHAR(256) PRIMARY KEY,
    user_name         VARCHAR(256),
    client_id         VARCHAR(256)
);


CREATE TABLE IF NOT EXISTS oauth_code
(
    code           VARCHAR(256),
    authentication BYTEA
);


CREATE TABLE IF NOT EXISTS authorities
(
    username  VARCHAR(256) NOT NULL,
    authority VARCHAR(256) NOT NULL,
    PRIMARY KEY (username, authority)
);

CREATE TABLE users
(
    id            SERIAL      PRIMARY KEY,
    login         varchar(50) NOT NULL,
    password_hash char(60)    NOT NULL,
    first_name    varchar(50),
    last_name     varchar(50),
    email         varchar(100)
);

CREATE TABLE users_authorities
(
    user_id   integer     NOT NULL REFERENCES users (id),
    authority varchar(50) NOT NULL,
    PRIMARY KEY (user_id, authority)
);


CREATE UNIQUE INDEX users_login_uindex ON users (login);

ALTER TABLE oauth_access_token
    ADD CONSTRAINT oauth_access_token_users_login_fk
        FOREIGN KEY (user_name) REFERENCES users (login);

ALTER TABLE oauth_access_token
    ADD CONSTRAINT oauth_access_token_oauth_client_details_client_id_fk
        FOREIGN KEY (client_id) REFERENCES oauth_client_details (client_id);

CREATE UNIQUE INDEX oauth_refresh_token_token_id_uindex ON oauth_refresh_token (token_id);

INSERT INTO oauth_client_details(
        client_id, resource_ids, client_secret, scope, authorized_grant_types,
        access_token_validity, refresh_token_validity)
        VALUES ('web_app', 'api', '$2a$04$hqawBldLsWkFJ5CVsvtL7ed1z9yeoknfuszPOEHWzxfLBoViK6OVi', 'api',
    'implicit,refresh_token,password,authorization_code', 3600, 604800);

INSERT INTO users(login, password_hash, first_name, last_name, email)
VALUES ('user',
        '$2a$10$n3.lw0B.0kr1P9n1lSe73.FjetPkZjZ8gNk2PGIgffeAg174rXQca',
        'Tester', 'Test', 'test@test.org');