package com.krasnopolskyi.trellodemokrasnopolskyi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks_ordering", schema = "krasnopolskyi")
public class TaskOrder {
    @Id
    @jakarta.persistence.Column(name = "task_id")
    private Long taskId;

    @jakarta.persistence.Column(name = "column_id")
    private Long columnId;

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

