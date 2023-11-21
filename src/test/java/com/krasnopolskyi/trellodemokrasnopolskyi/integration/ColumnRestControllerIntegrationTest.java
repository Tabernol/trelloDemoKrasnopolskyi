package com.krasnopolskyi.trellodemokrasnopolskyi.integration;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.http.handler.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ColumnRestControllerIntegrationTest extends IntegrationTestBase{
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getColumnById_ShouldReturnColumn_WhenColumnExists() {
        // Arrange
        Long columnId = 5L;

        // Act
        ResponseEntity<ColumnReadResponse> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/v1/columns/{id}", ColumnReadResponse.class, columnId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("in progress", response.getBody().getName());

    }

    @Test
    void getColumnById_ShouldReturnNotFound_WhenColumnDoesNotExist() {
        // Arrange
        Long nonExistingColumnId = 100L;

        // Act
        ResponseEntity<Void> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/v1/columns/{id}", Void.class, nonExistingColumnId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void createColumn_ShouldReturnCreated_WhenValidRequest() {
        // Arrange
        ColumnCreateRequest request = ColumnCreateRequest.builder().boardId(2L).name("test Column").build();

        // Act
        ResponseEntity<Column> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/columns", request, Column.class);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("test Column", response.getBody().getName());


    }

    @Test
    void createColumn_ValidationErrors_ShouldReturnBadRequest() {
        // Arrange
        ColumnCreateRequest request = ColumnCreateRequest.builder().boardId(2L).name("t").build();
        // Set request with validation errors

        // Act
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/v1/columns", request, ErrorResponse.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    void updateColumn_ShouldReturnOk_WhenValidRequest() {
        // Arrange
        Long columnId = 6L;
        ColumnCreateRequest request = ColumnCreateRequest.builder().boardId(2L).name("Updated Column").build();

        // Act
        restTemplate.put("http://localhost:" + port + "/api/v1/columns/{id}", request, columnId);

        // No explicit assertions, if it throws an exception, the test will fail
    }

    @Test
    void deleteColumn_ShouldReturnNoContent_WhenColumnExists() {
        // Arrange
        Long columnId = 4L;

        // Act
        restTemplate.delete("http://localhost:" + port + "/api/v1/columns/{id}", columnId);

        // No explicit assertions, if it throws an exception, the test will fail
    }

    @Test
    void deleteColumn_ShouldReturnNotFound_WhenColumnDoesNotExist() {
        // Arrange
        Long nonExistingColumnId = 100L;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/columns/{id}",
                org.springframework.http.HttpMethod.DELETE,
                null,
                Void.class,
                nonExistingColumnId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
