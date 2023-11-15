package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;

public interface ColumnService extends BaseService<Column> {
    Column update(Column column, Long id) throws ColumnNotFoundExceptionTrello;
}
