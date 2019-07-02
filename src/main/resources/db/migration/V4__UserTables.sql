CREATE TABLE IF NOT EXISTS users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(256) NOT NULL UNIQUE,
    password VARCHAR(256) NOT NULL,
    enabled  BOOLEAN
);

CREATE TABLE IF NOT EXISTS authorities
(
    username  VARCHAR(256) NOT NULL,
    authority VARCHAR(256) NOT NULL,
    PRIMARY KEY (username, authority)
);