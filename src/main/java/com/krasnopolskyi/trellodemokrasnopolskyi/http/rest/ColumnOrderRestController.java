package com.krasnopolskyi.trellodemokrasnopolskyi.http.rest;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_order_dto.ColumnOrderEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.ColumnOrder;
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
            @RequestBody ColumnOrderEditRequest columnOrderEditRequest) {

        if (!columnId.equals(columnOrderEditRequest.getColumnId())) {
            return ResponseEntity.badRequest().body("Mismatched columns IDs");
        }
        int updatedRow = 0;

        try {
            updatedRow = columnOrderingService.reorder(ColumnOrder
                    .builder()
                    .columnId(columnOrderEditRequest.getColumnId())
                    .orderIndex(columnOrderEditRequest.getOrderIndex())
                    .build());
        } catch (TrelloException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

        return updatedRow > 0 ?
                ResponseEntity.ok("Column ordered successfully") :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Column ordered failed");

    }
}
