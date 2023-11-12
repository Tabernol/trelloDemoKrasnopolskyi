package com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto;


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
public class ColumnEditDto {
    @Size(min = 2, max = 64)
    @NotBlank()
    private String name;
}
