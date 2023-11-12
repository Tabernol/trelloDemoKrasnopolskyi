package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ColumnServiceImpl implements ColumnService {

    private final ColumnRepository columnRepository;

    public ColumnServiceImpl(ColumnRepository columnRepository) {
        this.columnRepository = columnRepository;
    }

    @Override
    public Column findById(Long id) {
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
    public Column create(Column entity) {
        return columnRepository.save(entity);
    }


    @Override
    @Transactional
    public Column update(Column column, Long id) {
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
