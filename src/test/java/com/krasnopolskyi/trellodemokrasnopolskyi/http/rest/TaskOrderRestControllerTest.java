package com.krasnopolskyi.trellodemokrasnopolskyi.http.rest;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_order_dto.TaskOrderEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.TaskOrder;
import com.krasnopolskyi.trellodemokrasnopolskyi.http.rest.BoardRestController;
import com.krasnopolskyi.trellodemokrasnopolskyi.http.rest.TaskOrderRestController;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.TaskMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.TaskOrderingService;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@WebMvcTest(TaskOrderRestController.class)
class TaskOrderRestControllerTest {
    @MockBean
    private TaskOrderingService taskOrderingService;
    @InjectMocks
    private TaskOrderRestController taskOrderRestController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getOrderedTasks() throws Exception {
        List<Task> tasks = Arrays.asList(
                Task.builder().id(1L).name("Task 1").build(),
                Task.builder().id(2L).name("Task 2").build());

        List<TaskReadResponse> expectedResponses =
                Arrays.asList(
                        TaskReadResponse.builder().id(1L).name("task 1").build(),
                        TaskReadResponse.builder().id(2L).name("task 2").build());
        // Mocking the service response
        when(taskOrderingService.findAllByColumnByUserOrder(any(Long.class))).thenReturn(expectedResponses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/columns/{columnId}/tasks/order", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testMoveTaskMismatchedIds() throws Exception {
        // Mock data
        Long taskId = 1L;
        TaskOrderEditRequest request = new TaskOrderEditRequest();
        request.setTaskId(2L); // Mismatched ID
        // Convert request to JSON
        String requestJson = new ObjectMapper().writeValueAsString(request);

        // Setup mockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(taskOrderRestController).build();

        // Perform the PUT request
        mockMvc.perform(put("/api/v1/columns/tasks/1/move", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }
}
