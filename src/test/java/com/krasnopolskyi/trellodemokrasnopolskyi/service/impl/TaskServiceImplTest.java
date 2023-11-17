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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskOrderingService =
                new TaskOrderingServiceImpl(taskOrderingRepository, taskRepository, columnRepository);
        taskService =
                new TaskServiceImpl(taskRepository, taskOrderingService, columnRepository);

    }

    @Test
    void testFindById_ExistingTask_ShouldReturnTask() throws TaskNotFoundExceptionTrello {
        // Arrange
        Long taskId = 1L;
        Task mockTask = new Task();
        mockTask.setId(taskId);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(mockTask));

        // Act
        Task resultTask = taskService.findById(taskId);

        // Assert
        assertNotNull(resultTask);
        assertEquals(taskId, resultTask.getId());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void testFindById_NonExistingTask_ShouldThrowException() {
        // Arrange
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(TaskNotFoundExceptionTrello.class, () -> taskService.findById(taskId));
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void testFindAll_ShouldReturnListOfTasks() {
        // Arrange
        List<Task> mockTasks = Arrays.asList(new Task(), new Task());
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
        Column mockColumn = new Column();
        mockColumn.setId(1L);
        mockColumn.setName("Test Column");

        // Arrange
        Task mockTask = new Task();
        mockTask.setColumn(mockColumn);
        when(taskRepository.saveAndFlush(any(Task.class))).thenReturn(mockTask);
        when(taskOrderingService.insert(mockTask)).thenReturn(new TaskOrder());

        // Act
        Task createdTask = taskService.create(mockTask);

        // Assert
        assertNotNull(createdTask);
      //  verify(taskOrderingService, times(1)).insert(mockTask);
        verify(taskRepository, times(1)).saveAndFlush(any(Task.class));
    }

    @Test
    void testUpdate_ExistingTask_ShouldReturnUpdatedTask() throws ColumnNotFoundExceptionTrello, TaskNotFoundExceptionTrello {
        // Arrange
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setId(taskId);
        Task updatedTask = new Task();
        updatedTask.setName("Updated Task");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
//        when(columnRepository.findById(anyLong())).thenReturn(Optional.of(new Column()));
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
