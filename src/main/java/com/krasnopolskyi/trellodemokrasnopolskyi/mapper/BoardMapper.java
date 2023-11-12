package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardEditDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardReadDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BoardMapper implements BaseMapper<Board, BoardReadDto, BoardPostDto> {

    public Board mapToEntity(BoardPostDto boardPostDto) {
        return Board.builder()
                .name(boardPostDto.getName())
                .owner(boardPostDto.getOwner())
                .build();
    }

    public Board mapToEntity(BoardEditDto boardEditDto) {
        return Board.builder()
                .name(boardEditDto.getName())
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

    public List<BoardReadDto> mapAll(List<Board> source) {
        List<BoardReadDto> result = new ArrayList<>();
        for (Board board : source) {
            result.add(mapToDto(board));
        }
        return result;
    }
}
