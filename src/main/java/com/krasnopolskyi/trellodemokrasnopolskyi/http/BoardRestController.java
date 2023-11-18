package com.krasnopolskyi.trellodemokrasnopolskyi.http;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TrelloEntityNotFoundException;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.BoardMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.BoardService;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequestMapping("/api/v1/boards")
@Validated()
@Slf4j
public class BoardRestController {
    private final BoardService boardService;
    private final BoardMapper boardMapper;

    public BoardRestController(BoardService boardService,
                               BoardMapper boardMapper) {
        this.boardService = boardService;
        this.boardMapper = boardMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardReadResponse> getBoardById(
            @PathVariable("id") @Min(1) Long id) {
        try {
            return ResponseEntity.ok(boardMapper.mapToDto(boardService.findById(id)));
        } catch (TrelloEntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<BoardReadResponse>> getAllBoards() {
        return ResponseEntity.ok(boardMapper.mapAll(boardService.findAll()));

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Board> createBoard(
            @Validated()
            @RequestBody BoardCreateRequest boardCreateRequest) {
        Board board = boardMapper.mapToEntity(boardCreateRequest);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(boardService.create(board));
        } catch (TrelloEntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardReadResponse> updateBoard(
            @PathVariable("id") @Min(1) Long id,
            @Validated()
            @RequestBody BoardEditRequest boardEditRequest) {
        try {
            Board board = boardMapper.mapToEntity(boardEditRequest);
            return ResponseEntity.status(HttpStatus.OK).body(boardMapper.mapToDto(boardService.update(board, id)));
        } catch (TrelloEntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable("id") @Min(1) Long id) {
        return boardService.delete(id) ? noContent().build() : notFound().build();
    }
}
