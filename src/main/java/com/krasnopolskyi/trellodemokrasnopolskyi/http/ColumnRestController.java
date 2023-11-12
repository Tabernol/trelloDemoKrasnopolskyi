package com.krasnopolskyi.trellodemokrasnopolskyi.http;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.ColumnMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnService;
import com.krasnopolskyi.trellodemokrasnopolskyi.validator.ColumnValidator;
import com.krasnopolskyi.trellodemokrasnopolskyi.validator.CreateValidationGroup;
import jakarta.validation.groups.Default;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequestMapping("/api/v1/columns")
public class ColumnRestController {
    private final ColumnService columnService;
    private final ColumnMapper columnMapper;
    private final ColumnValidator columnValidator;

    public ColumnRestController(ColumnService columnService,
                                ColumnMapper columnMapper, ColumnValidator columnValidator) {
        this.columnService = columnService;
        this.columnMapper = columnMapper;
        this.columnValidator = columnValidator;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Column> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(columnService.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @GetMapping
    public ResponseEntity<List<Column>> getAll() {
        return ResponseEntity.ok(columnService.findAll());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Column> create(
            @Validated({Default.class, CreateValidationGroup.class})
            @RequestBody ColumnPostDto columnPostDto) {
        Column column = columnMapper.mapToEntity(columnPostDto);


        //chech exisiting board
        return ResponseEntity.ok(columnService.create(column));
    }


    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Column> update(
            @PathVariable("id") Long id,
            @Validated()
            @RequestBody ColumnPostDto columnPostDto) {
        if (!columnValidator.isColumnExist(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Column column = columnMapper.mapToEntity(columnPostDto);
        return ResponseEntity.ok(columnService.update(column, id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return columnService.delete(id) ? noContent().build() : notFound().build();
    }
}
