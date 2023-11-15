package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BoardMapper implements BaseMapper<Board, BoardReadResponse, BoardCreateRequest> {

    public Board mapToEntity(BoardCreateRequest boardCreateRequest) {
        return Board.builder()
                .name(boardCreateRequest.getName())
                .owner(boardCreateRequest.getOwner())
                .build();
    }

    public Board mapToEntity(BoardEditRequest boardEditRequest) {
        return Board.builder()
                .name(boardEditRequest.getName())
                .build();
    }


    public BoardReadResponse mapToDto(Board board) {
        return BoardReadResponse.builder()
                .id(board.getId())
                .name(board.getName())
                .owner(board.getOwner())
                .columns(board.getColumns())
                .build();

    }

    public List<BoardReadResponse> mapAll(List<Board> source) {
        List<BoardReadResponse> result = new ArrayList<>();
        for (Board board : source) {
            result.add(mapToDto(board));
        }
        return result;
    }
}
