package com.krasnopolskyi.trellodemokrasnopolskyi.validator;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.PillarRepository;
import org.springframework.stereotype.Component;

@Component
public class PillarValidator {
    private final PillarRepository pillarRepository;

    public PillarValidator(PillarRepository pillarRepository) {
        this.pillarRepository = pillarRepository;
    }


    public boolean isPillarExist(TaskPostDto taskPostDto) {
        if (taskPostDto.getPillarId() == null) return true;
        return pillarRepository.existsById(taskPostDto.getPillarId());
    }
}
