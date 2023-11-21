package com.krasnopolskyi.trellodemokrasnopolskyi.integration;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.http.handler.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskRestControllerIntegrationTest extends IntegrationTestBase {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getTaskById_ExistingId_ReturnsTask() {
        // Arrange & Act
        ResponseEntity<TaskReadResponse> response = restTemplate.getForEntity(
                "/api/v1/tasks/{id}", TaskReadResponse.class, 10);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("for done", response.getBody().getName());
        // Add more assertions based on the expected behavior
    }

    @Test
    void getTaskById_NoExistId_ShouldReturnNotFound() {
        // Arrange & Act
        ResponseEntity<TaskReadResponse> response = restTemplate.getForEntity(
                "/api/v1/tasks/{id}", TaskReadResponse.class, 1234);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void createTask_ValidRequest_ShouldReturnCreatedTask() {
        // Arrange
        TaskCreateRequest request = TaskCreateRequest.builder().name("test task").columnId(6L).build();
        // Set request with valid data

        // Act
        ResponseEntity<Task> response = restTemplate.postForEntity(
                "/api/v1/tasks", request, Task.class);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("test task", response.getBody().getName());
    }

    @Test
    void createTask_InvalidRequest_ShouldReturnUnprocessableEntity() {
        // Arrange
        TaskCreateRequest request = TaskCreateRequest.builder().name("t").columnId(6L).build();
        // Set request with validation errors

        // Act
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/v1/tasks", request, ErrorResponse.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateTask_ExistingIdAndValidRequest_ShouldReturnUpdatedTask() {
        // Arrange
        TaskCreateRequest request =
                TaskCreateRequest.builder()
                        .name("updated task")
                        .description("new")
                        .columnId(6L)
                        .build();
        // Set request with valid data

        // Act
        ResponseEntity<TaskReadResponse> response = restTemplate.exchange(
                "/api/v1/tasks/{id}", HttpMethod.PUT,
                new HttpEntity<>(request), TaskReadResponse.class, 10L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("updated task", response.getBody().getName());
        assertEquals("new", response.getBody().getDescription());

    }

    @Test
    void updateTask_InvalidRequest_ShouldReturnUnprocessableEntity() {
        // Arrange
        TaskCreateRequest request = TaskCreateRequest.builder()
                .name("u")
                .description("new")
                .columnId(6L)
                .build();
        // Set request with validation errors

        // Act
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                "/api/v1/tasks/{id}", HttpMethod.PUT,
                new HttpEntity<>(request), ErrorResponse.class, 10L);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    void deleteTask_ExistingId_ShouldReturnNoContent() {
        // Arrange & Act
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/v1/tasks/{id}", HttpMethod.DELETE,
                null, Void.class, 9L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteTask_NoExistId_ShouldReturnNotFound() {
        // Act & Assert
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/v1/tasks/{id}", HttpMethod.DELETE,
                null, Void.class, 1000L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
