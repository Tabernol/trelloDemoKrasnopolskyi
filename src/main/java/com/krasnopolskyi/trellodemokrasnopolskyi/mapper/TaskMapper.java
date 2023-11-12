package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskMapper implements BaseMapper<Task, TaskReadDto, TaskPostDto> {

    public TaskReadDto mapToDto(Task task) {
        return TaskReadDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .dateOfCreation(task.getDateOfCreation())
                .columnId(task.getColumn().getId())
                .build();
    }

    public Task mapToEntity(TaskPostDto taskPostDto) {
        return Task.builder()
                .name(taskPostDto.getName())
                .description(taskPostDto.getDescription())
                .column(Column.builder().id(taskPostDto.getColumnId()).build())
                .build();
    }


    public List<TaskReadDto> mapAll(List<Task> source) {
        List<TaskReadDto> result = new ArrayList<>();
        for (Task task : source) {
            result.add(mapToDto(task));
        }
        return result;
    }
}
