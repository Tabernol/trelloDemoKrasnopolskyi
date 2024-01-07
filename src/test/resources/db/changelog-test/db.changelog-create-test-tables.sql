--liquibase formatted sql

--changeset max:1
CREATE SCHEMA IF NOT EXISTS krasnopolskyi;
--changeset max:2
CREATE TABLE IF NOT EXISTS krasnopolskyi.boards
(
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(64) NOT NULL,
    owner VARCHAR(64) NOT NULL
    );

--changeset max:3
CREATE TABLE IF NOT EXISTS krasnopolskyi.columns
(
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(64) NOT NULL,
    board_id BIGINT NOT NULL REFERENCES krasnopolskyi.boards (id) ON DELETE CASCADE
    );

--changeset max:4
CREATE TABLE IF NOT EXISTS krasnopolskyi.tasks
(
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(64) NOT NULL,
    description      VARCHAR(256),
    date_of_creation TIMESTAMP,
    status           VARCHAR(32),
    column_id        BIGINT NOT NULL REFERENCES krasnopolskyi.columns (id) ON DELETE CASCADE
    );
--changeset max:5
CREATE TABLE IF NOT EXISTS krasnopolskyi.columns_ordering
(
    column_id   BIGINT PRIMARY KEY REFERENCES krasnopolskyi.columns (id) ON DELETE CASCADE,
    board_id    BIGINT NOT NULL REFERENCES krasnopolskyi.boards (id) ON DELETE CASCADE,
    order_index INT NOT NULL,
    FOREIGN KEY (board_id) REFERENCES krasnopolskyi.boards (id) ON DELETE CASCADE
    );
--changeset max:6
CREATE TABLE IF NOT EXISTS krasnopolskyi.tasks_ordering
(
    task_id     BIGINT PRIMARY KEY REFERENCES krasnopolskyi.tasks (id) ON DELETE CASCADE,
    column_id   BIGINT NOT NULL REFERENCES krasnopolskyi.columns (id) ON DELETE CASCADE,
    order_index INT NOT NULL,
    FOREIGN KEY (column_id) REFERENCES krasnopolskyi.columns (id)
    );