package com.krasnopolskyi.trellodemokrasnopolskyi.http;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.read.TaskReadDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.post.TaskPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Pillar;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.TaskMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.PillarService;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.TaskService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/tasks")
@Validated
public class TaskRestController {

    private final TaskService taskService;

    private final PillarService pillarService;
    private final TaskMapper taskMapper;

    public TaskRestController(TaskService taskService,
                              PillarService pillarService,
                              TaskMapper taskMapper) {
        this.taskService = taskService;
        this.pillarService = pillarService;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TaskReadDto create(@Valid @RequestBody TaskPostDto taskPostDto) {
        //Check if id column is not valid
//        Column maybeColumn = columnService.findById(taskPostDto.getColumnId()).orElseThrow(()
//                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Column not found"));

        Task task = taskMapper.mapToEntity(taskPostDto);
        return taskMapper.mapToDto(taskService.create(task));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskReadDto> update(@PathVariable("id") Long id, @Valid @RequestBody TaskPostDto taskPostDto) {
        Optional<Task> optionalTask = taskService.findById(id);
        if (optionalTask.isPresent()) {
            Task excitingTask = optionalTask.get();

            if (taskPostDto.getName() != null) {
                excitingTask.setName(taskPostDto.getName());
            }

            if (taskPostDto.getDescription() != null) {
                excitingTask.setDescription(taskPostDto.getDescription());
            }

            if (taskPostDto.getPillarId() != null) {
                Pillar pillar = pillarService.findById(taskPostDto.getPillarId()).orElseThrow(()
                        -> new ResponseStatusException(HttpStatus.NOT_FOUND));
                excitingTask.setPillar(pillar);
            }

            taskService.update(excitingTask, id);

            TaskReadDto taskReadDto = taskMapper.mapToDto(taskService.findById(id).get());
            return ResponseEntity.ok(taskReadDto);
        } else {
            return ResponseEntity.notFound().build();
        }

//        Task task = taskMapper.mapToEntity(taskPostDto);
//        return taskMapper.mapToDto(taskService.update(task, id).orElseThrow(()
//                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found")));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        return taskService.delete(id) ? noContent().build() : notFound().build();
    }

}
