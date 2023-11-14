package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.TaskOrder;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.TaskOrderRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.TaskRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.TaskOrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskOrderServiceImpl implements TaskOrderService {
    private final TaskOrderRepository taskOrderRepository;
    private final TaskRepository taskRepository;

    public TaskOrderServiceImpl(TaskOrderRepository taskOrderRepository, TaskRepository taskRepository) {
        this.taskOrderRepository = taskOrderRepository;
        this.taskRepository = taskRepository;
    }


    @Override
    public TaskOrder insert(Task task) {
        int size = taskRepository.findAllByColumn(task.getColumn().getId()).size();
        TaskOrder taskOrder = TaskOrder.builder()
                .taskId(task.getId())
                .columnId(task.getColumn().getId())
                .orderIndex(size)
                .build();
        return taskOrderRepository.save(taskOrder);
    }

    @Override
    public List<Long> findAllIdTasksByColumnInUserOrder(Long columnId) {
        return taskOrderRepository.findAllByColumnIdOrderByOrderIndex(columnId)
                .stream()
                .map(taskOrder -> taskOrder.getTaskId())
                .collect(Collectors.toList());
    }


}
