package com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto;


import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardReadResponse {
    private Long id;
    private String name;
    private String owner;
    private List<Column> columns;
}
