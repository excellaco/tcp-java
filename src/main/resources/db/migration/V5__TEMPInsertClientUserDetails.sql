-- This is temporary. Dynamically handling this will be handled in JA-27

INSERT INTO oauth_client_details (client_id, client_secret, scope, authorized_grant_types, authorities, access_token_validity)
VALUES ('app', '{bcrypt}$2a$10$p85zHPktuZ.jd/BC/ZAGmeWiJIO0lvAQI0T5REavmA7XavdA9J5O.', 'read,write', 'password,refresh_token,client_credentials', 'ROLE_CLIENT', 300);
INSERT INTO users (id, email, username, password, enabled) VALUES (1, 'john@winchester.com', 'user', '{bcrypt}$2a$10$cyf5NfobcruKQ8XGjUJkEegr9ZWFqaea6vjpXWEaSqTa2xL9wjgQC', TRUE);
INSERT INTO authorities (user_id, username, authority) VALUES (1, 'user', 'ROLE_USER');
INSERT INTO users (id, email, username, password, enabled) VALUES (2, 'leslie.knope@in.parks.gov', 'admin', '{bcrypt}$2a$10$cyf5NfobcruKQ8XGjUJkEegr9ZWFqaea6vjpXWEaSqTa2xL9wjgQC', TRUE);
INSERT INTO authorities (user_id, username, authority) VALUES (2, 'admin', 'ROLE_ADMIN');
INSERT INTO users (id, email, username, password, enabled) VALUES (3, 'test@excella.com', 'user2', '{bcrypt}$2a$10$cyf5NfobcruKQ8XGjUJkEegr9ZWFqaea6vjpXWEaSqTa2xL9wjgQC', true);
INSERT INTO authorities (user_id, username, authority) VALUES (3, 'user2', 'ROLE_USER');
