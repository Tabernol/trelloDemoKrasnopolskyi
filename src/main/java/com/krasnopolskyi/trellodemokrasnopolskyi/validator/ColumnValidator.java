package com.krasnopolskyi.trellodemokrasnopolskyi.validator;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnRepository;
import org.springframework.stereotype.Component;

@Component
public class ColumnValidator {
    private final ColumnRepository columnRepository;

    public ColumnValidator(ColumnRepository columnRepository) {
        this.columnRepository = columnRepository;
    }


    public boolean isColumnExist(Long id) {
        return columnRepository.existsById(id);
    }
}
