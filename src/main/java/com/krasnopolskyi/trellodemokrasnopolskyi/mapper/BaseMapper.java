package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

public interface BaseMapper<Entity, Dto> {
    Dto mapToDto(Entity entity);

//    Entity mapToEntity(Dto dto);
}
