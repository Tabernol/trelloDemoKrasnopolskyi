package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TaskNotFoundExceptionTrello;

import java.util.List;


public interface TaskService extends BaseService<Task> {

    Task update(Task task, Long id) throws TaskNotFoundExceptionTrello, ColumnNotFoundExceptionTrello;

//    List<Task> findAllByColumn(Long columnId);

}
