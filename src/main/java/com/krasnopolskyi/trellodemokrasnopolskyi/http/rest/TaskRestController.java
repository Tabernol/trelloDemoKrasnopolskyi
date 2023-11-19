package com.krasnopolskyi.trellodemokrasnopolskyi.http.rest;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskCreateEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TaskNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TrelloException;
import com.krasnopolskyi.trellodemokrasnopolskyi.http.handler.ErrorResponse;
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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

/**
 * REST controller class that handles task-related endpoints.
 */
@RestController
@RequestMapping("/api/v1/tasks")
@Validated
@Slf4j
public class TaskRestController {

    private final TaskService taskService;
    private final ColumnService columnService;
    private final TaskMapper taskMapper;

    /**
     * Constructs a new TaskRestController with the given dependencies.
     *
     * @param taskService  The service for managing tasks.
     * @param columnService The service for managing columns.
     * @param taskMapper   The mapper for converting task-related DTOs.
     */
    public TaskRestController(TaskService taskService,
                              ColumnService columnService,
                              TaskMapper taskMapper) {
        this.taskService = taskService;
        this.columnService = columnService;
        this.taskMapper = taskMapper;
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to retrieve.
     * @return The task response entity.
     * @throws TrelloException If the task is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskReadResponse> get(
            @PathVariable("id") @Min(1) Long id) throws TrelloException {
        return ResponseEntity.status(HttpStatus.OK).body(taskMapper.mapToDto(taskService.findById(id)));
    }

    /**
     * Retrieves all tasks.
     *
     * @return The list of task response entities.
     */
    @GetMapping
    public ResponseEntity<List<TaskReadResponse>> getAll() {
        return ResponseEntity.ok(taskMapper.mapAll(taskService.findAll()));
    }

    /**
     * Creates a new task.
     *
     * @param taskCreateEditRequest The request body containing task details.
     * @return The created task response entity.
     * @throws TrelloException If an error occurs during task creation.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Task> create(
            @Validated({Default.class, CreateValidationGroup.class})
            @RequestBody TaskCreateEditRequest taskCreateEditRequest) throws TrelloException {
        log.info("Create new task: {}", taskCreateEditRequest.toString());
        // Checking existing column
        columnService.findById(taskCreateEditRequest.getColumnId());
        Task task = taskMapper.mapToEntity(taskCreateEditRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(task));
    }

    /**
     * Updates an existing task.
     *
     * @param id                     The ID of the task to update.
     * @param taskCreateEditRequest The request body containing updated task details.
     * @return The updated task response entity.
     * @throws ColumnNotFoundExceptionTrello If the associated column is not found.
     * @throws TaskNotFoundExceptionTrello   If the task is not found.
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskReadResponse> update(
            @PathVariable("id") @Min(1) Long id,
            @Validated @RequestBody TaskCreateEditRequest taskCreateEditRequest) throws ColumnNotFoundExceptionTrello, TaskNotFoundExceptionTrello {
        Task task = taskMapper.mapToEntity(taskCreateEditRequest);
        return ResponseEntity.status(HttpStatus.OK).body(taskMapper.mapToDto(taskService.update(task, id)));
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id The ID of the task to delete.
     * @return The response entity indicating success or failure.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") @Min(1) Long id) {
        return taskService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
