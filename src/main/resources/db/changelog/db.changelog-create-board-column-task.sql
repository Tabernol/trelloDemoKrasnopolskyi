--liquibase formatted sql

--changeset max:1
CREATE SCHEMA IF NOT EXISTS krasnopolskyi AUTHORIZATION postgres;

--changeset max:2
CREATE TABLE IF NOT EXISTS krasnopolskyi.boards
(
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(64) NOT NULL,
    owner VARCHAR(64) NOT NULL
);

--changeset max:3
CREATE TABLE IF NOT EXISTS krasnopolskyi.pillars
(
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(64) NOT NULL,
    board_id BIGINT NOT NULL REFERENCES krasnopolskyi.boards (id)
);

--changeset max:4
CREATE TABLE IF NOT EXISTS krasnopolskyi.tasks
(
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(64) NOT NULL,
    description      VARCHAR(256),
    date_of_creation TIMESTAMP,
    pillar_id        BIGINT NOT NULL REFERENCES krasnopolskyi.pillars (id)
);