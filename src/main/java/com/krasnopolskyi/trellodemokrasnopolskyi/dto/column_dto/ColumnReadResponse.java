package com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColumnReadResponse {
    private Long id;
    private String name;
    private Long boardId;
    private List<Task> tasks;
}
