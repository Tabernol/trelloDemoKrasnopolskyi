package com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto;

import com.krasnopolskyi.trellodemokrasnopolskyi.validator.CreateValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreateEditRequest {

    @Size(min = 2, max = 64)
    @NotBlank(groups = {CreateValidationGroup.class})
    private String name;

    @Size(max = 256)
    private String description;


    @NotNull(groups = {CreateValidationGroup.class})
    private Long columnId;
}
