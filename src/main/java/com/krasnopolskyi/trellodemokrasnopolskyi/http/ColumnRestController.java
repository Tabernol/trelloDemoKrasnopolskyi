package com.krasnopolskyi.trellodemokrasnopolskyi.http;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.ColumnMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnService;
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

    public ColumnRestController(ColumnService columnService,
                                ColumnMapper columnMapper) {
        this.columnService = columnService;
        this.columnMapper = columnMapper;
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
        return ResponseEntity.ok(columnService.create(column));
    }


//    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Pillar> update(
//            @PathVariable("id") Long id,
//            @Validated()
//            @RequestBody PillarPostDto pillarPostDto) {
//        if (!pillarValidator.isPillarExist(taskPostDto)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }

//        Pillar pillar = pillarMapper.mapToEntity(pillarPostDto);


//        TaskReadDto updatedTaskDto = taskMapper.mapToDto(taskService.update(task, id).orElseThrow(()
//                -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
//        return ResponseEntity.ok(updatedTaskDto);
 //   }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return columnService.delete(id) ? noContent().build() : notFound().build();
    }
}
