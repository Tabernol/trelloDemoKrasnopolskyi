package com.krasnopolskyi.trellodemokrasnopolskyi.http;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.TaskMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.TaskService;
import com.krasnopolskyi.trellodemokrasnopolskyi.validator.CreateValidationGroup;
import com.krasnopolskyi.trellodemokrasnopolskyi.validator.PillarValidator;
import com.krasnopolskyi.trellodemokrasnopolskyi.validator.UpdateValidationGroup;
import jakarta.validation.Valid;
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
    private final PillarValidator pillarValidator;

    public TaskRestController(TaskService taskService,
                              TaskMapper taskMapper,
                              PillarValidator pillarValidator) {
        this.taskService = taskService;
        ;
        this.taskMapper = taskMapper;
        this.pillarValidator = pillarValidator;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskReadDto> getTask(@PathVariable("id") Long id) {
        TaskReadDto taskReadDto = taskMapper.mapToDto(taskService.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return ResponseEntity.ok(taskReadDto);
    }

    @GetMapping
    public ResponseEntity<List<TaskReadDto>> getAll() {
        List<TaskReadDto> tasks = taskMapper.mapAll(taskService.findAll());
        return ResponseEntity.ok(tasks);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskReadDto> create(
            @Validated(CreateValidationGroup.class)
            @RequestBody TaskPostDto taskPostDto) {

        if (!pillarValidator.isPillarExist(taskPostDto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Task task = taskMapper.mapToEntity(taskPostDto);
        return ResponseEntity.ok(taskMapper.mapToDto(taskService.create(task)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskReadDto> update(
            @PathVariable("id") Long id,
            @Validated(UpdateValidationGroup.class) @RequestBody TaskPostDto taskPostDto) {
        if (!pillarValidator.isPillarExist(taskPostDto)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Task task = taskMapper.mapToEntity(taskPostDto);
        TaskReadDto updatedTaskDto = taskMapper.mapToDto(taskService.update(task, id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return ResponseEntity.ok(updatedTaskDto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return taskService.delete(id) ? noContent().build() : notFound().build();
    }
}
