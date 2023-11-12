package com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColumnReadDto {
    private Long id;
    private String name;
    private Long boardId;
    private List<Task> tasks;
}
