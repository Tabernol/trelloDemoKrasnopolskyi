package com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto;

import com.krasnopolskyi.trellodemokrasnopolskyi.validator.CreateValidationGroup;
import com.krasnopolskyi.trellodemokrasnopolskyi.validator.UpdateValidationGroup;
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
public class TaskPostDto {

    @Size(min = 1, max = 64, groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    @NotBlank(groups = {CreateValidationGroup.class})
    private String name;

    @Size(max = 256, groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private String description;

    @NotBlank
    @NotBlank(groups = {CreateValidationGroup.class})
    private Long pillarId;
}
