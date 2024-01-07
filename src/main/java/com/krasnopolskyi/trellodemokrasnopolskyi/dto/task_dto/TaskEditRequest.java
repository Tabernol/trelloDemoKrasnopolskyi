package com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Status;
import com.krasnopolskyi.trellodemokrasnopolskyi.validator.CreateValidationGroup;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEditRequest {

    @Size(min = 2, max = 64)
    @NotBlank(groups = {CreateValidationGroup.class})
    private String name;

    @Size(max = 256)
    private String description;

    //create custom validator for this enum
    @Enumerated(EnumType.STRING)
    private Status status;
}
