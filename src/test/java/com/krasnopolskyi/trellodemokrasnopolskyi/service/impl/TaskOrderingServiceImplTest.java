package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.TaskOrder;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TaskNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.TaskOrderingRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class TaskOrderingServiceImplTest {
    @Mock
    private TaskOrderingRepository taskOrderingRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ColumnRepository columnRepository;
    @InjectMocks
    private TaskOrderingServiceImpl taskOrderingService;

    private Task task;
    private TaskOrder taskOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        task = Task.builder().id(1L).column(Column.builder().id(1L).build()).build();
        taskOrder = TaskOrder.builder().taskId(1L).columnId(1L).orderIndex(1).build();
        taskOrderingService =
                new TaskOrderingServiceImpl(taskOrderingRepository, taskRepository, columnRepository);
    }

    @Test
    void testInsertTask() {
        // Arrange
        when(taskRepository.findAllByColumn(1L)).thenReturn(Arrays.asList(task));
        when(taskOrderingRepository.save(any(TaskOrder.class))).thenReturn(taskOrder);

        // Act
        TaskOrder result = taskOrderingService.insert(task);

        // Assert
        assertEquals(taskOrder, result);
        verify(taskOrderingRepository, times(1)).save(any(TaskOrder.class));
    }

    @Test
    void testFindAllByColumnByUserOrder() {
        // Arrange
        Column column = Column.builder().id(1L).build();
        Task task1 = Task.builder().id(1L).name("Task 1").column(column).build();
        Task task2 = Task.builder().id(2L).name("Task 2").column(column).build();
        Task task3 = Task.builder().id(3L).name("Task 3").column(column).build();
        TaskOrder taskOrder1 = TaskOrder.builder().taskId(1L).columnId(1L).orderIndex(1).build();
        TaskOrder taskOrder2 = TaskOrder.builder().taskId(2L).columnId(1L).orderIndex(2).build();
        TaskOrder taskOrder3 = TaskOrder.builder().taskId(3L).columnId(1L).orderIndex(3).build();
        List<Task> allTasks = Arrays.asList(task1, task2, task3);
        List<TaskOrder> taskOrderings = Arrays.asList(taskOrder1, taskOrder2, taskOrder3);

        when(taskRepository.findAllByColumn(1L)).thenReturn(allTasks);
        when(taskOrderingRepository.findAllByColumnIdOrderByOrderIndex(1L)).thenReturn(taskOrderings);

        // Act
        List<Task> result = taskOrderingService.findAllByColumnByUserOrder(1L);

        // Assert
        verify(taskRepository, times(1)).findAllByColumn(1L);
        verify(taskOrderingRepository, times(1)).findAllByColumnIdOrderByOrderIndex(1L);

        List<Long> expectedOrder = Arrays.asList(1L, 2L, 3L);

        assertEquals(expectedOrder, result.stream().map(Task::getId).collect(Collectors.toList()));
    }

    @Test
    void testMoveTaskWithinSameColumn() throws TaskNotFoundExceptionTrello, ColumnNotFoundExceptionTrello {
        // Arrange
        List<TaskOrder> taskOrders = Arrays.asList(taskOrder);

        when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(task));
        when(taskOrderingRepository
                .findAllByColumnIdOrderByOrderIndex(1L)).thenReturn(taskOrders);

        // Act
        int result = taskOrderingService.moveTask(taskOrder);

        // Assert
        assertEquals(1, result);
        verify(taskOrderingRepository, times(1)).saveAllAndFlush(anyList());
    }

    @Test
    void testMoveTaskToDifferentColumn_ShouldMoveTaskToAnotherColumn() throws TaskNotFoundExceptionTrello, ColumnNotFoundExceptionTrello {
        // Arrange
        TaskOrder taskOrderToMove = TaskOrder.builder().taskId(1L).columnId(2L).orderIndex(1).build();
        List<TaskOrder> sourceColumn = Arrays.asList(taskOrderToMove);
        List<TaskOrder> targetColumn = Arrays.asList();

        when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(task));
        when(taskOrderingRepository
                .findAllByColumnIdOrderByOrderIndex(1L)).thenReturn(sourceColumn);
        when(taskOrderingRepository
                .findAllByColumnIdOrderByOrderIndex(2L)).thenReturn(targetColumn);

        when(columnRepository.findById(Mockito.anyLong()))
                .thenReturn(java.util.Optional.of(Column.builder().id(2L).build()));

        // Act
        int result = taskOrderingService.moveTask(taskOrderToMove);

        // Assert
        assertEquals(1, result);
        verify(taskOrderingRepository, times(2)).saveAllAndFlush(anyList());
        verify(taskRepository, times(1)).saveAndFlush(any(Task.class));
    }

    @Test
    void testMoveTaskWithinSameColumn_ShouldThrowTaskNotFoundExceptionTrello() {
        // Arrange
        when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TaskNotFoundExceptionTrello.class, () -> taskOrderingService.moveTask(taskOrder));
        verify(taskOrderingRepository, never()).saveAllAndFlush(anyList());
    }

    @Test
    void testMoveTaskToDifferentColumn_ShouldThrowColumnNotFoundExceptionTrello() {
        // Arrange
        TaskOrder taskOrderToMove = TaskOrder.builder().taskId(1L).columnId(2L).orderIndex(1).build();
        List<TaskOrder> sourceColumn = Arrays.asList(taskOrderToMove);

        when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(task));
        when(taskOrderingRepository
                .findAllByColumnIdOrderByOrderIndex(Mockito.anyLong())).thenReturn(sourceColumn);
        when(columnRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        verify(taskOrderingRepository, never()).saveAllAndFlush(anyList());
        assertThrows(ColumnNotFoundExceptionTrello.class, () -> taskOrderingService.moveTask(taskOrderToMove));
    }

}
