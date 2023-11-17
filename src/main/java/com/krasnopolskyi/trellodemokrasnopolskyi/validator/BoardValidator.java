package com.krasnopolskyi.trellodemokrasnopolskyi.validator;

import com.krasnopolskyi.trellodemokrasnopolskyi.exception.BoardNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.BoardRepository;
import org.springframework.stereotype.Component;

@Component
public class BoardValidator {
    private final BoardRepository boardRepository;

    public BoardValidator(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public boolean validate(Long id) throws BoardNotFoundExceptionTrello {
        if (boardRepository.existsById(id)) {
            return true;
        } else throw new BoardNotFoundExceptionTrello("Board with id " + id + " not found");
    }
}
