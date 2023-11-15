--liquibase formatted sql


--changeset max:1
CREATE TABLE IF NOT EXISTS krasnopolskyi.tasks_order
(
    task_id     BIGINT PRIMARY KEY REFERENCES krasnopolskyi.tasks (id) ON DELETE CASCADE,
    column_id   BIGINT NOT NULL,
    order_index INT NOT NULL
    );