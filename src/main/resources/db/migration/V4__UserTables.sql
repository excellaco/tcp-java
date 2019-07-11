CREATE TABLE IF NOT EXISTS users
(
    id       SERIAL PRIMARY KEY,
    email    VARCHAR(256) NOT NULL UNIQUE,
    username VARCHAR(256) NOT NULL UNIQUE,
    password VARCHAR(256) NOT NULL,
    enabled  BOOLEAN,
    credentials_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_locked BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS authorities
(
    user_id INT NOT NULL REFERENCES users (id),
    username  VARCHAR(256) NOT NULL,
    authority VARCHAR(256) NOT NULL,
    PRIMARY KEY (username, authority)
);