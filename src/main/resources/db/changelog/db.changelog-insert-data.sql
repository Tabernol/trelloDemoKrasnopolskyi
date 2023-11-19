--liquibase formatted sql

--changeset max:1
INSERT INTO krasnopolskyi.boards (name, owner)
VALUES ('Board 1', 'Owner 1'),
       ('Board 2', 'Owner 2')


--changeset max:2
    INSERT
INTO krasnopolskyi.columns (name, board_id)
VALUES
    ('column 1', 1),
    ('column 2', 1),
    ('column 3', 1),
--
    ('to do', 2),
    ('in progress', 2),
    ('done', 2)

--changeset max:3
INSERT INTO krasnopolskyi.columns_ordering (column_id, board_id, order_index)
VALUES
    (1, 1, 1),
    (2, 1, 2),
    (3, 1, 3),
--
    (4, 2, 1),
    (5, 2, 2),
    (6, 2, 3)


--changeset max:4
INSERT INTO krasnopolskyi.tasks (name, description, date_of_creation, column_id)
VALUES
    ('Task 1', 'Description 1', '2023-11-10 10:00:00', 1),
    ('Task 2', 'Description 2', '2023-11-10 11:00:00', 1),
    ('Task 3', 'Description 3', '2023-11-10 11:00:00', 1),
    ('Task 4', 'Description 4', '2023-11-10 11:00:00', 1),
--
    ('first', 'Description', '2023-11-10 11:00:00', 2),
    ('second', 'Description', '2023-11-10 11:00:00', 2),
    ('third', 'Description', '2023-11-10 11:00:00', 2),

    ('for to do', 'board_2', '2023-11-10 11:00:00', 4),
    ('for in progress', 'board_2', '2023-11-10 11:00:00', 5),
    ('for done', 'board_2', '2023-11-10 11:00:00', 6)


--changeset max:5
INSERT INTO krasnopolskyi.tasks_ordering (task_id, column_id, order_index)
VALUES
    (1, 1, 1),
    (2, 1, 2),
    (3, 1, 3),
    (4, 1, 4),
--
    (5, 2, 1),
    (6, 2, 2),
    (7, 2, 3),

    (8, 4, 1),
    (9, 5, 1),
    (10, 6, 1)



