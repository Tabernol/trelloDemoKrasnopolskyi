--liquibase formatted sql


--changeset max:1
CREATE TABLE IF NOT EXISTS krasnopolskyi.columns_ordering
(
    column_id   BIGINT PRIMARY KEY REFERENCES krasnopolskyi.columns (id) ON DELETE CASCADE,
    board_id    BIGINT NOT NULL REFERENCES krasnopolskyi.boards (id) ON DELETE CASCADE,
    order_index INT NOT NULL,
    FOREIGN KEY (board_id) REFERENCES krasnopolskyi.boards (id) ON DELETE CASCADE
    );