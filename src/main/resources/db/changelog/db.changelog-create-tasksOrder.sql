--liquibase formatted sql


--changeset max:1
CREATE TABLE IF NOT EXISTS krasnopolskyi.tasks_order
(
    id    BIGSERIAL PRIMARY KEY,
    column_id  BIGINT NOT NULL,
    task_id  BIGINT UNIQUE NOT NULL,
    order_index int NOT NULL
);