package com.krasnopolskyi.trellodemokrasnopolskyi.dto.post;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColumnPostDto {
    @Size(min = 1, max = 64)
    private String name;
    @NotBlank(message = "board id may not be blank")
    private Long boardId;
}
