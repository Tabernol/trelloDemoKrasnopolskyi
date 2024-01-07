package com.krasnopolskyi.trellodemokrasnopolskyi.http.rest;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.BoardNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TrelloException;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.BoardMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.BoardService;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

/**
 * REST controller class that handles board-related endpoints.
 * @author Maksym Krasnopolskyi
 */
@RestController
@RequestMapping("/api/v1/boards")
@Validated
@Slf4j(topic = "BOARD_CONTROLLER")
public class BoardRestController {
    private final BoardService boardService;

    /**
     * Constructs a new BoardRestController with the given dependencies.
     *
     * @param boardService The service for managing boards.
     */
    public BoardRestController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * Retrieves a board by its ID.
     *
     * @param id The ID of the board to retrieve.
     * @return The board response entity.
     * @throws TrelloException If the board is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BoardReadResponse> getBoardById(
            @PathVariable("id") @Min(1) Long id) throws TrelloException {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.findById(id));
    }

    /**
     * Retrieves all boards.
     *
     * @return The list of board response entities.
     */
    @GetMapping
    public ResponseEntity<List<BoardReadResponse>> getAllBoards() {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.findAll());
    }

    /**
     * Creates a new board.
     *
     * @param boardCreateRequest The request body containing board details.
     * @return The created board response entity.
     * @throws TrelloException If an error occurs during board creation.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BoardReadResponse> createBoard(
            @Validated @RequestBody BoardCreateRequest boardCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.create(boardCreateRequest));
    }

    /**
     * Updates an existing board.
     *
     * @param id                The ID of the board to update.
     * @param boardEditRequest  The request body containing updated board details.
     * @return The updated board response entity.
     * @throws BoardNotFoundExceptionTrello If the board is not found.
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardReadResponse> updateBoard(
            @PathVariable("id") @Min(1) Long id,
            @Validated @RequestBody BoardEditRequest boardEditRequest) throws BoardNotFoundExceptionTrello {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.update(boardEditRequest, id));
    }

    /**
     * Deletes a board by its ID.
     *
     * @param id The ID of the board to delete.
     * @return The response entity indicating success or failure.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable("id") @Min(1) Long id) {
        return boardService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
