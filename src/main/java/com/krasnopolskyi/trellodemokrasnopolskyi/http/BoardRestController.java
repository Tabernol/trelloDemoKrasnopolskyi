package com.krasnopolskyi.trellodemokrasnopolskyi.http;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardEditDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardReadDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TrelloEntityNotFoundException;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.BoardMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.BoardService;
import com.krasnopolskyi.trellodemokrasnopolskyi.validator.BoardValidator;
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
public class BoardRestController {
    private final BoardService boardService;
    private final BoardMapper boardMapper;
    private final BoardValidator boardValidator;

    public BoardRestController(BoardService boardService, BoardMapper boardMapper, BoardValidator boardValidator) {
        this.boardService = boardService;
        this.boardMapper = boardMapper;
        this.boardValidator = boardValidator;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardReadDto> get(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(boardMapper.mapToDto(boardService.findById(id)));
        } catch (TrelloEntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<BoardReadDto>> getAll() {
        return ResponseEntity.ok(boardMapper.mapAll(boardService.findAll()));

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Board> create(
            @Validated()
            @RequestBody BoardPostDto boardPostDto) {
        Board board = boardMapper.mapToEntity(boardPostDto);
        return ResponseEntity.ok(boardService.create(board));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardReadDto> update(
            @PathVariable("id") Long id,
            @Validated()
            @RequestBody BoardEditDto boardEditDto) {
        try {
            boardValidator.validate(id);
            Board board = boardMapper.mapToEntity(boardEditDto);
            return ResponseEntity.ok(boardMapper.mapToDto(boardService.update(board, id)));
        } catch (TrelloEntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return boardService.delete(id) ? noContent().build() : notFound().build();
    }
}
