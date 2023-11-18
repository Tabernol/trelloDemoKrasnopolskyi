package com.krasnopolskyi.trellodemokrasnopolskyi.http;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TrelloEntityNotFoundException;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.ColumnMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnService;
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
@RequestMapping("/api/v1/columns")
@Validated()
@Slf4j
public class ColumnRestController {
    private final ColumnService columnService;
    private final ColumnMapper columnMapper;

    public ColumnRestController(ColumnService columnService,
                                ColumnMapper columnMapper) {
        this.columnService = columnService;
        this.columnMapper = columnMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColumnReadResponse> getColumnById(@PathVariable("id") @Min(1) Long id) {
        try {
            return ResponseEntity.ok(columnMapper.mapToDto(columnService.findById(id)));
        } catch (TrelloEntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ColumnReadResponse>> getAllColumns() {
        return ResponseEntity.ok(columnMapper.mapAll(columnService.findAll()));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Column> createColumn(
            @Validated()
            @RequestBody ColumnCreateRequest columnCreateRequest) {
        try {
            //check existing board
//            boardValidator.validate(columnCreateRequest.getBoardId());
            Column column = columnMapper.mapToEntity(columnCreateRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(columnService.create(column));

        } catch (TrelloEntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ColumnReadResponse> updateColumn(
            @PathVariable("id") @Min(1) Long id,
            @Validated()
            @RequestBody ColumnEditRequest columnEditRequest) {
        try {
            Column column = columnMapper.mapToEntity(columnEditRequest);
            return ResponseEntity.ok(columnMapper.mapToDto(columnService.update(column, id)));
        } catch (TrelloEntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteColumn(@PathVariable("id") @Min(1) Long id) {
        return columnService.delete(id) ? noContent().build() : notFound().build();
    }
}
