package com.krasnopolskyi.trellodemokrasnopolskyi.mapper;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskMapperTest {
    // Mock data
    private final TaskCreateRequest createEditRequest = TaskCreateRequest.builder()
            .name("Test Task")
            .description("Task description")
            .columnId(1L)
            .build();

    private final Task taskEntity = Task.builder()
            .id(1L)
            .name("Test Task")
            .description("Task description")
            .dateOfCreation(LocalDateTime.now())
            .column(Column.builder().id(1L).build())
            .build();

    private final TaskReadResponse readResponse = TaskReadResponse.builder()
            .id(1L)
            .name("Test Task")
            .description("Task description")
            .dateOfCreation(LocalDateTime.now())
            .columnId(1L)
            .build();

    private final TaskMapper taskMapper = new TaskMapper();

    @Test
    void testMapToDto() {
        TaskReadResponse result = taskMapper.mapToDto(taskEntity);

        assertEquals(taskEntity.getId(), result.getId());
        assertEquals(taskEntity.getName(), result.getName());
        assertEquals(taskEntity.getDescription(), result.getDescription());
        assertEquals(taskEntity.getDateOfCreation(), result.getDateOfCreation());
        assertEquals(taskEntity.getColumn().getId(), result.getColumnId());
    }

    @Test
    void testMapToEntity() {
        Task result = taskMapper.mapToEntity(createEditRequest);

        assertEquals(createEditRequest.getName(), result.getName());
        assertEquals(createEditRequest.getDescription(), result.getDescription());
        assertEquals(createEditRequest.getColumnId(), result.getColumn().getId());
    }

    @Test
    void testMapAll() {
        // Mock data
        List<Task> taskList = Arrays.asList(
                taskEntity,
                Task.builder()
                        .id(2L)
                        .name("Another Task")
                        .description("Another task description")
                        .dateOfCreation(LocalDateTime.now())
                        .column(Column.builder().id(1L).build())
                        .build()
        );

        // Perform mapping
        List<TaskReadResponse> result = taskMapper.mapAll(taskList);

        // Verify the result
        assertEquals(taskList.size(), result.size());
        assertEquals(taskEntity.getId(), result.get(0).getId());
        assertEquals(taskEntity.getName(), result.get(0).getName());
        assertEquals(taskEntity.getDescription(), result.get(0).getDescription());
        assertEquals(taskEntity.getDateOfCreation(), result.get(0).getDateOfCreation());
        assertEquals(taskEntity.getColumn().getId(), result.get(0).getColumnId());
    }
}
