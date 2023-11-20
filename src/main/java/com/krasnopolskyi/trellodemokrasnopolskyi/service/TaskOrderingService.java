package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.TaskOrder;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ProhibitionMovingException;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TaskNotFoundExceptionTrello;

import java.util.List;

public interface TaskOrderingService {

    TaskOrder insert(Task task);
    List<Task> findAllByColumnByUserOrder(Long columnId);

    int moveTask(TaskOrder taskOrder) throws TaskNotFoundExceptionTrello, ColumnNotFoundExceptionTrello, ProhibitionMovingException;
}
