package com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto;

import com.krasnopolskyi.trellodemokrasnopolskyi.validator.CreateValidationGroup;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColumnCreateRequest {
    @Size(min = 2, max = 64)
    @NotBlank()
    private String name;
    @Min(value = 1)
    private Long boardId;
}
