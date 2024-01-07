package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.board_dto.BoardReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.BoardNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TrelloException;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.BoardMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class BoardServiceImplTest {
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private BoardMapper boardMapper;
    @InjectMocks
    private BoardServiceImpl boardService;

    private Board board;

    private BoardEditRequest boardEditRequest;
    private BoardCreateRequest boardCreateRequest;

    private BoardReadResponse boardReadResponse;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        board = Board.builder().id(3L).name("board3").owner("test@test.ua").build();
        boardReadResponse = BoardReadResponse.builder().id(3L).name("board3").owner("test@test.ua").build();
        boardEditRequest = BoardEditRequest.builder().name("New name").build();
        boardService = new BoardServiceImpl(boardRepository, boardMapper);
    }

    @Test
    void testFindById_ExistingBoard_ShouldReturnBoard() throws TrelloException {
        // Arrange
        when(boardRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(board));
        when(boardMapper.mapToDto(board)).thenReturn(boardReadResponse);

        // Act
        BoardReadResponse resultBoard = boardService.findById(3L);

        // Assert
        assertNotNull(resultBoard);
        assertEquals(3L, resultBoard.getId());
        assertEquals(boardReadResponse, resultBoard);
        verify(boardRepository, times(1)).findById(3L);
    }

    @Test
    void testFindById_NonExistingBoard_ShouldThrowException() {
        // Arrange
        when(boardRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(BoardNotFoundExceptionTrello.class, () -> boardService.findById(3L));
        verify(boardRepository, times(1)).findById(3L);
    }

    @Test
    void testFindAll_ShouldReturnListOfBoards() {
        List<Board> boards = Collections.singletonList(board);
        List<BoardReadResponse> responseList = Collections.singletonList(boardReadResponse);
        // Arrange
        when(boardRepository.findAll()).thenReturn(boards);
        when(boardMapper.mapAll(boards)).thenReturn(responseList);

        // Act
        List<BoardReadResponse> resultBoards = boardService.findAll();

        // Assert
        assertFalse(resultBoards.isEmpty());
        assertEquals(1, resultBoards.size());
        assertEquals(boardReadResponse, resultBoards.get(0));
        verify(boardRepository, times(1)).findAll();
    }

    @Test
    void testCreate_ShouldReturnCreatedBoard() {
        // Arrange
        when(boardRepository.save(Mockito.any())).thenReturn(board);
        when(boardMapper.mapToEntity(boardCreateRequest)).thenReturn(board);
        when(boardMapper.mapToDto(board)).thenReturn(boardReadResponse);

        // Act
        BoardReadResponse createdBoard = boardService.create(boardCreateRequest);

        // Assert
        assertNotNull(createdBoard);
        assertEquals(boardReadResponse, createdBoard);
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    void testUpdate_ExistingBoard_ShouldReturnUpdatedBoard() throws BoardNotFoundExceptionTrello {
        // Arrange
        when(boardRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(board));
        when(boardRepository.save(Mockito.any())).thenReturn(board);
        when(boardMapper.mapToDto(board)).thenReturn(boardReadResponse);


        // Act
        BoardReadResponse updatedBoard = boardService.update(boardEditRequest, 3L);

        // Assert
        assertNotNull(updatedBoard);
        assertEquals(boardReadResponse, updatedBoard);
        verify(boardRepository, times(1)).findById(3L);
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    void testUpdate_NonExistingBoard_ThrowsException() throws BoardNotFoundExceptionTrello {
        // Arrange
        when(boardRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(BoardNotFoundExceptionTrello.class, () -> boardService.update(boardEditRequest, 3L));

        verify(boardRepository, times(1)).findById(3L);
        verify(boardRepository, never()).save(Mockito.any());
    }

    @Test
    void testDelete_ExistingBoard_ShouldReturnTrue() {
        // Arrange
        when(boardRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(board));

        // Act
        boolean result = boardService.delete(3L);

        // Assert
        assertTrue(result);
        verify(boardRepository, times(1)).findById(3L);
        verify(boardRepository, times(1)).delete(board);
        verify(boardRepository, times(1)).flush();
    }

    @Test
    void testDelete_NonExistingBoard_ShouldReturnFalse() {
        // Arrange
        when(boardRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // Act
        boolean result = boardService.delete(3L);

        // Assert
        assertFalse(result);
        verify(boardRepository, times(1)).findById(3L);
        verify(boardRepository, never()).delete(Mockito.any());
        verify(boardRepository, never()).flush();
    }
}




