package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.ColumnOrder;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;

import java.util.List;

public interface ColumnOrderingService {

    ColumnOrder insert(Column column);

    List<Long> findAllColumnsIdByBoardIdInUserOrder(Long boardId);

    List<Column> findAllColumnsByBoardInUserOrder(Long boardId);

    int reorder(ColumnOrder columnOrder, Long columnId) throws ColumnNotFoundExceptionTrello;
}
