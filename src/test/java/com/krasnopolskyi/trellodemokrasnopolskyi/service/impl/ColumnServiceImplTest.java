package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.ColumnOrder;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.BoardNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.ColumnMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.BoardRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnOrderingRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnOrderingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ColumnServiceImplTest {
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private ColumnRepository columnRepository;
    @Mock
    private ColumnOrderingRepository columnOrderingRepository;
    @Mock
    private ColumnOrderingService columnOrderingService;
    @Autowired
    private ColumnMapper columnMapper;
    @InjectMocks
    private ColumnServiceImpl columnService;
    private Column testColumn;
    private ColumnCreateRequest columnCreateRequest;
    private ColumnEditRequest columnEditRequest;

    private ColumnReadResponse columnReadResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testColumn = Column.builder().id(1L).name("Test Column").board(Board.builder().id(1L).build()).build();
        columnCreateRequest = ColumnCreateRequest.builder().name("Test Column").boardId(1L).build();
        columnEditRequest = ColumnEditRequest.builder().name("Test Column").build();
        columnReadResponse = ColumnReadResponse.builder().id(1L).name("Test Column").boardId(1L).build();
        columnOrderingService = new ColumnOrderingServiceImpl(columnOrderingRepository, columnRepository, columnMapper);
        columnService = new ColumnServiceImpl(boardRepository, columnRepository, columnOrderingService, columnMapper);
    }

    @Test
    void testFindById_ExistingColumn_ShouldReturnColumn() throws ColumnNotFoundExceptionTrello {
        // Arrange
        when(columnRepository.findById(1L)).thenReturn(Optional.of(testColumn));

        // Act
        ColumnReadResponse resultColumn = columnService.findById(1L);

        // Assert
        assertNotNull(resultColumn);
        assertEquals(1L, resultColumn.getId());
        assertEquals("Test Column", resultColumn.getName());
        verify(columnRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NonExistingColumn_ShouldThrowException() {
        // Arrange
        when(columnRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ColumnNotFoundExceptionTrello.class, () -> columnService.findById(1L));
        verify(columnRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAll_ShouldReturnListOfColumns() {
        // Arrange
        when(columnRepository.findAll()).thenReturn(Collections.singletonList(testColumn));

        // Act
        List<ColumnReadResponse> resultColumns = columnService.findAll();

        // Assert
        assertFalse(resultColumns.isEmpty());
        assertEquals(1, resultColumns.size());
        assertEquals(columnReadResponse, resultColumns.get(0));
        verify(columnRepository, times(1)).findAll();
    }

    @Test
    void testCreate_ValidColumn_ShouldReturnCreatedColumn() throws BoardNotFoundExceptionTrello {
        ColumnOrder columnOrder = ColumnOrder.builder().columnId(1L).boardId(1L).orderIndex(1).build();
        // Arrange
        when(boardRepository.findById(1L)).thenReturn(Optional.of(Board.builder().id(1L).build()));
        when(columnRepository.saveAndFlush(any(Column.class))).thenReturn(testColumn);
        when(columnRepository.findAllByBoard(1L)).thenReturn(Arrays.asList(testColumn));
        when(columnOrderingRepository.saveAndFlush(any(ColumnOrder.class))).thenReturn(columnOrder);

        // Act
        ColumnReadResponse createdColumn = columnService.create(columnCreateRequest);

        // Assert
        assertNotNull(createdColumn);
        verify(columnRepository, times(1)).saveAndFlush(any(Column.class));
        verify(columnRepository, times(1)).findAllByBoard(1L);
        verify(columnOrderingRepository, times(1)).saveAndFlush(columnOrder);
    }

    @Test
    void testCreate_InvalidBoard_ShouldThrowException() throws BoardNotFoundExceptionTrello {
        // Arrange
        when(boardRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BoardNotFoundExceptionTrello.class, () -> columnService.create(columnCreateRequest));
    }


    @Test
    void testUpdate_ExistingColumn_ShouldReturnUpdatedColumn() throws ColumnNotFoundExceptionTrello {
        // Arrange
        Column updatedColumn = Column.builder().id(1L).board(Board.builder().id(1L).build()).name("Updated Column").build();
        when(columnRepository.findById(1L)).thenReturn(Optional.of(testColumn));
        when(columnRepository.save(testColumn)).thenReturn(updatedColumn);


        // Act
        ColumnReadResponse resultColumn = columnService.update(columnEditRequest, 1L);

        // Assert
        assertNotNull(resultColumn);
        assertEquals(1L, resultColumn.getId());
        assertEquals("Updated Column", resultColumn.getName());
        verify(columnRepository, times(1)).findById(1L);
        verify(columnRepository, times(1)).save(any(Column.class));
    }

    @Test
    void testUpdate_NonExistingColumn_ShouldThrowException() {
        // Arrange
        Column updatedColumn = Column.builder().id(2L).name("Updated Column").build();
        when(columnRepository.findById(2L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ColumnNotFoundExceptionTrello.class, () -> columnService.update(columnEditRequest, 2L));
        verify(columnRepository, times(1)).findById(2L);
        verify(columnRepository, never()).save(any(Column.class));
    }

    @Test
    void testDelete_ExistingColumn_ShouldReturnTrue() {
        // Arrange
        when(columnRepository.findById(1L)).thenReturn(Optional.of(testColumn));

        // Act
        boolean result = columnService.delete(1L);

        // Assert
        assertTrue(result);
        verify(columnRepository, times(1)).findById(1L);
        verify(columnRepository, times(1)).delete(testColumn);
        verify(columnRepository, times(1)).flush();
    }

    @Test
    void testDelete_NonExistingColumn_ShouldReturnFalse() {
        // Arrange
        when(columnRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        boolean result = columnService.delete(1L);

        // Assert
        assertFalse(result);
        verify(columnRepository, times(1)).findById(1L);
        verify(columnRepository, never()).delete(any(Column.class));
        verify(columnRepository, never()).flush();
    }
}
