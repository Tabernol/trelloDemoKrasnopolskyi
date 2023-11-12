package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;

public interface ColumnService extends BaseService<Column> {

    Column update(Column column, Long id);
}
