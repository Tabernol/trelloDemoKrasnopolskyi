--liquibase formatted sql

--changeset max:1
INSERT INTO krasnopolskyi.boards (name, owner)
VALUES
    ('Board 1', 'Owner 1'),
    ('Board 2', 'Owner 2')


--changeset max:2
INSERT INTO krasnopolskyi.columns (name, board_id)
VALUES
    ('Column 1', 1),
    ('Column 2', 1),
    ('Column 3', 1),
    ('to do', 2),
    ('in progress', 2),
    ('done', 2)


--changeset max:3
INSERT INTO krasnopolskyi.tasks (name, description, date_of_creation, column_id)
VALUES
    ('Task 1', 'Description 1', '2023-11-10 10:00:00', 1),
    ('Task 2', 'Description 2', '2023-11-10 11:00:00', 1),
    ('Task 3', 'Description 3', '2023-11-10 12:00:00', 2),
    ('Task 4', 'Description 4', '2023-11-10 13:00:00', 2)
