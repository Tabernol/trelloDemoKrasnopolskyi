package com.krasnopolskyi.trellodemokrasnopolskyi.http.rest;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_order_dto.TaskOrderEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.TaskOrder;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TrelloException;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.TaskMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.TaskOrderingService;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/columns")
@Validated()
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
    public ResponseEntity<List<TaskReadResponse>> getOrderedTasks(
            @PathVariable @Min(1) Long columnId) {
        List<Task> sortedTask = taskOrderingService.findAllByColumnByUserOrder(columnId);
        return ResponseEntity.ok(taskMapper.mapAll(sortedTask));
    }


    @PutMapping("/tasks/{taskId}/move")
    public ResponseEntity<String> moveTask(
            @PathVariable("taskId") @Min(1) Long taskId,
            @Validated()
            @RequestBody TaskOrderEditRequest taskOrderEditRequest) {

        //maybe do it with AOP
        if (!taskId.equals(taskOrderEditRequest.getTaskId())) {
            return ResponseEntity.badRequest().body("Mismatched task IDs");
        }

        TaskOrder taskOrder = TaskOrder.builder()
                .taskId(taskOrderEditRequest.getTaskId())
                .columnId(taskOrderEditRequest.getColumnId())
                .orderIndex(taskOrderEditRequest.getNewOrderIndex())
                .build();


        int updatedRow = 0;
        try {
            updatedRow = taskOrderingService.moveTask(taskOrder);
        } catch (TrelloException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }


        log.info("updated row = " + updatedRow);
        return updatedRow > 0 ?
                ResponseEntity.ok("Task moved successfully") :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Task move failed");
    }
}
