package com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_order_dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColumnOrderEditRequest {
    @NotNull
    @Min(value = 1)
    private Long columnId;
    @NotNull
    @Min(value = 1)
    private int orderIndex;
}
