package com.krasnopolskyi.trellodemokrasnopolskyi.http.rest;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_order_dto.TaskOrderEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.TaskOrder;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ProhibitionMovingException;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TaskNotFoundExceptionTrello;
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

/**
 * REST controller class that handles task ordering-related endpoints.
 * @author Maksym Krasnopolskyi
 */
@RestController
@RequestMapping("/api/v1/columns")
@Validated()
@Slf4j
public class TaskOrderRestController {

    private final TaskOrderingService taskOrderingService;
    private final TaskMapper taskMapper;

    public static final String ORDER_SUCCESSFUL = "Task moved successfully";
    public static final String ORDER_FAILED = "Task move failed";
    public static final String MISMATCHED_IDS = "Mismatched IDs";

    /**
     * Constructs a new TaskOrderRestController with the given dependencies.
     *
     * @param taskOrderingService The service for managing task ordering.
     * @param taskMapper          The mapper for converting task-related DTOs.
     */
    public TaskOrderRestController(
            TaskOrderingService taskOrderingService,
            TaskMapper taskMapper) {
        this.taskOrderingService = taskOrderingService;
        this.taskMapper = taskMapper;
    }

    /**
     * Retrieves the list of ordered tasks for a given column.
     *
     * @param columnId The ID of the column for which to retrieve ordered tasks.
     * @return The response entity containing the list of ordered tasks.
     */
    @GetMapping("/{columnId}/tasks/order")
    public ResponseEntity<List<TaskReadResponse>> getOrderedTasks(
            @PathVariable @Min(1) Long columnId) {
        List<Task> sortedTask = taskOrderingService.findAllByColumnByUserOrder(columnId);
        return ResponseEntity.ok(taskMapper.mapAll(sortedTask));
    }

    /**
     * Moves a task to a new order index within its column
     * or in another column in the same board.
     *
     * @param taskId                The ID of the task to move.
     * @param taskOrderEditRequest The request body containing the details of the task move.
     * @return The response entity indicating the success or failure of the task move.
     * @throws ColumnNotFoundExceptionTrello If the associated column is not found.
     * @throws TaskNotFoundExceptionTrello   If the task is not found.
     */
    @PutMapping("/tasks/{taskId}/move")
    public ResponseEntity<String> moveTask(
            @PathVariable("taskId") @Min(1) Long taskId,
            @Validated @RequestBody TaskOrderEditRequest taskOrderEditRequest)
            throws ColumnNotFoundExceptionTrello, TaskNotFoundExceptionTrello, ProhibitionMovingException {

        // maybe do it with AOP
        if (!taskId.equals(taskOrderEditRequest.getTaskId())) {
            return ResponseEntity.badRequest().body(MISMATCHED_IDS);
        }

        TaskOrder taskOrder = TaskOrder.builder()
                .taskId(taskOrderEditRequest.getTaskId())
                .columnId(taskOrderEditRequest.getColumnId())
                .orderIndex(taskOrderEditRequest.getNewOrderIndex())
                .build();

        int updatedRow = taskOrderingService.moveTask(taskOrder);

        return updatedRow > 0 ?
                ResponseEntity.ok(ORDER_SUCCESSFUL) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ORDER_FAILED);
    }
}
