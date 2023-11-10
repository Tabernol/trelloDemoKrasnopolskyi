package com.krasnopolskyi.trellodemokrasnopolskyi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskReadDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime dateOfCreation;
    private Long columnId;
}