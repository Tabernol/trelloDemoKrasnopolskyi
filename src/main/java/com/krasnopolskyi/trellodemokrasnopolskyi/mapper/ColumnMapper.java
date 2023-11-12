package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnEditDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnReadDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ColumnMapper implements BaseMapper<Column, ColumnReadDto, ColumnPostDto> {

    public Column mapToEntity(ColumnPostDto columnPostDto) {
        return Column.builder()
                .name(columnPostDto.getName())
                .board(Board.builder().id(columnPostDto.getBoardId()).build())
                .build();
    }

    public Column mapToEntity(ColumnEditDto columnEditDto) {
        return Column.builder()
                .name(columnEditDto.getName())
                .build();
    }

    public ColumnReadDto mapToDto(Column column) {
        return ColumnReadDto.builder()
                .id(column.getId())
                .name(column.getName())
                .boardId(column.getBoard().getId())
                .tasks(column.getTasks())
                .build();
    }

    public List<ColumnReadDto> mapAll(List<Column> source) {
        List<ColumnReadDto> result = new ArrayList<>();
        for (Column column : source) {
            result.add(mapToDto(column));
        }
        return result;
    }


}
