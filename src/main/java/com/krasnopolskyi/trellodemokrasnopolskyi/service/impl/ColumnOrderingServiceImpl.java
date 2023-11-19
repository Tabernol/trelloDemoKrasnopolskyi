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

/**
 * Service class that provides business logic for managing the ordering of columns.
 */
@Service
public class ColumnOrderingServiceImpl implements ColumnOrderingService {

    private final ColumnOrderingRepository columnOrderingRepository;
    private final ColumnRepository columnRepository;

    /**
     * Constructs a new ColumnOrderingServiceImpl with the given dependencies.
     *
     * @param columnOrderingRepository The repository for column ordering entities.
     * @param columnRepository         The repository for column entities.
     */
    public ColumnOrderingServiceImpl(ColumnOrderingRepository columnOrderingRepository,
                                     ColumnRepository columnRepository) {
        this.columnOrderingRepository = columnOrderingRepository;
        this.columnRepository = columnRepository;
    }

    /**
     * Inserts a column order for the specified column.
     *
     * @param column The column for which to insert the order.
     * @return The created column order entity.
     */
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

    /**
     * Retrieves all column IDs for a board in the user-defined order.
     *
     * @param boardId The ID of the board.
     * @return A list of column IDs in the user-defined order for the specified board.
     */
    @Override
    public List<Long> findAllColumnsIdByBoardIdInUserOrder(Long boardId) {
        return columnOrderingRepository.findAllByBoardIdOrderByOrderIndex(boardId)
                .stream()
                .map(ColumnOrder::getColumnId)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all columns for a board in the user-defined order.
     *
     * @param boardId The ID of the board.
     * @return A list of columns in the user-defined order for the specified board.
     */
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

    /**
     * Reorders a column based on the provided column order.
     *
     * @param columnOrder The column order information.
     * @return The total number of columns affected.
     * @throws ColumnNotFoundExceptionTrello If the associated column is not found.
     */
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

    //this method checks index which was passed.
    //if it is more than size of last index that index will set as last
    private void checkIndex(List<ColumnOrder> columnOrderList, ColumnOrder columnOrder) {
        if (columnOrder.getOrderIndex() > columnOrderList.size()) {
            columnOrder.setOrderIndex(columnOrderList.size() + 1);
        }
    }

    //this is important method which set new order indexes after insert column to list
    private int updateOrderIndexesAndSave(List<ColumnOrder> columnOrderList) {
        for (int i = 0; i < columnOrderList.size(); i++) {
            columnOrderList.get(i).setOrderIndex(i + 1);
        }
        columnOrderList = columnOrderingRepository.saveAllAndFlush(columnOrderList);
        return columnOrderList.size();
    }
}
