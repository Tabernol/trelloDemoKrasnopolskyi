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


@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskOrderingService taskOrderingService;
    private final ColumnRepository columnRepository;

    public TaskServiceImpl(TaskRepository taskRepository,
                           TaskOrderingService taskOrderingService,
                           ColumnRepository columnRepository) {
        this.taskRepository = taskRepository;
        this.taskOrderingService = taskOrderingService;
        this.columnRepository = columnRepository;
    }

    @Override
    public Task findById(Long id) throws TaskNotFoundExceptionTrello {
        return taskRepository.findById(id)
                .orElseThrow(()
                        -> new TaskNotFoundExceptionTrello("Task with id " + id + " not found"));
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    @Transactional
    public Task create(Task entity) {
        entity.setDateOfCreation(LocalDateTime.now());
        Task task = taskRepository.saveAndFlush(entity);
        //insert into tasks_ordering table
        taskOrderingService.insert(task);
        return task;
    }

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
