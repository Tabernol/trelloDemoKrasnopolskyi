package com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto;

import com.krasnopolskyi.trellodemokrasnopolskyi.validator.CreateValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColumnPostDto {
    @Size(min = 2, max = 64)
    @NotBlank(groups = {CreateValidationGroup.class})
    private String name;
    @NotNull(groups = {CreateValidationGroup.class})
    private Long boardId;
}
