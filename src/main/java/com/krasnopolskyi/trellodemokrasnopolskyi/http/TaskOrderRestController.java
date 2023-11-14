package com.krasnopolskyi.trellodemokrasnopolskyi.http;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.TaskMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/columns")
public class TaskOrderRestController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    public TaskOrderRestController(TaskService taskService,
                                   TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping("/{columnId}/tasks/order")
    public ResponseEntity<List<TaskReadDto>> getOrderedTasks(@PathVariable Long columnId) {
        List<Task> sortedTask = taskService.findAllByColumnByUserOrder(columnId);
        return ResponseEntity.ok(taskMapper.mapAll(sortedTask));
    }
}
