package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;


public interface BoardService extends BaseService<Board>{
    Board update(Board board, Long id) ;
}
