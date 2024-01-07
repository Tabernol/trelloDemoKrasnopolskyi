package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskCreateRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskEditRequest;
import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadResponse;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Status;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.TaskOrder;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.IncorrectStatusChangeExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TaskNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.TaskMapper;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.TaskOrderingRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.TaskRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.TaskOrderingService;
import com.krasnopolskyi.trellodemokrasnopolskyi.utils.TaskUtils;
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
    @Autowired
    private TaskMapper taskMapper;

    private TaskUtils taskUtils;

    @InjectMocks
    private TaskServiceImpl taskService;
    private Task testTask;
    private TaskEditRequest taskEditRequest;
    private TaskCreateRequest taskCreateRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testTask = Task.builder().id(1L).name("Test Task").column(Column.builder().id(1L).build()).build();
        taskEditRequest = TaskEditRequest.builder().name("Updated Task").build();
        taskCreateRequest = TaskCreateRequest.builder().name("New Task").columnId(1L).build();
        taskOrderingService =
                new TaskOrderingServiceImpl(taskOrderingRepository, taskRepository, columnRepository, taskMapper);
        taskService =
                new TaskServiceImpl(taskRepository, taskOrderingService, columnRepository, taskMapper, taskUtils);

    }

    @Test
    void testFindById_ExistingTask_ShouldReturnTask() throws TaskNotFoundExceptionTrello {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        // Act
        TaskReadResponse resultTask = taskService.findById(1L);

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
        List<TaskReadResponse> resultTasks = taskService.findAll();

        // Assert
        assertFalse(resultTasks.isEmpty());
        assertEquals(mockTasks.size(), resultTasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testCreate_ShouldReturnCreatedTask() throws ColumnNotFoundExceptionTrello {
        TaskOrder taskOrder = TaskOrder.builder().taskId(1L).columnId(1L).orderIndex(1).build();
        // Arrange
        when(taskRepository.saveAndFlush(any(Task.class))).thenReturn(testTask);
        when(taskRepository.findAllByColumn(1L)).thenReturn(Arrays.asList(testTask));
        when(taskOrderingRepository.save(any(TaskOrder.class))).thenReturn(taskOrder);
        when(columnRepository.findById(1L)).thenReturn(Optional.of(Column.builder().id(1L).build()));

        // Act
        TaskReadResponse createdTask = taskService.create(taskCreateRequest);

        // Assert
        assertNotNull(createdTask);
        verify(taskRepository, times(1)).saveAndFlush(any(Task.class));
        verify(taskRepository, times(1)).findAllByColumn(1L);
        verify(taskOrderingRepository, times(1)).save(taskOrder);
    }

    @Test
    void testUpdate_ExistingTask_ShouldReturnUpdatedTask()
            throws ColumnNotFoundExceptionTrello,
            TaskNotFoundExceptionTrello,
            IncorrectStatusChangeExceptionTrello {
        // Arrange
        Long taskId = 1L;
        Task updatedTask = Task.builder()
                .name("Updated name")
                .description("updated description")
                .column(Column.builder().id(1L).build())
                .build();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        // Act
        TaskReadResponse resultTask = taskService.update(taskEditRequest, taskId);

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
        assertThrows(TaskNotFoundExceptionTrello.class, () -> taskService.update(taskEditRequest, taskId));
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
