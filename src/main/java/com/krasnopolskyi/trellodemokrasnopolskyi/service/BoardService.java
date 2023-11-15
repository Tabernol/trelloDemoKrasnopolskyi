package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.BoardNotFoundExceptionTrello;


public interface BoardService extends BaseService<Board>{
    Board update(Board board, Long id) throws BoardNotFoundExceptionTrello;
}
