package com.krasnopolskyi.trellodemokrasnopolskyi.service;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.post.TaskPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;

import java.util.Optional;

public interface TaskService extends BaseService<Task> {

    Optional<Task> update(Task task, Long id);
}
