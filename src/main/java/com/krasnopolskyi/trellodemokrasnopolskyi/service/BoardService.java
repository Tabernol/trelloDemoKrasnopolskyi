package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;

import java.util.Optional;

public interface BoardService extends BaseService<Board>{
    Optional<Board> update(Board board, Long id);
}
