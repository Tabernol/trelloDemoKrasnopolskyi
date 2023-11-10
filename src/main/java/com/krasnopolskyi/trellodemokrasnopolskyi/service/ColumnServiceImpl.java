package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnRepository;
import org.springframework.stereotype.Service;

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
    public Column create(Column entity) {
        return null;
    }

    @Override
    public List<Column> findAll() {
        return null;
    }

    @Override
    public Optional<Column> update(Column entity, Long id) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
