package com.krasnopolskyi.trellodemokrasnopolskyi.http;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TrelloEntityNotFoundException;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.TaskMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.TaskService;
import com.krasnopolskyi.trellodemokrasnopolskyi.validator.CreateValidationGroup;
import com.krasnopolskyi.trellodemokrasnopolskyi.validator.ColumnValidator;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping("/api/v1/tasks")
@Validated
public class TaskRestController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final ColumnValidator columnValidator;

    public TaskRestController(TaskService taskService,
                              TaskMapper taskMapper,
                              ColumnValidator columnValidator) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
        this.columnValidator = columnValidator;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskReadDto> get(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(taskMapper.mapToDto(taskService.findById(id)));
        } catch (TrelloEntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<TaskReadDto>> getAll() {
        return ResponseEntity.ok(taskMapper.mapAll(taskService.findAll()));
    }


    @ApiResponse(responseCode = "404", description = "columnId not found")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Task> create(
            @Validated({Default.class, CreateValidationGroup.class})
            @RequestBody TaskPostDto taskPostDto) {
        try {
            columnValidator.validate(taskPostDto.getColumnId());
            Task task = taskMapper.mapToEntity(taskPostDto);
            return ResponseEntity.ok(taskService.create(task));
        } catch (TrelloEntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskReadDto> update(
            @PathVariable("id") Long id,
            @Validated()
            @RequestBody TaskPostDto taskPostDto) {
        try {
            Task task = taskMapper.mapToEntity(taskPostDto);
            return ResponseEntity.ok(taskMapper.mapToDto(taskService.update(task, id)));
        } catch (TrelloEntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return taskService.delete(id) ? noContent().build() : notFound().build();
    }
}
