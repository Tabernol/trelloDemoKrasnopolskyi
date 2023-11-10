package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.post.ColumnPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.post.TaskPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ColumnMapper implements BaseMapper<Column, ColumnPostDto> {
    @Override
    public ColumnPostDto mapToDto(Column column) {
        return null;
    }

    public Column mapToEntity(ColumnPostDto columnPostDto) {
        return Column.builder()
                .name(columnPostDto.getName())
                .build();
    }
}
