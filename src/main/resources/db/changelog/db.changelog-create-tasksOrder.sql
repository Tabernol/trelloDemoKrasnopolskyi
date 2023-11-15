--liquibase formatted sql


--changeset max:1
CREATE TABLE IF NOT EXISTS krasnopolskyi.tasks_ordering
(
    task_id     BIGINT PRIMARY KEY REFERENCES krasnopolskyi.tasks (id) ON DELETE CASCADE,
    column_id   BIGINT NOT NULL REFERENCES krasnopolskyi.columns (id) ON DELETE CASCADE,
    order_index INT NOT NULL,
    FOREIGN KEY (column_id) REFERENCES krasnopolskyi.columns (id)
    );