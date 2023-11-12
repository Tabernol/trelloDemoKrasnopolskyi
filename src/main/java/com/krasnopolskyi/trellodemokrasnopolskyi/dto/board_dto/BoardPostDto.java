package com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardPostDto {
    @NotBlank
    private String name;
    @Email
    private String owner;
}
