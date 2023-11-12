package com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto;


import jakarta.validation.constraints.Email;
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
public class BoardPostDto {
    @NotBlank
    @Size(min = 2, max = 64)
    private String name;
    @Email
    @Size(max = 64)
    private String owner;
}
