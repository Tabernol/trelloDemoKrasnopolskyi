package com.krasnopolskyi.trellodemokrasnopolskyi.http.rest;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_order_dto.ColumnOrderEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.ColumnOrder;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.ColumnMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnOrderingService;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest controller for handling column ordering operations in Trello-like boards.
 *
 * @author Maksym Krasnopolskyi
 */
@RestController
@RequestMapping("/api/v1/boards")
@Validated()
@Slf4j
public class ColumnOrderRestController {
    private final ColumnOrderingService columnOrderingService;

    public static final String ORDER_SUCCESSFUL = "Column ordered successfully";

    public static final String ORDER_FAILED = "Column ordered failed";

    /**
     * Constructs a new {@code ColumnOrderRestController} with the specified services.
     *
     * @param columnOrderingService Service for handling column ordering operations.
     */
    public ColumnOrderRestController(ColumnOrderingService columnOrderingService) {
        this.columnOrderingService = columnOrderingService;
    }

    /**
     * Retrieves ordered columns for a specific board.
     *
     * @param boardId ID of the board for which columns are retrieved.
     * @return ResponseEntity containing a list of ordered column DTOs.
     */
    @GetMapping("/{boardId}/columns")
    public ResponseEntity<List<ColumnReadResponse>> getOrderedColumn(
            @PathVariable("boardId") Long boardId) {
        List<ColumnReadResponse> sortedColumns = columnOrderingService.findAllColumnsByBoardInUserOrder(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(sortedColumns);
    }

    /**
     * Reorders a column within a board based on the provided request.
     *
     * @param columnId               ID of the column to be reordered.
     * @param columnOrderEditRequest Request containing the new order index for the column.
     * @return ResponseEntity indicating the success or failure of the reorder operation.
     * @throws ColumnNotFoundExceptionTrello if the specified column is not found.
     */
    @PutMapping("/columns/{columnId}/reorder")
    public ResponseEntity<String> reOrder(
            @PathVariable("columnId") @Min(1) Long columnId,
            @Validated() @RequestBody ColumnOrderEditRequest columnOrderEditRequest)
            throws ColumnNotFoundExceptionTrello {

        // Perform the reorder operation and handle the response
        int updatedRow = columnOrderingService.reorder(columnOrderEditRequest, columnId);

        return updatedRow > 0 ?
                ResponseEntity.ok(ORDER_SUCCESSFUL) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ORDER_FAILED);
    }
}
