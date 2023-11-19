package com.krasnopolskyi.trellodemokrasnopolskyi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Objects;

/**
 * Entity class representing the ordering of columns in a Trello board.
 * <p>
 * This class is annotated with JPA annotations to define its mapping to the database.
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "columns_ordering", schema = "krasnopolskyi")
public class ColumnOrder {

    /**
     * The unique identifier of the column.
     */
    @Id
    @jakarta.persistence.Column(name = "column_id")
    private Long columnId;

    /**
     * The unique identifier of the board to which the column belongs.
     */
    @jakarta.persistence.Column(name = "board_id")
    private Long boardId;

    /**
     * The index representing the order of the column in the board.
     */
    @jakarta.persistence.Column(name = "order_index")
    private int orderIndex;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColumnOrder that = (ColumnOrder) o;
        return Objects.equals(columnId, that.columnId) && Objects.equals(boardId, that.boardId);
    }


    @Override
    public int hashCode() {
        return Objects.hash(columnId, boardId);
    }
}

