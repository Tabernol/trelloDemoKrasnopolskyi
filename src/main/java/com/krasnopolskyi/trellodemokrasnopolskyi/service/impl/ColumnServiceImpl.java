package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ColumnServiceImpl implements ColumnService {

    private final ColumnRepository columnRepository;

    public ColumnServiceImpl(ColumnRepository columnRepository) {
        this.columnRepository = columnRepository;
    }

    @Override
    public Optional<Column> findById(Long id) {
        return columnRepository.findById(id);
    }

    @Override
    @Transactional
    public Column create(Column entity) {
        return columnRepository.save(entity);
    }

    @Override
    public List<Column> findAll() {
        return columnRepository.findAll();
    }

    @Override
    public Optional<Column> update(Column column, Long id) {
        return null;
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
