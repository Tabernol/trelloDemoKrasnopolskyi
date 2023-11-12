package com.krasnopolskyi.trellodemokrasnopolskyi.validator;

import com.krasnopolskyi.trellodemokrasnopolskyi.repository.BoardRepository;
import org.springframework.stereotype.Component;

@Component
public class BoardValidator {

    private final BoardRepository boardRepository;

    public BoardValidator(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }


    public boolean isColumnExist(Long id) {
        return boardRepository.existsById(id);
    }
}
