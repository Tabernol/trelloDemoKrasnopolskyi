package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.TaskOrder;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TaskNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.TaskOrderingRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.TaskRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.TaskOrderingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskOrderingRepository taskOrderingRepository;
    @Mock
    private ColumnRepository columnRepository;
    @Mock
    private TaskOrderingService taskOrderingService;

    @InjectMocks
    private TaskServiceImpl taskService;
    private Task testTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testTask = Task.builder().id(1L).name("Test Task").column(Column.builder().id(1L).build()).build();
        taskOrderingService =
                new TaskOrderingServiceImpl(taskOrderingRepository, taskRepository, columnRepository);
        taskService =
                new TaskServiceImpl(taskRepository, taskOrderingService, columnRepository);

    }

    @Test
    void testFindById_ExistingTask_ShouldReturnTask() throws TaskNotFoundExceptionTrello {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        // Act
        Task resultTask = taskService.findById(1L);

        // Assert
        assertNotNull(resultTask);
        assertEquals(1L, resultTask.getId());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NonExistingTask_ShouldThrowException() {
        // Arrange;
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(TaskNotFoundExceptionTrello.class, () -> taskService.findById(1L));
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAll_ShouldReturnListOfTasks() {
        // Arrange
        List<Task> mockTasks = Arrays.asList(testTask);
        when(taskRepository.findAll()).thenReturn(mockTasks);

        // Act
        List<Task> resultTasks = taskService.findAll();

        // Assert
        assertFalse(resultTasks.isEmpty());
        assertEquals(mockTasks.size(), resultTasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testCreate_ShouldReturnCreatedTask() {
        TaskOrder taskOrder = TaskOrder.builder().taskId(1L).columnId(1L).orderIndex(1).build();
        // Arrange
        when(taskRepository.saveAndFlush(any(Task.class))).thenReturn(testTask);
        when(taskRepository.findAllByColumn(1L)).thenReturn(Arrays.asList(testTask));
        when(taskOrderingRepository.save(any(TaskOrder.class))).thenReturn(taskOrder);

        // Act
        Task createdTask = taskService.create(testTask);

        // Assert
        assertNotNull(createdTask);
        verify(taskRepository, times(1)).saveAndFlush(any(Task.class));
        verify(taskRepository, times(1)).findAllByColumn(1L);
        verify(taskOrderingRepository, times(1)).save(taskOrder);
    }

    @Test
    void testUpdate_ExistingTask_ShouldReturnUpdatedTask() throws ColumnNotFoundExceptionTrello, TaskNotFoundExceptionTrello {
        // Arrange
        Long taskId = 1L;
        Task updatedTask = Task.builder().name("Updated name").description("updated description").build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        // Act
        Task resultTask = taskService.update(updatedTask, taskId);

        // Assert
        assertNotNull(resultTask);
        assertEquals(updatedTask.getName(), resultTask.getName());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(any(Task.class));
    }


    @Test
    void testUpdate_NonExistingTask_ShouldThrowException() {
        // Arrange
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(TaskNotFoundExceptionTrello.class, () -> taskService.update(new Task(), taskId));
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void testDelete_ExistingTask_ShouldReturnTrue() {
        // Arrange
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setId(taskId);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        // Act
        boolean result = taskService.delete(taskId);

        // Assert
        assertTrue(result);
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).delete(existingTask);
        verify(taskRepository, times(1)).flush();
    }

    @Test
    void testDelete_NonExistingTask_ShouldReturnFalse() {
        // Arrange
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act
        boolean result = taskService.delete(taskId);

        // Assert
        assertFalse(result);
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).delete(any(Task.class));
        verify(taskRepository, never()).flush();
    }
}
