package com.krasnopolskyi.trellodemokrasnopolskyi.integration;


import com.krasnopolskyi.trellodemokrasnopolskyi.anotation.IT;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.BoardNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.BoardRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.impl.BoardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class BoardIntegrationTest extends IntegrationTestBase {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardServiceImpl boardService;

    @Test
    void findById_ExistingId_ReturnsBoard() throws BoardNotFoundExceptionTrello {
        // Arrange
        Board testBoard = Board.builder()
                .id(3L)
                .name("board 3")
                .owner("test@test.ua")
                .build();

        Board savedBoard = boardRepository.save(testBoard);
        // Ensure the board is saved before testing the service

        // Act
        Board result = boardService.findById(3L);

        // Assert
        assertNotNull(result);
        assertEquals(savedBoard.getId(), result.getId());
    }


}
