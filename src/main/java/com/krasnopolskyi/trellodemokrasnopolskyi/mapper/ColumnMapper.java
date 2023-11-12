package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardReadDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnReadDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.BoardService;
import org.springframework.stereotype.Component;

@Component
public class ColumnMapper implements BaseMapper<Column, ColumnReadDto, ColumnPostDto> {

    public Column mapToEntity(ColumnPostDto columnPostDto) {
        return Column.builder()
                .name(columnPostDto.getName())
                .board(Board.builder().id(columnPostDto.getBoardId()).build())
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
}
