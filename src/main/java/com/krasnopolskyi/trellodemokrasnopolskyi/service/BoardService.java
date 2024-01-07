package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.BoardNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TrelloException;

import java.util.List;


public interface BoardService {
    BoardReadResponse update(BoardEditRequest board, Long id) throws BoardNotFoundExceptionTrello;

    BoardReadResponse findById(Long id) throws BoardNotFoundExceptionTrello;

    BoardReadResponse create(BoardCreateRequest entity);

    List<BoardReadResponse> findAll();

    boolean delete(Long id);
}
