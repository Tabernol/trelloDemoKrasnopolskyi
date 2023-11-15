package com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_order_dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColumnOrderDto {
    @NotNull
    private Long columnId;
    private int orderIndex;
}
