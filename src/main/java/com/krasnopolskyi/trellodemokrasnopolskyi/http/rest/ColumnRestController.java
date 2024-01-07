package com.krasnopolskyi.trellodemokrasnopolskyi.http.rest;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TrelloException;
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

/**
 * REST controller class that handles column-related endpoints.
 * @author Maksym Krasnopolskyi
 */
@RestController
@RequestMapping("/api/v1/columns")
@Validated
@Slf4j
public class ColumnRestController {
    private final ColumnService columnService;

    /**
     * Constructs a new ColumnRestController with the given dependencies.
     *
     * @param columnService The service for managing columns.
     */
    public ColumnRestController(ColumnService columnService) {
        this.columnService = columnService;
    }

    /**
     * Retrieves a column by its ID.
     *
     * @param id The ID of the column to retrieve.
     * @return The column response entity.
     * @throws TrelloException If the column is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ColumnReadResponse> getColumnById(
            @PathVariable("id") @Min(1) Long id) throws TrelloException {
        return ResponseEntity.ok(columnService.findById(id));
    }

    /**
     * Retrieves all columns.
     *
     * @return The list of column response entities.
     */
    @GetMapping
    public ResponseEntity<List<ColumnReadResponse>> getAllColumns() {
        return ResponseEntity.ok(columnMapper.mapAll(columnService.findAll()));
    }

    /**
     * Creates a new column.
     *
     * @param columnCreateRequest The request body containing column details.
     * @return The created column response entity.
     * @throws TrelloException If an error occurs during column creation.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Column> createColumn(
            @Validated @RequestBody ColumnCreateRequest columnCreateRequest) throws TrelloException {
        Column column = columnMapper.mapToEntity(columnCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(columnService.create(column));
    }

    /**
     * Updates an existing column.
     *
     * @param id                The ID of the column to update.
     * @param columnEditRequest The request body containing updated column details.
     * @return The updated column response entity.
     * @throws ColumnNotFoundExceptionTrello If the column is not found.
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ColumnReadResponse> updateColumn(
            @PathVariable("id") @Min(1) Long id,
            @Validated @RequestBody ColumnEditRequest columnEditRequest) throws ColumnNotFoundExceptionTrello {
        Column column = columnMapper.mapToEntity(columnEditRequest);
        return ResponseEntity.ok(columnMapper.mapToDto(columnService.update(column, id)));
    }

    /**
     * Deletes a column by its ID.
     *
     * @param id The ID of the column to delete.
     * @return The response entity indicating success or failure.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteColumn(@PathVariable("id") @Min(1) Long id) {
        return columnService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
