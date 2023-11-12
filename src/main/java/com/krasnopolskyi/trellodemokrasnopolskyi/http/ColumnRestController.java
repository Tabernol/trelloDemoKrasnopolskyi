package com.krasnopolskyi.trellodemokrasnopolskyi.http;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnEditDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnReadDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TrelloEntityNotFoundException;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.ColumnMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnService;
import com.krasnopolskyi.trellodemokrasnopolskyi.validator.BoardValidator;
import com.krasnopolskyi.trellodemokrasnopolskyi.validator.CreateValidationGroup;
import jakarta.validation.groups.Default;
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
@RequestMapping("/api/v1/columns")
public class ColumnRestController {
    private final ColumnService columnService;
    private final ColumnMapper columnMapper;
    private final BoardValidator boardValidator;

    public ColumnRestController(ColumnService columnService,
                                ColumnMapper columnMapper,
                                BoardValidator boardValidator) {
        this.columnService = columnService;
        this.columnMapper = columnMapper;
        this.boardValidator = boardValidator;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColumnReadDto> get(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(columnMapper.mapToDto(columnService.findById(id)));
        } catch (TrelloEntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ColumnReadDto>> getAll() {
        return ResponseEntity.ok(columnMapper.mapAll(columnService.findAll()));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Column> create(
            @Validated({Default.class, CreateValidationGroup.class})
            @RequestBody ColumnPostDto columnPostDto) {
        try{
            //check existing board
            boardValidator.validate(columnPostDto.getBoardId());
            Column column = columnMapper.mapToEntity(columnPostDto);
            return ResponseEntity.ok(columnService.create(column));

        } catch (TrelloEntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ColumnReadDto> update(
            @PathVariable("id") Long id,
            @Validated()
            @RequestBody ColumnEditDto columnEditDto) {
        try {
            Column column = columnMapper.mapToEntity(columnEditDto);
            return ResponseEntity.ok(columnMapper.mapToDto(columnService.update(column, id)));
        } catch (TrelloEntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return columnService.delete(id) ? noContent().build() : notFound().build();
    }
}
