package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Status;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.IncorrectStatusChangeExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.StatusNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TaskNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.TaskMapper;
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
 *
 * @author Maksym Krasnopolskyi
 */
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskOrderingService taskOrderingService;
    private final ColumnRepository columnRepository;
    private final TaskMapper taskMapper;

    /**
     * Constructs a new TaskServiceImpl with the given dependencies.
     *
     * @param taskRepository      The repository for task entities.
     * @param taskOrderingService The service for managing task ordering.
     * @param columnRepository    The repository for column entities.
     * @param taskMapper          Mapper for task.
     * @param taskUtils           Utils class for task
     */
    public TaskServiceImpl(TaskRepository taskRepository,
                           TaskOrderingService taskOrderingService,
                           ColumnRepository columnRepository,
                           TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskOrderingService = taskOrderingService;
        this.columnRepository = columnRepository;
        this.taskMapper = taskMapper;
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to retrieve.
     * @return The task with the specified ID.
     * @throws TaskNotFoundExceptionTrello If no task with the specified ID is found.
     */
    @Override
    public TaskReadResponse findById(Long id) throws TaskNotFoundExceptionTrello {
        return taskMapper.mapToDto(taskRepository.findById(id)
                .orElseThrow(()
                        -> new TaskNotFoundExceptionTrello("Task with id " + id + " not found")));
    }

    /**
     *
     * @param id id of task
     * @return new status of task
     * @throws TaskNotFoundExceptionTrello if task not found
     * @throws StatusNotFoundExceptionTrello will be throw if status out of arrange
     */
    @Override
    public String findStatusById(Long id) throws TaskNotFoundExceptionTrello, StatusNotFoundExceptionTrello {
        Task task = taskRepository.findById(id)
                .orElseThrow(()
                        -> new TaskNotFoundExceptionTrello("Task with id " + id + " not found"));
        return task.getStatus().name();
    }

    /**
     * Retrieves all tasks.
     *
     * @return A list of all tasks.
     */
    @Override
    public List<TaskReadResponse> findAll() {
        return taskMapper.mapAll(taskRepository.findAll());
    }

    /**
     * Creates a new task.
     *
     * @param taskCreateRequest The dto for creating.
     * @return The created task entity.
     */
    @Override
    @Transactional
    public TaskReadResponse create(TaskCreateRequest taskCreateRequest) throws ColumnNotFoundExceptionTrello {
        // checking if column exists
        columnRepository.findById(taskCreateRequest.getColumnId()).orElseThrow(
                () -> new ColumnNotFoundExceptionTrello("Column with id " +
                        taskCreateRequest.getColumnId() + " not found"));
        Task task = taskMapper.mapToEntity(taskCreateRequest);
        //set date of creation
        task.setDateOfCreation(LocalDateTime.now());
        task.setStatus(Status.CREATED);
        task = taskRepository.saveAndFlush(task);
        // Insert into tasks_ordering table
        taskOrderingService.insert(task);
        return taskMapper.mapToDto(task);
    }

    /**
     * Updates an existing task with the provided information.
     *
     * @param taskEditRequest The updated task information.
     * @param id              The ID of the task to update.
     * @return The updated task entity.
     * @throws ColumnNotFoundExceptionTrello If the associated column is not found.
     * @throws TaskNotFoundExceptionTrello   If no task with the specified ID is found.
     */
    @Override
    @Transactional
    public TaskReadResponse update(TaskEditRequest taskEditRequest, Long id)
            throws ColumnNotFoundExceptionTrello, TaskNotFoundExceptionTrello, IncorrectStatusChangeExceptionTrello {
        Task existingTask = taskRepository.findById(id).orElseThrow(()
                -> new TaskNotFoundExceptionTrello("Task with id " + id + " not found"));

        if (taskEditRequest.getName() != null) {
            existingTask.setName(taskEditRequest.getName());
        }
//        if (taskEditRequest.getStatus() != null) {
//            TaskUtils.checkUpdatingStatus(existingTask, taskEditRequest);
//            existingTask.setStatus(taskEditRequest.getStatus());
//        }

        if (taskEditRequest.getDescription() != null) {
            existingTask.setDescription(taskEditRequest.getDescription());
        }
        Task task = taskRepository.save(existingTask);
        return taskMapper.mapToDto(task);
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
