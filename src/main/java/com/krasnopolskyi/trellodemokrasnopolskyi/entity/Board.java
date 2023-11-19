package com.krasnopolskyi.trellodemokrasnopolskyi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
/**
 * Entity class representing a Trello board.
 * <p>
 * This class is annotated with JPA annotations to define its mapping to the database.
 * @author Maksym Krasnopolskyi
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "boards", schema = "krasnopolskyi")
public class Board {
    /**
     * The unique identifier for the board.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The name of the board.
     */
    private String name;
    /**
     * The owner of the board. It is an email.
     */
    private String owner;
    /**
     * The list of columns associated with the board.
     * <p>
     * This is a bidirectional one-to-many relationship with the {@link Column} entity.
     * The columns are mapped by the "board" field in the Column entity.
     */
    @OneToMany(mappedBy = "board")
    @JsonIgnore
    private List<Column> columns;
}
