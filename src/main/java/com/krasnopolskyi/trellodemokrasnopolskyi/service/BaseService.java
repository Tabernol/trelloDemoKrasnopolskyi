package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T> {

    Optional<T> findById(Long id);

    T create(T entity);

    List<T> findAll();

    T edit(T entity, Long id);

    void delete(Long id);

}
