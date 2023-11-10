package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.post.ColumnPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ColumnMapper implements BaseMapper<Column, ColumnPostDto> {

    private final BoardService boardService;

    public ColumnMapper(BoardService boardService) {
        this.boardService = boardService;
    }


    @Override
    public ColumnPostDto mapToDto(Column column) {
        return null;
    }

    public Column mapToEntity(ColumnPostDto columnPostDto) {
        return Column.builder()
                .name(columnPostDto.getName())
                .board(boardService.findById(columnPostDto.getBoardId()).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .build();
    }
}
