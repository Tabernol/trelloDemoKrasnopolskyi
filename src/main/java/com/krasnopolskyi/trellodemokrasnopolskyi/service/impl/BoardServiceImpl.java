package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.BoardNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.BoardRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.BoardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public Board findById(Long id) {
        return boardRepository.findById(id).orElseThrow(
                () -> new BoardNotFoundExceptionTrello("Board with id " + id + " not found"));
    }

    @Override
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    @Override
    @Transactional
    public Board create(Board entity) {
        return boardRepository.save(entity);
    }

    @Override
    public Board update(Board board, Long id) {
        Board existingBoard = findById(id);
            if (board.getName() != null) {
                existingBoard.setName(board.getName());
            }
        return boardRepository.save(existingBoard);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return boardRepository.findById(id)
                .map(entity -> {
                    boardRepository.delete(entity);
                    boardRepository.flush();
                    return true;
                }).orElse(false);
    }
}
