package com.krasnopolskyi.trellodemokrasnopolskyi.http;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.read.TaskReadDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.post.TaskPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.TaskMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnService;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public TaskReadDto getTask(@PathVariable("id") Long id) {
        return taskMapper.mapToDto(taskService.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @GetMapping
    public List<TaskReadDto> getAll() {
        return taskMapper.mapAll(taskService.findAll());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskReadDto create(@Valid @RequestBody TaskPostDto taskPostDto) {
        //Check if id column is not valid
//        Column maybeColumn = columnService.findById(taskPostDto.getColumnId()).orElseThrow(()
//                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Column not found"));

        Task task = taskMapper.mapToEntity(taskPostDto);
        return taskMapper.mapToDto(taskService.create(task));
    }

    @PatchMapping("/{id}")
    public TaskReadDto update(@PathVariable("id") Long id, @Valid @RequestBody TaskPostDto taskPostDto) {
        Task task = taskMapper.mapToEntityWithNullFields(taskPostDto);
        return taskMapper.mapToDto(taskService.update(task, id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Column not found")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return taskService.delete(id) ? noContent().build() : notFound().build();
    }

}
