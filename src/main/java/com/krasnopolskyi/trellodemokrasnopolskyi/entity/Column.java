package com.krasnopolskyi.trellodemokrasnopolskyi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entity class representing a column in a Trello board.
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
@Table(name = "columns", schema = "krasnopolskyi")
public class Column {

    /**
     * The unique identifier for the column.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the column.
     */
    private String name;

    /**
     * The board to which this column belongs.
     * <p>
     * This is a many-to-one relationship with the {@link Board} entity.
     * The board is mapped by the "board" field in this entity.
     * It is fetched lazily to avoid loading unnecessary data.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @JsonIgnore
    private Board board;

    /**
     * The list of tasks associated with the column.
     * <p>
     * This is a bidirectional one-to-many relationship with the {@link Task} entity.
     * The tasks are mapped by the "column" field in the Task entity.
     */
    @OneToMany(mappedBy = "column")
    private List<Task> tasks;
}
