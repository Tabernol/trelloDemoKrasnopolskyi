package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.BoardService;
import org.springframework.stereotype.Component;

@Component
public class ColumnMapper implements BaseMapper<Column, ColumnPostDto> {

    private final BoardService boardService;

    public ColumnMapper(BoardService boardService) {
        this.boardService = boardService;
    }



    public Column mapToEntity(ColumnPostDto columnPostDto) {
        return Column.builder()
                .name(columnPostDto.getName())
                .board(Board.builder().id(columnPostDto.getBoardId()).build())
                .build();
    }
}
