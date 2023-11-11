package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskPostDto;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Pillar;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskMapper implements BaseMapper<Task, TaskReadDto> {

//    private final PillarValidator pillarValidator;
//
//    public TaskMapper(PillarValidator pillarValidator) {
//        this.pillarValidator = pillarValidator;
//    }


    @Override
    public TaskReadDto mapToDto(Task task) {
        return TaskReadDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .dateOfCreation(task.getDateOfCreation())
                .columnId(task.getPillar().getId())
                .build();
    }

    public Task mapToEntity(TaskPostDto taskPostDto) {

        return Task.builder()
                .name(taskPostDto.getName())
                .description(taskPostDto.getDescription())
//                .pillar(pillarService.findById(taskPostDto.getPillarId()).orElseThrow(()
//                        -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pillar not found")))
                .pillar(Pillar.builder().id(taskPostDto.getPillarId()).build())
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
