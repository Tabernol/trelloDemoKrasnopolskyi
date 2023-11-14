package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.TaskOrder;

import java.util.List;

public interface TaskOrderService {

    TaskOrder insert(Task task);

    List<Long> findAllIdTasksByColumnInUserOrder(Long columnId);
}
