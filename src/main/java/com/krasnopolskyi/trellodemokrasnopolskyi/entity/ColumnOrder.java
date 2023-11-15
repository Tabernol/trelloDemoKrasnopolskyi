package com.krasnopolskyi.trellodemokrasnopolskyi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "columns_ordering", schema = "krasnopolskyi")
public class ColumnOrder {

    @Id
    @jakarta.persistence.Column(name = "column_id")
    private Long columnId;

    @jakarta.persistence.Column(name = "board_id")
    private Long boardId;

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
