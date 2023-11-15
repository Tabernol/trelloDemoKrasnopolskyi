package com.krasnopolskyi.trellodemokrasnopolskyi.http;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.TaskOrder;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.TaskMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.TaskOrderingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/columns")
@Slf4j
public class TaskOrderRestController {
    private final TaskOrderingService taskOrderingService;
    private final TaskMapper taskMapper;

    public TaskOrderRestController(
            TaskOrderingService taskOrderingService,
            TaskMapper taskMapper) {
        this.taskOrderingService = taskOrderingService;
        this.taskMapper = taskMapper;
    }

    @GetMapping("/{columnId}/tasks/order")
    public ResponseEntity<List<TaskReadDto>> getOrderedTasks(@PathVariable Long columnId) {
        List<Task> sortedTask = taskOrderingService.findAllByColumnByUserOrder(columnId);
        return ResponseEntity.ok(taskMapper.mapAll(sortedTask));
    }


    @PostMapping("/tasks/move")
    public ResponseEntity<Void> moveTask(
            @RequestBody TaskOrder taskOrder) {
        int updatedRow = taskOrderingService.moveTask(taskOrder);

        log.info("updated row = " + updatedRow);
        return ResponseEntity.ok().build();
    }


//    @PostMapping("/api/columns/{columnId}/tasks/order/move")
//    public ResponseEntity<Void> moveTaskToAnotherColumn(
//            @PathVariable Long columnId,
//            @RequestBody MoveRequest moveRequest) {
//        tasksOrderService.moveTaskToAnotherColumn(columnId, moveRequest.getTaskId(), moveRequest.getNewOrderIndex());
//        return ResponseEntity.ok().build();
//    }
}
