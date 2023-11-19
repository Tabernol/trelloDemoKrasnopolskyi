package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.BoardNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnOrderingService;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnService;
import com.krasnopolskyi.trellodemokrasnopolskyi.validator.BoardValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service class that provides business logic for column-related operations.
 */
@Service
public class ColumnServiceImpl implements ColumnService {

    private final BoardValidator boardValidator;
    private final ColumnRepository columnRepository;
    private final ColumnOrderingService columnOrderingService;

    /**
     * Constructs a new ColumnServiceImpl with the given dependencies.
     *
     * @param boardValidator        The validator for board entities.
     * @param columnRepository      The repository for column entities.
     * @param columnOrderingService The service for managing column ordering.
     */
    public ColumnServiceImpl(
            BoardValidator boardValidator,
            ColumnRepository columnRepository,
            ColumnOrderingService columnOrderingService) {
        this.boardValidator = boardValidator;
        this.columnRepository = columnRepository;
        this.columnOrderingService = columnOrderingService;
    }

    /**
     * Retrieves a column by its ID.
     *
     * @param id The ID of the column to retrieve.
     * @return The column with the specified ID.
     * @throws ColumnNotFoundExceptionTrello If no column with the specified ID is found.
     */
    @Override
    public Column findById(Long id) throws ColumnNotFoundExceptionTrello {
        return columnRepository.findById(id)
                .orElseThrow(()
                        -> new ColumnNotFoundExceptionTrello("Column with id " + id + " not found"));
    }

    /**
     * Retrieves all columns.
     *
     * @return A list of all columns.
     */
    @Override
    public List<Column> findAll() {
        return columnRepository.findAll();
    }

    /**
     * Creates a new column.
     *
     * @param column The column entity to create.
     * @return The created column entity.
     * @throws BoardNotFoundExceptionTrello If the associated board is not found.
     */
    @Override
    @Transactional
    public Column create(Column column) throws BoardNotFoundExceptionTrello {
        // Checking existing board
        boardValidator.validate(column.getBoard().getId());
        column = columnRepository.saveAndFlush(column);
        // Insert into columns_ordering table
        columnOrderingService.insert(column);
        return column;
    }

    /**
     * Updates an existing column with the provided information.
     *
     * @param column The updated column information.
     * @param id     The ID of the column to update.
     * @return The updated column entity.
     * @throws ColumnNotFoundExceptionTrello If no column with the specified ID is found.
     */
    @Override
    @Transactional
    public Column update(Column column, Long id) throws ColumnNotFoundExceptionTrello {
        Column existingColumn = findById(id);
        existingColumn.setName(column.getName());
        return columnRepository.save(existingColumn);
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
