package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.ColumnOrder;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnOrderingRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnOrderingService;
import org.springframework.stereotype.Service;

@Service
public class ColumnOrderingServiceImpl implements ColumnOrderingService {

    private final ColumnOrderingRepository columnOrderingRepository;

    private final ColumnRepository columnRepository;

    public ColumnOrderingServiceImpl(ColumnOrderingRepository columnOrderingRepository,
                                     ColumnRepository columnRepository) {
        this.columnOrderingRepository = columnOrderingRepository;
        this.columnRepository = columnRepository;
    }

    @Override
    public ColumnOrder insert(Column column) {
        int size = columnRepository.findAllByBoard(column.getBoard().getId()).size();
        ColumnOrder columnOrder = ColumnOrder.builder()
                .columnId(column.getId())
                .boardId(column.getBoard().getId())
                .orderIndex(size)
                .build();
        return columnOrderingRepository.saveAndFlush(columnOrder);
    }
}
