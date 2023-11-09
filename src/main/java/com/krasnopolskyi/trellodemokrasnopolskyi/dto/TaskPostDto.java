package com.krasnopolskyi.trellodemokrasnopolskyi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskPostDto {
    private String name;
    private String description;
    private Long columnId;
}
