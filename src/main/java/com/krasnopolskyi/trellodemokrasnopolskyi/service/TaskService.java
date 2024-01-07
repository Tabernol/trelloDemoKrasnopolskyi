package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.IncorrectStatusChangeExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.StatusNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TaskNotFoundExceptionTrello;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface TaskService {
    TaskReadResponse findById(Long id) throws TaskNotFoundExceptionTrello;

    String findStatusById(Long id) throws TaskNotFoundExceptionTrello, StatusNotFoundExceptionTrello;

    List<TaskReadResponse> findAll();

    @Transactional
    TaskReadResponse create(TaskCreateRequest taskCreateRequest) throws ColumnNotFoundExceptionTrello;

    @Transactional
    TaskReadResponse update(TaskEditRequest taskEditRequest, Long id)
            throws ColumnNotFoundExceptionTrello, TaskNotFoundExceptionTrello, IncorrectStatusChangeExceptionTrello;

    @Transactional
    boolean delete(Long id);


}
