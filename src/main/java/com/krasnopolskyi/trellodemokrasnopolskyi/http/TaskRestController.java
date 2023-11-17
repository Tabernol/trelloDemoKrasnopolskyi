package com.krasnopolskyi.trellodemokrasnopolskyi.http;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskCreateEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TrelloEntityNotFoundException;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.TaskMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnService;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.TaskService;
import com.krasnopolskyi.trellodemokrasnopolskyi.validator.CreateValidationGroup;
import jakarta.validation.constraints.Min;
import jakarta.validation.groups.Default;
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
@RequestMapping("/api/v1/tasks")
@Validated
@Slf4j
public class TaskRestController {
    private final TaskService taskService;

    private final ColumnService columnService;
    private final TaskMapper taskMapper;

    public TaskRestController(TaskService taskService,
                              ColumnService columnService,
                              TaskMapper taskMapper) {
        this.taskService = taskService;
        this.columnService = columnService;
        this.taskMapper = taskMapper;

    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskReadResponse> get(
            @PathVariable("id") @Min(1) Long id) {
        try {
            return ResponseEntity.ok(taskMapper.mapToDto(taskService.findById(id)));
        } catch (TrelloEntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<TaskReadResponse>> getAll() {
        return ResponseEntity.ok(taskMapper.mapAll(taskService.findAll()));
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Task> create(
            @Validated({Default.class, CreateValidationGroup.class})
            @RequestBody TaskCreateEditRequest taskCreateEditRequest) {
        log.info("Create new task : " + taskCreateEditRequest.toString());
        try {
            //Checking existing column
            columnService.findById(taskCreateEditRequest.getColumnId());

            Task task = taskMapper.mapToEntity(taskCreateEditRequest);
            return ResponseEntity.ok(taskService.create(task));
        } catch (TrelloEntityNotFoundException e) {
            log.warn("Something went wrong Task.create " + e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskReadResponse> update(
            @PathVariable("id") @Min(1) Long id,
            @Validated()
            @RequestBody TaskCreateEditRequest taskCreateEditRequest) {
        try {
            Task task = taskMapper.mapToEntity(taskCreateEditRequest);
            return ResponseEntity.ok(taskMapper.mapToDto(taskService.update(task, id)));
        } catch (TrelloEntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") @Min(1) Long id) {
        return taskService.delete(id) ? noContent().build() : notFound().build();
    }
}
