package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Board;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.ColumnOrder;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnOrderingRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ColumnOrderingServiceImplTest {
    @Mock
    private ColumnOrderingRepository columnOrderingRepository;
    @Mock
    private ColumnRepository columnRepository;
    @InjectMocks
    private ColumnOrderingServiceImpl columnOrderingService;
    private Column column;
    private ColumnOrder columnOrder1;
    private ColumnOrder columnOrder2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        column = Column.builder().id(1L).name("Column 1").board(Board.builder().id(1L).build()).build();
        columnOrder1 = ColumnOrder.builder().columnId(1L).boardId(1L).orderIndex(1).build();
        columnOrder2 = ColumnOrder.builder().columnId(2L).boardId(1L).orderIndex(2).build();
        columnOrderingService = new ColumnOrderingServiceImpl(columnOrderingRepository, columnRepository);
    }

    @Test
    void testInsert_ShouldReturnColumnOrder() {
        // Arrange
        when(columnRepository.findAllByBoard(anyLong())).thenReturn(Collections.emptyList());
        when(columnOrderingRepository.saveAndFlush(any(ColumnOrder.class))).thenReturn(new ColumnOrder());

        // Act
        ColumnOrder result = columnOrderingService.insert(column);

        // Assert
        assertNotNull(result);
        verify(columnRepository, times(1)).findAllByBoard(anyLong());
        verify(columnOrderingRepository, times(1)).saveAndFlush(any(ColumnOrder.class));
    }

    @Test
    void testFindAllColumnsIdByBoardIdInUserOrder_ShouldReturnListOfLong() {
        // Arrange
        Long boardId = 1L;
        when(columnOrderingRepository.findAllByBoardIdOrderByOrderIndex(boardId))
                .thenReturn(Arrays.asList(columnOrder1, columnOrder2));

        // Act
        List<Long> result = columnOrderingService.findAllColumnsIdByBoardIdInUserOrder(boardId);

        // Assert
        assertNotNull(result);
        assertEquals(Arrays.asList(1L, 2L), result);
        verify(columnOrderingRepository, times(1)).findAllByBoardIdOrderByOrderIndex(boardId);
    }

    @Test
    void testFindAllColumnsByBoardInUserOrder_ShouldReturnListOfColumn() {
        Column column2 = Column.builder().id(2L).name("Column 2").build();
        // Arrange
        Long boardId = 1L;
        when(columnOrderingRepository.findAllByBoardIdOrderByOrderIndex(boardId))
                .thenReturn(Arrays.asList(columnOrder1, columnOrder2));

        when(columnRepository.findAllByBoard(boardId))
                .thenReturn(Arrays.asList(column, column2));


        // Act
        List<Column> result = columnOrderingService.findAllColumnsByBoardInUserOrder(boardId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Column 1", result.get(0).getName());
        assertEquals("Column 2", result.get(1).getName());
        verify(columnRepository, times(1)).findAllByBoard(boardId);
    }

    @Test
    void testReorder_ShouldUpdateOrderIndexesAndReturnSize() throws ColumnNotFoundExceptionTrello {
        // Arrange
        ColumnOrder columnOrder = new ColumnOrder(1L, 1L, 2);
        List<ColumnOrder> columnOrderList = Arrays.asList(
                columnOrder1, columnOrder2, new ColumnOrder(3L, 1L, 3)
        );

        Board board = Board.builder().id(1L).build();

        when(columnRepository.findById(anyLong())).thenReturn(Optional.of(Column.builder().board(board).build()));
        when(columnOrderingRepository.findAllByBoardIdOrderByOrderIndex(anyLong())).thenReturn(columnOrderList);
        when(columnOrderingRepository.saveAllAndFlush(anyList())).thenReturn(columnOrderList);

        // Act
        int result = columnOrderingService.reorder(columnOrder, 1L);

        // Assert
        assertEquals(columnOrderList.size(), result);
        verify(columnRepository, times(1)).findById(anyLong());
        verify(columnOrderingRepository, times(1)).findAllByBoardIdOrderByOrderIndex(anyLong());
        verify(columnOrderingRepository, times(1)).saveAllAndFlush(anyList());
    }

    @Test
    void testReorder_ShouldThrowException() throws ColumnNotFoundExceptionTrello {
        // Arrange
        when(columnRepository.findById(1L)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(ColumnNotFoundExceptionTrello.class, () -> columnOrderingService.reorder(columnOrder1, 1L));
    }
}
