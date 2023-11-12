package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardReadDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import org.springframework.stereotype.Component;

@Component
public class BoardMapper implements BaseMapper<Board, BoardReadDto, BoardPostDto> {

    public Board mapToEntity(BoardPostDto boardPostDto) {
        return Board.builder()
                .name(boardPostDto.getName())
                .owner(boardPostDto.getOwner())
                .build();
    }


    public BoardReadDto mapToDto(Board board) {
        return BoardReadDto.builder()
                .id(board.getId())
                .name(board.getName())
                .owner(board.getOwner())
                .columns(board.getColumns())
                .build();

    }
}
