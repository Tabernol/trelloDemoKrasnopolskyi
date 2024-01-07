package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_order_dto.ColumnOrderEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.ColumnOrder;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;

import java.util.List;

public interface ColumnOrderingService {

    ColumnOrder insert(Column column);

    List<Long> findAllColumnsIdByBoardIdInUserOrder(Long boardId);

    List<ColumnReadResponse> findAllColumnsByBoardInUserOrder(Long boardId);

    int reorder(ColumnOrderEditRequest columnOrderEditRequest, Long columnId) throws ColumnNotFoundExceptionTrello;
}
