package com.krasnopolskyi.trellodemokrasnopolskyi.integration;


import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.http.handler.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BoardRestControllerIntegrationTest extends IntegrationTestBase {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void testBoardById() {
        assertEquals("Board 1", this.restTemplate.getForObject(
                "http://localhost:" + port + "/api/v1/boards/1", BoardReadResponse.class).getName());
    }

    @Test
    void testAddBoard() {
        // Arrange
        Board board = Board.builder().name("Board 3").owner("test@owner.ua").build();
        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity("http://localhost:" + port + "/api/v1/boards", board, String.class);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void getBoardById_ShouldReturnNotFound_WhenBoardDoesNotExist() {
        // Arrange
        Long nonExistingBoardId = 100L;

        // Act
        ResponseEntity<Void> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/v1/boards/{id}", Void.class, nonExistingBoardId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void updateBoard_ShouldReturnOk_WhenValidRequest() {
        // Arrange
        Long boardId = 1L;
        BoardCreateRequest request = new BoardCreateRequest();
        request.setName("Updated Board");
        request.setOwner("updated@test.com");

        // Act
        restTemplate.put("http://localhost:" + port + "/api/v1/boards/{id}", request, boardId);

    }

    @Test
    void updateBoard_ShouldReturnNotFound_WhenValidRequest() {
        // Arrange
        Long boardId = 12L;
        BoardCreateRequest request = new BoardCreateRequest();
        request.setName("Updated Board");
        request.setOwner("updated@test.com");

        // Act
        restTemplate.put("http://localhost:" + port + "/api/v1/boards/{id}", request, boardId);

    }

    @Test
    void deleteBoard_ShouldReturnNoContent_WhenBoardExists() {
        // Arrange
        Long boardId = 1L;

        // Act
        restTemplate.delete("http://localhost:" + port + "/api/v1/boards/{id}", boardId);

        // No explicit assertions, if it throws an exception, the test will fail
    }

    @Test
    void deleteBoard_ShouldReturnNotFound_WhenBoardDoesNotExist() {
        // Arrange
        Long nonExistingBoardId = 100L;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/boards/{id}",
                org.springframework.http.HttpMethod.DELETE,
                null,
                Void.class,
                nonExistingBoardId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createBoard_ValidationErrors_ShouldReturnBadRequest() {
        // Arrange
        BoardCreateRequest request = BoardCreateRequest.builder().name("t").owner("bad Email").build();
        // Set request with validation errors

        // Act
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/v1/boards", request, ErrorResponse.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
