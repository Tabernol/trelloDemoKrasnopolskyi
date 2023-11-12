package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.BoardRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.BoardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public Optional<Board> findById(Long id) {
        return boardRepository.findById(id);
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
    public Optional<Board> update(Board board, Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if(optionalBoard.isPresent()){
            Board existingBoard = optionalBoard.get();
            if(board.getName() != null){
                existingBoard.setName(board.getName());
                boardRepository.save(existingBoard);
            }
        }
        return boardRepository.findById(id);
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
