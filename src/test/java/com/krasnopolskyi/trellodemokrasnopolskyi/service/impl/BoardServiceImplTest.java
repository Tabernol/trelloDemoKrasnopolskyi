package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.BoardNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TrelloEntityNotFoundException;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.BoardRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
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
    @InjectMocks
    private BoardServiceImpl boardService;

    private static final Board MOCK_BOARD_3 = Board.builder()
            .id(3L)
            .name("board3")
            .owner("test@test.ua").build();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        boardService = new BoardServiceImpl(boardRepository);
    }

    @Test
    void testFindById_ExistingBoard_ShouldReturnBoard() throws TrelloEntityNotFoundException {
        when(boardRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(MOCK_BOARD_3));

        Board resultBoard = boardService.findById(3L);

        assertNotNull(resultBoard);
        assertEquals(3L, resultBoard.getId());
        verify(boardRepository, times(1)).findById(3L);
    }

    @Test
    void testFindById_NonExistingBoard_ShouldThrowException() {
        when(boardRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(BoardNotFoundExceptionTrello.class, () -> boardService.findById(3L));

        verify(boardRepository, times(1)).findById(3L);
    }

    @Test
    void testFindAll_ShouldReturnListOfBoards() {
        when(boardRepository.findAll()).thenReturn(Collections.singletonList(MOCK_BOARD_3));

        List<Board> resultBoards = boardService.findAll();

        assertFalse(resultBoards.isEmpty());
        assertEquals(1, resultBoards.size());
        assertEquals(MOCK_BOARD_3, resultBoards.get(0));
        verify(boardRepository, times(1)).findAll();
    }

    @Test
    void testCreate_ShouldReturnCreatedBoard() {
        when(boardRepository.save(Mockito.any())).thenReturn(MOCK_BOARD_3);

        Board createdBoard = boardService.create(MOCK_BOARD_3);

        assertNotNull(createdBoard);
        assertEquals(MOCK_BOARD_3, createdBoard);
        verify(boardRepository, times(1)).save(MOCK_BOARD_3);
    }

    @Test
    void testUpdate_ExistingBoard_ShouldReturnUpdatedBoard() throws BoardNotFoundExceptionTrello {
        when(boardRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(MOCK_BOARD_3));
        when(boardRepository.save(Mockito.any())).thenReturn(MOCK_BOARD_3);

        Board updatedBoard = boardService.update(MOCK_BOARD_3, 3L);

        assertNotNull(updatedBoard);
        assertEquals(MOCK_BOARD_3, updatedBoard);
        verify(boardRepository, times(1)).findById(3L);
        verify(boardRepository, times(1)).save(MOCK_BOARD_3);
    }

    @Test
    void testUpdate_NonExistingBoard_ThrowsException() throws BoardNotFoundExceptionTrello {
        when(boardRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(BoardNotFoundExceptionTrello.class, () -> boardService.update(MOCK_BOARD_3, 3L));

        verify(boardRepository, times(1)).findById(3L);
        verify(boardRepository, never()).save(Mockito.any());
    }

    @Test
    void testDelete_ExistingBoard_ShouldReturnTrue() {
        when(boardRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(MOCK_BOARD_3));

        boolean result = boardService.delete(3L);

        assertTrue(result);
        verify(boardRepository, times(1)).findById(3L);
        verify(boardRepository, times(1)).delete(MOCK_BOARD_3);
        verify(boardRepository, times(1)).flush();
    }

    @Test
    void testDelete_NonExistingBoard_ShouldReturnFalse() {
        when(boardRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        boolean result = boardService.delete(3L);

        assertFalse(result);
        verify(boardRepository, times(1)).findById(3L);
        verify(boardRepository, never()).delete(Mockito.any());
        verify(boardRepository, never()).flush();
    }
}




