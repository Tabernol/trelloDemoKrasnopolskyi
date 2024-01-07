package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.BoardNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.ColumnMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.BoardRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnOrderingService;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service class that provides business logic for column-related operations.
 *
 * @author Maksym Krasnopolskyi
 */
@Service
public class ColumnServiceImpl implements ColumnService {

    private final BoardRepository boardRepository;
    private final ColumnRepository columnRepository;
    private final ColumnOrderingService columnOrderingService;

    private final ColumnMapper columnMapper;

    /**
     * Constructs a new ColumnServiceImpl with the given dependencies.
     *
     * @param boardRepository       The repository for board entities
     * @param columnRepository      The repository for column entities.
     * @param columnOrderingService The service for managing column ordering.
     * @param columnMapper          Mapper
     */
    public ColumnServiceImpl(

            BoardRepository boardRepository, ColumnRepository columnRepository,
            ColumnOrderingService columnOrderingService, ColumnMapper columnMapper) {
        this.boardRepository = boardRepository;
        this.columnRepository = columnRepository;
        this.columnOrderingService = columnOrderingService;
        this.columnMapper = columnMapper;
    }

    /**
     * Retrieves a column by its ID.
     *
     * @param id The ID of the column to retrieve.
     * @return The column with the specified ID.
     * @throws ColumnNotFoundExceptionTrello If no column with the specified ID is found.
     */
    @Override
    public ColumnReadResponse findById(Long id) throws ColumnNotFoundExceptionTrello {
        Column column = columnRepository.findById(id)
                .orElseThrow(()
                        -> new ColumnNotFoundExceptionTrello("Column with id " + id + " not found"));
        return columnMapper.mapToDto(column);
    }

    /**
     * Retrieves all columns.
     *
     * @return A list of all columns.
     */
    @Override
    public List<ColumnReadResponse> findAll() {
        return columnMapper.mapAll(columnRepository.findAll());
    }

    /**
     * Creates a new column.
     *
     * @param columnCreateRequest The column is dto for creating.
     * @return The created column entity.
     * @throws BoardNotFoundExceptionTrello If the associated board is not found.
     */
    @Override
    @Transactional
    public ColumnReadResponse create(ColumnCreateRequest columnCreateRequest) throws BoardNotFoundExceptionTrello {
        Long boardId = columnCreateRequest.getBoardId();
        // Checking existing board
        boardRepository.findById(boardId).orElseThrow(() ->
                new BoardNotFoundExceptionTrello("Board with id " + boardId + " not found"));

        Column column = columnMapper.mapToEntity(columnCreateRequest);
        // saving new column
        column = columnRepository.saveAndFlush(column);
        // Insert into columns_ordering table
        columnOrderingService.insert(column);
        return columnMapper.mapToDto(column);
    }

    /**
     * Updates an existing column with the provided information.
     *
     * @param columnEditRequest The updated column information.
     * @param id     The ID of the column to update.
     * @return The updated column entity.
     * @throws ColumnNotFoundExceptionTrello If no column with the specified ID is found.
     */
    @Override
    @Transactional
    public ColumnReadResponse update(ColumnEditRequest columnEditRequest, Long id) throws ColumnNotFoundExceptionTrello {
        Column existingColumn = columnRepository.findById(id).orElseThrow(()
                -> new ColumnNotFoundExceptionTrello("Column with id " + id + " not found"));
        existingColumn.setName(columnEditRequest.getName());
        existingColumn = columnRepository.save(existingColumn);
        return columnMapper.mapToDto(existingColumn);
    }

    /**
     * Deletes a column by its ID.
     *
     * @param id The ID of the column to delete.
     * @return {@code true} if the column is successfully deleted, {@code false} otherwise.
     */
    @Override
    @Transactional
    public boolean delete(Long id) {
        return columnRepository.findById(id)
                .map(entity -> {
                    columnRepository.delete(entity);
                    columnRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
