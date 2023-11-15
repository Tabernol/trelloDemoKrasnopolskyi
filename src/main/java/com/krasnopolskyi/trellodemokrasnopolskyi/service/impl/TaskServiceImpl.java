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
    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(()
                        -> new TaskNotFoundExceptionTrello("Task with id " + id + " not found"));
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

//    @Override
//    public List<Task> findAllByColumn(Long columnId) {
//        return taskRepository.findAllByColumn(columnId);
//    }

//    @Override
//    public List<Task> findAllByColumnByUserOrder(Long columnId) {
//        List<Task> listOfTask = taskRepository.findAllByColumn(columnId);// none sorted
//        List<Long> userOrder = taskOrderService.findAllIdTasksByColumnInUserOrder(columnId);//only id of Task in user order
//        Map<Long, Task> tasks = listOfTask
//                .stream()
//                .collect(Collectors.toMap((Task::getId), Function.identity()));
//
//        List<Task> sortedTasks = userOrder
//                .stream()
//                .map(tasks::get)
//                .collect(Collectors.toList());
//
//        return sortedTasks;
//    }

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
    public Task update(Task task, Long id) {
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
                            -> new ColumnNotFoundExceptionTrello("Column with id " + task.getColumn().getId() + " not found"));
            existingTask.setColumn(column);
        }
//        if (task.getColumnId() != null) {
//            Column column = columnRepository.findById(task.getColumnId())
//                    .orElseThrow(()
//                            -> new ColumnNotFoundExceptionTrello("Column with id " + task.getColumnId() + " not found"));
//            existingTask.setColumnId(column.getId());
//        }

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
