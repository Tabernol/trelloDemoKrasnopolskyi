package com.krasnopolskyi.trellodemokrasnopolskyi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

/**
 * Entity class representing the ordering of tasks within a column in a Trello board.
 * <p>
 * This class is annotated with JPA annotations to define its mapping to the database.
 * @author Maksym Krasnopolskyi
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks_ordering", schema = "krasnopolskyi")
public class TaskOrder {
    /**
     * The unique identifier of the task.
     */
    @Id
    @jakarta.persistence.Column(name = "task_id")
    private Long taskId;

    /**
     * The unique identifier of the column to which the task belongs.
     */
    @jakarta.persistence.Column(name = "column_id")
    private Long columnId;

    /**
     * The index representing the order of the task within the column.
     */
    @jakarta.persistence.Column(name = "order_index")
    private int orderIndex;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskOrder taskOrder = (TaskOrder) o;
        return Objects.equals(taskId, taskOrder.taskId) && Objects.equals(columnId, taskOrder.columnId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, columnId);
    }
}


