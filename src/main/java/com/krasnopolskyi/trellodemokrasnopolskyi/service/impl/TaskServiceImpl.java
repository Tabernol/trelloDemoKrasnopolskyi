package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.TaskRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ColumnRepository columnRepository;

    public TaskServiceImpl(TaskRepository taskRepository, ColumnRepository columnRepository) {
        this.taskRepository = taskRepository;
        this.columnRepository = columnRepository;
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    @Transactional
    public Task create(Task entity) {
        entity.setDateOfCreation(LocalDateTime.now());
        return taskRepository.save(entity);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Transactional
    public Optional<Task> update(Task task, Long id) {
        Optional<Task> optionalTask = findById(id);
        if (optionalTask.isPresent()) {
            Task excitingTask = optionalTask.get();

            if (task.getName() != null) {
                excitingTask.setName(task.getName());
            }

            if (task.getDescription() != null) {
                excitingTask.setDescription(task.getDescription());
            }

            if (task.getColumn().getId() != null) {
                Column column = columnRepository.findById(task.getColumn().getId()).orElseThrow(()
                        -> new ResponseStatusException(HttpStatus.NOT_FOUND));
                excitingTask.setColumn(column);
            }
            taskRepository.save(excitingTask);
        }
        return taskRepository.findById(id);
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
