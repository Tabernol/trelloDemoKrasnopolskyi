package com.krasnopolskyi.trellodemokrasnopolskyi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity class representing a task in a Trello column.
 * <p>
 * This class is annotated with JPA annotations to define its mapping to the database.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks", schema = "krasnopolskyi")
public class Task {

    /**
     * The unique identifier for the task.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the task.
     */
    private String name;

    /**
     * The description of the task.
     */
    private String description;

    /**
     * The date and time when the task was created.
     */
    private LocalDateTime dateOfCreation;

    /**
     * The column to which this task belongs.
     * <p>
     * This is a many-to-one relationship with the {@link Column} entity.
     * The column is mapped by the "tasks" field in this entity.
     * It is fetched lazily to avoid loading unnecessary data.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "column_id")
    @JsonIgnore
    private Column column;
}

