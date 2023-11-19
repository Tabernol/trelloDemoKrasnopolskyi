package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.BoardNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.BoardRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.BoardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service class that provides business logic for board-related operations.
 */
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    /**
     * Constructs a new BoardServiceImpl with the given BoardRepository.
     *
     * @param boardRepository The repository for board entities.
     */
    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /**
     * Retrieves a board by its ID.
     *
     * @param id The ID of the board to retrieve.
     * @return The board with the specified ID.
     * @throws BoardNotFoundExceptionTrello If no board with the specified ID is found.
     */
    @Override
    public Board findById(Long id) throws BoardNotFoundExceptionTrello {
        return boardRepository.findById(id).orElseThrow(
                () -> new BoardNotFoundExceptionTrello("Board with id " + id + " not found"));
    }

    /**
     * Retrieves all boards.
     *
     * @return A list of all boards.
     */
    @Override
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    /**
     * Creates a new board.
     *
     * @param entity The board entity to create.
     * @return The created board entity.
     */
    @Override
    @Transactional
    public Board create(Board entity) {
        return boardRepository.save(entity);
    }

    /**
     * Updates an existing board with the provided information.
     *
     * @param board The updated board information.
     * @param id    The ID of the board to update.
     * @return The updated board entity.
     * @throws BoardNotFoundExceptionTrello If no board with the specified ID is found.
     */
    @Override
    @Transactional
    public Board update(Board board, Long id) throws BoardNotFoundExceptionTrello {
        Board existingBoard = findById(id);
        if (board.getName() != null) {
            existingBoard.setName(board.getName());
        }
        return boardRepository.save(existingBoard);
    }

    /**
     * Deletes a board by its ID.
     *
     * @param id The ID of the board to delete.
     * @return {@code true} if the board is successfully deleted, {@code false} otherwise.
     */
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
