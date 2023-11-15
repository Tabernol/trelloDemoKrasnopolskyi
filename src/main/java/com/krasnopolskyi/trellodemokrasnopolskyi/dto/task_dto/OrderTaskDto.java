package com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderTaskDto {
    @Min(value = 1)
    @NotNull
    private Long taskId;
    private Long columnId;
    @Min(value = 1)
    @NotNull
    private Integer newOrderIndex;
}
