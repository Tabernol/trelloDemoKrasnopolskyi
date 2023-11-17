package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.BoardNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.BoardRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnOrderingRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.ColumnOrderingService;
import com.krasnopolskyi.trellodemokrasnopolskyi.validator.BoardValidator;
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
public class ColumnServiceImplTest {
    @Mock
    private BoardValidator boardValidator;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private ColumnRepository columnRepository;
    @Mock
    private ColumnOrderingRepository columnOrderingRepository;
    @Mock
    private ColumnOrderingService columnOrderingService;
    @InjectMocks
    private ColumnServiceImpl columnService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        boardValidator = new BoardValidator(boardRepository);
        columnOrderingService = new ColumnOrderingServiceImpl(columnOrderingRepository, columnRepository);
        columnService = new ColumnServiceImpl(boardValidator, columnRepository, columnOrderingService);
    }

    @Test
    void testFindById_ExistingColumn_ShouldReturnColumn() throws ColumnNotFoundExceptionTrello {
        Column mockColumn = new Column();
        mockColumn.setId(1L);
        mockColumn.setName("Test Column");

        when(columnRepository.findById(1L)).thenReturn(Optional.of(mockColumn));

        Column resultColumn = columnService.findById(1L);

        assertNotNull(resultColumn);
        assertEquals(1L, resultColumn.getId());
        assertEquals("Test Column", resultColumn.getName());
        verify(columnRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NonExistingColumn_ShouldThrowException() {
        when(columnRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ColumnNotFoundExceptionTrello.class, () -> columnService.findById(1L));
        verify(columnRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAll_ShouldReturnListOfColumns() {
        Column mockColumn = new Column();
        mockColumn.setId(1L);
        mockColumn.setName("Test Column");

        when(columnRepository.findAll()).thenReturn(Collections.singletonList(mockColumn));

        List<Column> resultColumns = columnService.findAll();

        assertFalse(resultColumns.isEmpty());
        assertEquals(1, resultColumns.size());
        assertEquals(mockColumn, resultColumns.get(0));
        verify(columnRepository, times(1)).findAll();
    }

//    @Test
//    void testCreate_ValidColumn_ShouldReturnCreatedColumn() throws BoardNotFoundExceptionTrello {
//
//    }
//
//
//    @Test
//    void testCreate_InvalidBoard_ShouldThrowException() throws BoardNotFoundExceptionTrello {
//
//    }


    @Test
    void testUpdate_ExistingColumn_ShouldReturnUpdatedColumn() throws ColumnNotFoundExceptionTrello {
        Column existingColumn = new Column();
        existingColumn.setId(1L);
        existingColumn.setName("Existing Column");

        Column updatedColumn = new Column();
        updatedColumn.setId(1L);
        updatedColumn.setName("Updated Column");

        when(columnRepository.findById(1L)).thenReturn(Optional.of(existingColumn));
        when(columnRepository.save(any(Column.class))).thenReturn(updatedColumn);


        Column resultColumn = columnService.update(updatedColumn, 1L);

        assertNotNull(resultColumn);
        assertEquals(1L, resultColumn.getId());
        assertEquals("Updated Column", resultColumn.getName());
        verify(columnRepository, times(1)).findById(1L);
        verify(columnRepository, times(1)).save(any(Column.class));
    }

    @Test
    void testUpdate_NonExistingColumn_ShouldThrowException() {
        Column updatedColumn = new Column();
        updatedColumn.setId(1L);
        updatedColumn.setName("Updated Column");

        when(columnRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ColumnNotFoundExceptionTrello.class, () -> columnService.update(updatedColumn, 1L));
        verify(columnRepository, times(1)).findById(1L);
        verify(columnRepository, never()).save(any(Column.class));
    }

    @Test
    void testDelete_ExistingColumn_ShouldReturnTrue() {
        Column existingColumn = new Column();
        existingColumn.setId(1L);
        existingColumn.setName("Existing Column");

        when(columnRepository.findById(1L)).thenReturn(Optional.of(existingColumn));

        boolean result = columnService.delete(1L);

        assertTrue(result);
        verify(columnRepository, times(1)).findById(1L);
        verify(columnRepository, times(1)).delete(existingColumn);
        verify(columnRepository, times(1)).flush();
    }

    @Test
    void testDelete_NonExistingColumn_ShouldReturnFalse() {
        when(columnRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = columnService.delete(1L);

        assertFalse(result);
        verify(columnRepository, times(1)).findById(1L);
        verify(columnRepository, never()).delete(any(Column.class));
        verify(columnRepository, never()).flush();
    }
}
