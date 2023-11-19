package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TaskNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.TaskRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.TaskOrderingService;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;


/**
 * Service class that provides business logic for task-related operations.
 */
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskOrderingService taskOrderingService;
    private final ColumnRepository columnRepository;

    /**
     * Constructs a new TaskServiceImpl with the given dependencies.
     *
     * @param taskRepository       The repository for task entities.
     * @param taskOrderingService  The service for managing task ordering.
     * @param columnRepository     The repository for column entities.
     */
    public TaskServiceImpl(TaskRepository taskRepository,
                           TaskOrderingService taskOrderingService,
                           ColumnRepository columnRepository) {
        this.taskRepository = taskRepository;
        this.taskOrderingService = taskOrderingService;
        this.columnRepository = columnRepository;
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to retrieve.
     * @return The task with the specified ID.
     * @throws TaskNotFoundExceptionTrello If no task with the specified ID is found.
     */
    @Override
    public Task findById(Long id) throws TaskNotFoundExceptionTrello {
        return taskRepository.findById(id)
                .orElseThrow(()
                        -> new TaskNotFoundExceptionTrello("Task with id " + id + " not found"));
    }

    /**
     * Retrieves all tasks.
     *
     * @return A list of all tasks.
     */
    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    /**
     * Creates a new task.
     *
     * @param entity The task entity to create.
     * @return The created task entity.
     */
    @Override
    @Transactional
    public Task create(Task entity) {
        entity.setDateOfCreation(LocalDateTime.now());
        Task task = taskRepository.saveAndFlush(entity);
        // Insert into tasks_ordering table
        taskOrderingService.insert(task);
        return task;
    }

    /**
     * Updates an existing task with the provided information.
     *
     * @param task The updated task information.
     * @param id   The ID of the task to update.
     * @return The updated task entity.
     * @throws ColumnNotFoundExceptionTrello If the associated column is not found.
     * @throws TaskNotFoundExceptionTrello   If no task with the specified ID is found.
     */
    @Override
    @Transactional
    public Task update(Task task, Long id) throws ColumnNotFoundExceptionTrello, TaskNotFoundExceptionTrello {
        Task existingTask = findById(id);

        if (task.getName() != null) {
            existingTask.setName(task.getName());
        }

        if (task.getDescription() != null) {
            existingTask.setDescription(task.getDescription());
        }

        if (task.getColumn() != null) {
            Column column = columnRepository.findById(task.getColumn().getId())
                    .orElseThrow(()
                            -> new ColumnNotFoundExceptionTrello(
                            "Column with id " + task.getColumn().getId() + " not found"));
            existingTask.setColumn(column);
        }

        return taskRepository.save(existingTask);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id The ID of the task to delete.
     * @return {@code true} if the task is successfully deleted, {@code false} otherwise.
     */
    @Override
    @Transactional
    public boolean delete(Long id) {
        return taskRepository.findById(id)
                .map(entity -> {
                    taskRepository.delete(entity);
                    taskRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
