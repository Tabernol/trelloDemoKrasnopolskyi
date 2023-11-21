package com.krasnopolskyi.trellodemokrasnopolskyi.http.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.TaskMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnService;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.TaskService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@WebMvcTest(TaskRestController.class)
public class TaskRestControllerTest {
    @MockBean
    private TaskService taskService;
    @MockBean
    private ColumnService columnService;
    @MockBean
    private TaskMapper taskMapper;
    @InjectMocks
    private TaskRestController taskRestController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetTaskById() throws Exception {
        taskRestController = new TaskRestController(taskService, columnService, taskMapper);
        Long taskId = 1L;
        LocalDateTime testTime = LocalDateTime.parse("2023-11-18T02:25:29");
        Task task = Task.builder()
                .id(taskId)
                .name("Task 1")
                .description("Description for Task 1")
                .dateOfCreation(testTime)
                .column(Column.builder().id(1L).build())
                .build();
        TaskReadResponse expectedResponse = TaskReadResponse.builder().id(taskId)
                .name("Task 1")
                .description("Description for Task 1")
                .dateOfCreation(testTime)
                .columnId(1L)
                .build();

        when(taskService.findById(taskId)).thenReturn(task);
        when(taskMapper.mapToDto(task)).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedResponse.getId()))
                .andExpect(jsonPath("$.name").value(expectedResponse.getName()))
                .andExpect(jsonPath("$.description").value(expectedResponse.getDescription()))
                .andExpect(jsonPath("$.dateOfCreation").value(expectedResponse.getDateOfCreation().toString()))
                .andExpect(jsonPath("$.columnId").value(expectedResponse.getColumnId()));

        verify(taskService, times(1)).findById(taskId);
    }

    @Test
    void testGetAllTasks() throws Exception {
        List<Task> tasks = Arrays.asList(
                Task.builder().id(1L).name("Task 1").build(),
                Task.builder().id(2L).name("Task 2").build()
        );
        List<TaskReadResponse> expectedResponses =
                Arrays.asList(
                        TaskReadResponse.builder().id(1L).name("task 1").build(),
                        TaskReadResponse.builder().id(2L).name("task 2").build());

        when(taskService.findAll()).thenReturn(tasks);
        when(taskMapper.mapAll(tasks)).thenReturn(expectedResponses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(expectedResponses.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(expectedResponses.get(0).getName()))
                .andExpect(jsonPath("$[1].id").value(expectedResponses.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(expectedResponses.get(1).getName()));

        verify(taskService, times(1)).findAll();
    }

    @Test
    void testCreateTask() throws Exception {
        TaskCreateRequest request = TaskCreateRequest.builder()
                .name("New Task")
                .description("Description for New Task")
                .columnId(1L)
                .build();


        Task taskToCreate = Task.builder().name("New Task")
                .description("Description for New Task")
                .column(Column.builder().id(1L).build())
                .build();

        Task createdTask = Task.builder()
                .id(1L)
                .name(request.getName())
                .description(request.getDescription())
                .column(Column.builder().id(request.getColumnId()).build())
                .dateOfCreation(LocalDateTime.parse("2023-11-18T02:25:29"))
                .build();

        when(taskMapper.mapToEntity(request)).thenReturn(taskToCreate);
        when(columnService.findById(request.getColumnId())).thenReturn(Column.builder().id(request.getColumnId()).build());
        when(taskService.create(taskToCreate)).thenReturn(createdTask);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(createdTask.getId()))
                .andExpect(jsonPath("$.name").value(createdTask.getName()))
                .andExpect(jsonPath("$.description").value(createdTask.getDescription()))
                .andExpect(jsonPath("$.dateOfCreation").value(createdTask.getDateOfCreation().toString()));

        verify(columnService, times(1)).findById(request.getColumnId());
        verify(taskService, times(1)).create(taskToCreate);
    }

    @Test
    void testDeleteTask() throws Exception {
        Long taskId = 1L;

        when(taskService.delete(taskId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tasks/{id}", taskId))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).delete(taskId);
    }
}
