CREATE SCHEMA IF NOT EXISTS medic;

CREATE TABLE medic."user"
(
    id SERIAL PRIMARY KEY
);

CREATE TABLE medic.tablet
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    image_url   VARCHAR(255),
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE medic.feature
(
    id    SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

CREATE TABLE medic.tablet_feature
(
    id         SERIAL PRIMARY KEY,
    tablet_id  INTEGER NOT NULL
        REFERENCES medic.tablet (id) ON DELETE CASCADE,
    feature_id INTEGER NOT NULL
        REFERENCES medic.feature (id) ON DELETE CASCADE,
    UNIQUE (tablet_id, feature_id)
);

CREATE TABLE medic.user_tablet
(
    id        SERIAL PRIMARY KEY,
    user_id   INTEGER NOT NULL
        REFERENCES medic."user" (id) ON DELETE CASCADE,
    tablet_id INTEGER NOT NULL
        REFERENCES medic.tablet (id) ON DELETE CASCADE,
    UNIQUE (user_id, tablet_id)
);

CREATE TABLE medic.user_feature
(
    id         SERIAL PRIMARY KEY,
    user_id    INTEGER NOT NULL
        REFERENCES medic."user" (id) ON DELETE CASCADE,
    feature_id INTEGER NOT NULL
        REFERENCES medic.feature (id) ON DELETE CASCADE,
    UNIQUE (user_id, feature_id)
);
