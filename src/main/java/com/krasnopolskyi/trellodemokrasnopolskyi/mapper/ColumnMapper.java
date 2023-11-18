package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ColumnMapper implements BaseMapper<Column, ColumnReadResponse, ColumnCreateRequest> {

    public Column mapToEntity(ColumnCreateRequest columnCreateRequest) {
        return Column.builder()
                .name(columnCreateRequest.getName())
                .board(Board.builder().id(columnCreateRequest.getBoardId()).build())
                .build();
    }

    public Column mapToEntity(ColumnEditRequest columnEditRequest) {
        return Column.builder()
                .name(columnEditRequest.getName())
                .build();
    }

    public ColumnReadResponse mapToDto(Column column) {
        return ColumnReadResponse.builder()
                .id(column.getId())
                .name(column.getName())
                .boardId(column.getBoard().getId())
                .tasks(column.getTasks())
                .build();
    }

    public List<ColumnReadResponse> mapAll(List<Column> source) {
        List<ColumnReadResponse> result = new ArrayList<>();
        for (Column column : source) {
            result.add(mapToDto(column));
        }
        return result;
    }
}
