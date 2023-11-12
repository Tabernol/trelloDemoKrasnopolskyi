package com.krasnopolskyi.trellodemokrasnopolskyi.validator;

import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnRepository;
import org.springframework.stereotype.Component;

@Component
public class ColumnValidator {
    private final ColumnRepository columnRepository;

    public ColumnValidator(ColumnRepository columnRepository) {
        this.columnRepository = columnRepository;
    }


    public void validate(Long id) {
        columnRepository.findById(id)
                .orElseThrow(() -> new ColumnNotFoundExceptionTrello("Column with id " + id + " not found"));
    }
}
