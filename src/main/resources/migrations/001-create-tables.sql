CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS sessions (
                                        id SERIAL PRIMARY KEY,
                                        user_id SERIAL NOT NULL,
                                        token TEXT NOT NULL UNIQUE,
                                        expiration_time TIMESTAMP NOT NULL,
                                        active BOOLEAN NOT NULL DEFAULT TRUE
);

ALTER TABLE sessions
    ADD CONSTRAINT fk_sessions_user
        FOREIGN KEY(user_id)
            REFERENCES users(id)
            ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS banned_tokens (
                                             token TEXT PRIMARY KEY
);
