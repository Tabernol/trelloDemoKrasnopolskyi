package com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto;

import com.krasnopolskyi.trellodemokrasnopolskyi.validator.CreateValidationGroup;
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
}
