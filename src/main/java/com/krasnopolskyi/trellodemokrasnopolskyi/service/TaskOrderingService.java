package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.TaskOrder;

import java.util.List;

public interface TaskOrderingService {

    TaskOrder insert(Task task);
    List<Task> findAllByColumnByUserOrder(Long columnId);

    List<Long> findAllIdTasksByColumnInUserOrder(Long columnId);

    int moveTask(TaskOrder taskOrder);
}
