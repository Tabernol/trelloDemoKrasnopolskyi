package com.krasnopolskyi.trellodemokrasnopolskyi.http.rest;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_order_dto.ColumnOrderEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.ColumnOrder;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TrelloException;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.ColumnMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnOrderingService;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards")
@Validated()
@Slf4j
public class ColumnOrderRestController {
    private final ColumnOrderingService columnOrderingService;
    private final ColumnMapper columnMapper;

    public static final String ORDER_SUCCESSFUL = "Column ordered successfully";
    public static final String ORDER_FAILED = "Column ordered failed";
    public static final String MISMATCHED_IDS = "Mismatched columns IDs";

    public ColumnOrderRestController(ColumnOrderingService columnOrderingService,
                                     ColumnMapper columnMapper) {
        this.columnOrderingService = columnOrderingService;
        this.columnMapper = columnMapper;
    }

    @GetMapping("/{boardId}/columns")
    public ResponseEntity<List<ColumnReadResponse>> getOrderedColumn(
            @PathVariable("boardId") Long boardId) {
        List<Column> sortedColumns = columnOrderingService.findAllColumnsByBoardInUserOrder(boardId);
        return ResponseEntity.ok(columnMapper.mapAll(sortedColumns));
    }

    @PutMapping("/columns/{columnId}/reorder")
    public ResponseEntity<String> reOrder(
            @PathVariable("columnId") @Min(1) Long columnId,
            @Validated()
            @RequestBody ColumnOrderEditRequest columnOrderEditRequest) throws ColumnNotFoundExceptionTrello {

        if (!columnId.equals(columnOrderEditRequest.getColumnId())) {
            return ResponseEntity.badRequest().body(MISMATCHED_IDS);
        }

        int updatedRow = columnOrderingService.reorder(ColumnOrder
                .builder()
                .columnId(columnOrderEditRequest.getColumnId())
                .orderIndex(columnOrderEditRequest.getOrderIndex())
                .build());

        return updatedRow > 0 ?
                ResponseEntity.ok(ORDER_SUCCESSFUL) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ORDER_FAILED);

    }
}
