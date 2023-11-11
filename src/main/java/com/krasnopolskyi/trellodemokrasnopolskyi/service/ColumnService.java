package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;

import java.util.Optional;

public interface ColumnService extends BaseService<Column> {

    Optional<Column> update(Column column, Long id);
}
