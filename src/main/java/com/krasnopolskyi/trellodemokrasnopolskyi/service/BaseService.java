package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TrelloEntityNotFoundException;

import java.util.List;


public interface BaseService<T> {

    T findById(Long id) throws TrelloEntityNotFoundException;

    T create(T entity) throws TrelloEntityNotFoundException;

    List<T> findAll();

    boolean delete(Long id);

}
