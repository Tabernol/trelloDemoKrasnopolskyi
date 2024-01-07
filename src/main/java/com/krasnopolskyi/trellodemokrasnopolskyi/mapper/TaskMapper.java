package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskMapper implements BaseMapper<Task, TaskReadResponse, TaskCreateRequest> {

    public TaskReadResponse mapToDto(Task task) {
        return TaskReadResponse.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .dateOfCreation(task.getDateOfCreation())
                .status(task.getStatus())
                .columnId(task.getColumn().getId())
                .build();
    }

    public Task mapToEntity(TaskCreateRequest taskCreateRequest) {
        return Task.builder()
                .name(taskCreateRequest.getName())
                .description(taskCreateRequest.getDescription())
                .column(Column.builder().id(taskCreateRequest.getColumnId()).build())
                .build();
    }

    public Task mapToEntity(TaskEditRequest taskEditRequest) {
        return Task.builder()
                .name(taskEditRequest.getName())
                .description(taskEditRequest.getDescription())
                .status(taskEditRequest.getStatus())
                .build();
    }


    public List<TaskReadResponse> mapAll(List<Task> source) {
        List<TaskReadResponse> result = new ArrayList<>();
        for (Task task : source) {
            result.add(mapToDto(task));
        }
        return result;
    }
}
