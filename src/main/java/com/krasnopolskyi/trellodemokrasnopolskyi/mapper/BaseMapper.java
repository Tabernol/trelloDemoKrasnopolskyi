package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;

import java.util.List;

public interface BaseMapper<Entity, ReadDto, PostDto> {
    ReadDto mapToDto(Entity entity);

    Entity mapToEntity(PostDto postDto);

    List<ReadDto> mapAll(List<Entity> Entity);
}
