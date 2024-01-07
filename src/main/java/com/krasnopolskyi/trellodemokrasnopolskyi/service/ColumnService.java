package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.BoardNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ColumnService {

    ColumnReadResponse findById(Long id) throws ColumnNotFoundExceptionTrello;

    List<ColumnReadResponse> findAll();

    @Transactional
    ColumnReadResponse create(ColumnCreateRequest columnCreateRequest) throws BoardNotFoundExceptionTrello;

    @Transactional
    ColumnReadResponse update(ColumnEditRequest columnEditRequest, Long id) throws ColumnNotFoundExceptionTrello;

    @Transactional
    boolean delete(Long id);
}
