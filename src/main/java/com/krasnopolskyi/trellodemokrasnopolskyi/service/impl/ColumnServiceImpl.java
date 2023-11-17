package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.BoardNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnOrderingService;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnService;
import com.krasnopolskyi.trellodemokrasnopolskyi.validator.BoardValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ColumnServiceImpl implements ColumnService {
    private final BoardValidator boardValidator;
    private final ColumnRepository columnRepository;
    private final ColumnOrderingService columnOrderingService;

    public ColumnServiceImpl(
            BoardValidator boardValidator,
            ColumnRepository columnRepository,
            ColumnOrderingService columnOrderingService) {
        this.boardValidator = boardValidator;
        this.columnRepository = columnRepository;
        this.columnOrderingService = columnOrderingService;
    }

    @Override
    public Column findById(Long id) throws ColumnNotFoundExceptionTrello {
        return columnRepository.findById(id)
                .orElseThrow(()
                        -> new ColumnNotFoundExceptionTrello("Column with id " + id + " not found"));
    }

    @Override
    public List<Column> findAll() {
        return columnRepository.findAll();
    }

    @Override
    @Transactional
    public Column create(Column column) throws BoardNotFoundExceptionTrello {
        //checking existing board
        boardValidator.validate(column.getBoard().getId());
        column = columnRepository.saveAndFlush(column);
        // insert to columns_ordering table
        columnOrderingService.insert(column);
        return column;
    }


    @Override
    @Transactional
    public Column update(Column column, Long id) throws ColumnNotFoundExceptionTrello {
        Column existingColumn = findById(id);
        existingColumn.setName(column.getName());
        return columnRepository.save(existingColumn);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return columnRepository.findById(id)
                .map(entity -> {
                    columnRepository.delete(entity);
                    columnRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
