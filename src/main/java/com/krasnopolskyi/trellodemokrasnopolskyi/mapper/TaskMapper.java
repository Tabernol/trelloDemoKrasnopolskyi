package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskMapper implements BaseMapper<Task, TaskReadDto> {

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
                .column(Column.builder().id(taskPostDto.getColumnId()).build())
                .build();
    }

//    public Task mapToEntityWithNullFields(TaskPostDto taskPostDto) {
//        Task task = new Task();
//        task.setName(taskPostDto.getName());
//        task.setDescription(taskPostDto.getDescription());
////        if (taskPostDto.getColumnId() != null) {
////            task.setColumn(columnService.findById(taskPostDto.getColumnId()).orElseThrow(()
////                    -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Column not found")));
////        } else {
////            task.setColumn(null);
////        }
//        return task;
//    }


    public List<TaskReadDto> mapAll(List<Task> source) {
        List<TaskReadDto> result = new ArrayList<>();
        for (Task task : source) {
            result.add(mapToDto(task));
        }
        return result;
    }
}
