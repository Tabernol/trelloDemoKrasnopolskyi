package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskCreateEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskMapper implements BaseMapper<Task, TaskReadResponse, TaskCreateEditRequest> {

    public TaskReadResponse mapToDto(Task task) {
        return TaskReadResponse.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .dateOfCreation(task.getDateOfCreation())
                .columnId(task.getColumn().getId())
                .build();
    }

    public Task mapToEntity(TaskCreateEditRequest taskCreateEditRequest) {
        return Task.builder()
                .name(taskCreateEditRequest.getName())
                .description(taskCreateEditRequest.getDescription())
                .column(Column.builder().id(taskCreateEditRequest.getColumnId()).build())
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
