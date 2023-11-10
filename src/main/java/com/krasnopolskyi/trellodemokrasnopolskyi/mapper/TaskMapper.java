package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.read.TaskReadDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.post.TaskPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskMapper implements BaseMapper<Task, TaskReadDto> {

    private final ColumnService columnService;

    public TaskMapper(ColumnService columnService) {
        this.columnService = columnService;
    }


    @Override
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
                .column(columnService.findById(taskPostDto.getColumnId()).orElseThrow(()
                        -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Column not found")))
                .build();
    }

    public Task mapToEntityWithNullFields(TaskPostDto taskPostDto) {
        Task task = new Task();
        task.setName(taskPostDto.getName());
        task.setDescription(taskPostDto.getDescription());
//        if (taskPostDto.getColumnId() != null) {
//            task.setColumn(columnService.findById(taskPostDto.getColumnId()).orElseThrow(()
//                    -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Column not found")));
//        } else {
//            task.setColumn(null);
//        }
        return task;
    }


    public List<TaskReadDto> mapAll(List<Task> source) {
        List<TaskReadDto> result = new ArrayList<>();
        for (Task task : source) {
            result.add(mapToDto(task));
        }
        return result;
    }
}
