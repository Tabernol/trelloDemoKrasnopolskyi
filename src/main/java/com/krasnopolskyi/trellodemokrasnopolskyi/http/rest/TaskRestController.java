package com.krasnopolskyi.trellodemokrasnopolskyi.http.rest;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.IncorrectStatusChangeExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TaskNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TrelloException;
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

import java.util.List;

/**
 * REST controller class that handles task-related endpoints.
 *
 * @author Maksym Krasnopolskyi
 */
@RestController
@RequestMapping("/api/v1/tasks")
@Validated
@Slf4j
public class TaskRestController {
    private final TaskService taskService;

    /**
     * Constructs a new TaskRestController with the given dependencies.
     *
     * @param taskService The service for managing tasks.
     */
    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to retrieve.
     * @return The task response entity.
     * @throws TrelloException If the task is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskReadResponse> getTaskById(
            @PathVariable("id") @Min(1) Long id) throws TrelloException {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.findById(id));
    }


    @GetMapping("/status/{id}")
    public ResponseEntity<String> getNewStatusTask(@PathVariable("id") Long id)
            throws TrelloException {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.findStatusById(id));
    }

    /**
     * Retrieves all tasks.
     *
     * @return The list of task response entities.
     */
    @GetMapping
    public ResponseEntity<List<TaskReadResponse>> getAllTasks() {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.findAll());
    }

    /**
     * Creates a new task.
     *
     * @param taskCreateRequest The request body containing task details.
     * @return The created task response entity.
     * @throws TrelloException If an error occurs during task creation.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskReadResponse> createTask(
            @Validated({Default.class, CreateValidationGroup.class})
            @RequestBody TaskCreateRequest taskCreateRequest) throws TrelloException {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(taskCreateRequest));
    }

    /**
     * Updates an existing task.
     *
     * @param id              The ID of the task to update.
     * @param taskEditRequest The request body containing updated task details.
     * @return The updated task response entity.
     * @throws ColumnNotFoundExceptionTrello If the associated column is not found.
     * @throws TaskNotFoundExceptionTrello   If the task is not found.
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskReadResponse> updateTask(
            @PathVariable("id") @Min(1) Long id,
            @Validated @RequestBody TaskEditRequest taskEditRequest)
            throws ColumnNotFoundExceptionTrello, TaskNotFoundExceptionTrello, IncorrectStatusChangeExceptionTrello {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.update(taskEditRequest, id));
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id The ID of the task to delete.
     * @return The response entity indicating success or failure.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") @Min(1) Long id) {
        return taskService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
