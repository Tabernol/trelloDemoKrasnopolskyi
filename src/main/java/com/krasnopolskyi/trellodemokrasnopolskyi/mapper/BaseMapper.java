package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

public interface BaseMapper<Entity, ReadDto, PostDto> {
    ReadDto mapToDto(Entity entity);

    Entity mapToEntity(PostDto postDto);
}
