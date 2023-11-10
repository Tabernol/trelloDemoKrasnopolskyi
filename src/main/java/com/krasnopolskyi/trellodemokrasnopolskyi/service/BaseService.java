package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;

import java.util.List;
import java.util.Optional;

public interface BaseService<T> {

    Optional<T> findById(Long id);

    T create(T entity);

    List<T> findAll();

    Optional<T> update(T entity, Long id);

    boolean delete(Long id);

}
