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


    public void validate(Long id) {
        boardRepository.findById(id).orElseThrow(
                () -> new BoardNotFoundExceptionTrello("Board with id " + id + " not found"));
    }
}
