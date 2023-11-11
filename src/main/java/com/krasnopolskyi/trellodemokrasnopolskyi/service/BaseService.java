package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;

import java.util.List;
import java.util.Optional;

public interface BaseService<T> {

    Optional<T> findById(Long id);

    T create(T entity);

    List<T> findAll();

    boolean delete(Long id);

}
