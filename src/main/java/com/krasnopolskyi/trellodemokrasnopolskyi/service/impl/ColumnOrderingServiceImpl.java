package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.ColumnOrder;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.ColumnMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnOrderingRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnOrderingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    @Transactional
    public ColumnOrder insert(Column column) {
        int size = columnRepository.findAllByBoard(column.getBoard().getId()).size();
        ColumnOrder columnOrder = ColumnOrder.builder()
                .columnId(column.getId())
                .boardId(column.getBoard().getId())
                .orderIndex(size)
                .build();
        return columnOrderingRepository.saveAndFlush(columnOrder);
    }

    @Override
    public List<Long> findAllColumnsIdByBoardIdInUserOrder(Long boardId) {
        return columnOrderingRepository.findAllByBoardIdOrderByOrderIndex(boardId)
                .stream()
                .map(ColumnOrder::getColumnId)
                .collect(Collectors.toList());
    }

    public List<Column> findAllColumnsByBoardInUserOrder(Long boardId) {
        List<Column> nonSortedColumns = columnRepository.findAllByBoard(boardId);

        Map<Long, Column> columns = nonSortedColumns.stream()
                .collect(Collectors.toMap(Column::getId, Function.identity()));

        List<Long> userOrder = findAllColumnsIdByBoardIdInUserOrder(boardId);

        return userOrder
                .stream()
                .map(columns::get)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int reorder(ColumnOrder columnOrder) throws ColumnNotFoundExceptionTrello {
        Long columnId = columnOrder.getColumnId();
        Column column = columnRepository.findById(columnId).orElseThrow(
                () -> new ColumnNotFoundExceptionTrello("Column with id " + columnId + " not found"));
        Long boardId = column.getBoard().getId();

        List<ColumnOrder> columnOrderList =
                new LinkedList<>(columnOrderingRepository
                        .findAllByBoardIdOrderByOrderIndex(boardId));

        columnOrder.setBoardId(boardId);

        columnOrderList.remove(columnOrder);

        checkIndex(columnOrderList, columnOrder);

        columnOrderList.add(columnOrder.getOrderIndex() - 1, columnOrder);

        return updateOrderIndexesAndSave(columnOrderList);
    }


    private void checkIndex(List<ColumnOrder> columnOrderList, ColumnOrder columnOrder) {
        if (columnOrder.getOrderIndex() > columnOrderList.size()) {
            columnOrder.setOrderIndex(columnOrderList.size() + 1);
        }
    }

    private int updateOrderIndexesAndSave(List<ColumnOrder> columnOrderList) {
        for (int i = 0; i < columnOrderList.size(); i++) {
            columnOrderList.get(i).setOrderIndex(i + 1);
        }
        columnOrderList = columnOrderingRepository.saveAllAndFlush(columnOrderList);
        return columnOrderList.size();
    }
}
