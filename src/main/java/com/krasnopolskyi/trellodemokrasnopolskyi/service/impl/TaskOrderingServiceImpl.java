package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.TaskOrder;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ProhibitionMovingException;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TaskNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.TaskOrderingRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.TaskRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.TaskOrderingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service class that provides business logic for managing the ordering of tasks.
 * @author Maksym Krasnopolskyi
 */
@Service
@Slf4j
public class TaskOrderingServiceImpl implements TaskOrderingService {

    private final TaskOrderingRepository taskOrderingRepository;
    private final TaskRepository taskRepository;
    private final ColumnRepository columnRepository;

    /**
     * Constructs a new TaskOrderingServiceImpl with the given dependencies.
     *
     * @param taskOrderingRepository The repository for task ordering entities.
     * @param taskRepository         The repository for task entities.
     * @param columnRepository       The repository for column entities.
     */
    public TaskOrderingServiceImpl(TaskOrderingRepository taskOrderingRepository,
                                   TaskRepository taskRepository,
                                   ColumnRepository columnRepository) {
        this.taskOrderingRepository = taskOrderingRepository;
        this.taskRepository = taskRepository;
        this.columnRepository = columnRepository;
    }

    /**
     * Inserts a task order for the specified task.
     *
     * @param task The task for which to insert the order.
     * @return The created task order entity.
     */
    @Override
    @Transactional
    public TaskOrder insert(Task task) {
        int size = taskRepository.findAllByColumn(task.getColumn().getId()).size();
        TaskOrder taskOrder = TaskOrder.builder()
                .taskId(task.getId())
                .columnId(task.getColumn().getId())
                .orderIndex(size)
                .build();
        return taskOrderingRepository.save(taskOrder);
    }

    /**
     * Retrieves all tasks for a column in the user-defined order.
     *
     * @param columnId The ID of the column.
     * @return A list of tasks in the user-defined order for the specified column.
     */
    @Override
    public List<Task> findAllByColumnByUserOrder(Long columnId) {
        List<Task> listOfTask = taskRepository.findAllByColumn(columnId);// none sorted

        List<Long> userOrder = findAllIdTasksByColumnInUserOrder(columnId);//only id of Task in user order

        Map<Long, Task> tasks = listOfTask
                .stream()
                .collect(Collectors.toMap((Task::getId), Function.identity()));

        return userOrder
                .stream()
                .map(tasks::get)
                .collect(Collectors.toList());
    }

    /**
     * Moves a task within or between columns based on the provided task order.
     *
     * @param taskOrder The task order information.
     * @return The total number of tasks affected.
     * @throws TaskNotFoundExceptionTrello   If the associated task is not found.
     * @throws ColumnNotFoundExceptionTrello If the associated column is not found.
     */
    @Override
    @Transactional
    public int moveTask(TaskOrder taskOrder) throws TaskNotFoundExceptionTrello, ColumnNotFoundExceptionTrello, ProhibitionMovingException {
        Task task = getTask(taskOrder);
        Long sourceColumnId = task.getColumn().getId();
        Long targetColumnId = taskOrder.getColumnId();

        //if columnId == null that means method moves task within current column
        if (targetColumnId == null) {
            taskOrder.setColumnId(sourceColumnId);
        }

        List<TaskOrder> sourceColumn = getTasksByColumnId(sourceColumnId);
        sourceColumn.remove(taskOrder);

        if (Objects.equals(targetColumnId, sourceColumnId)) {
            //only change order in column
            return reorder(sourceColumn, taskOrder);
        } else {
            // move task to another column
            updateOrderIndexesAndSave(sourceColumn);
            int rowUpdated = moveToAnotherColumn(taskOrder);
            return sourceColumn.size() + rowUpdated;
        }
    }

    // Method returns list of IDs task which were sorted in user order
    private List<Long> findAllIdTasksByColumnInUserOrder(Long columnId) {
        return taskOrderingRepository.findAllByColumnIdOrderByOrderIndex(columnId)
                .stream()
                .map(taskOrder -> taskOrder.getTaskId())
                .collect(Collectors.toList());
    }

    //this method just return Task by id
    private Task getTask(TaskOrder taskOrder) throws TaskNotFoundExceptionTrello {
        return taskRepository.findById(taskOrder.getTaskId())
                .orElseThrow(() -> new TaskNotFoundExceptionTrello("Task with id " + taskOrder.getTaskId() + " not found"));
    }

    //this is important method which set new order indexes after insert task to list
    private void updateOrderIndexesAndSave(List<TaskOrder> orderedTasks) {
        for (int i = 0; i < orderedTasks.size(); i++) {
            orderedTasks.get(i).setOrderIndex(i + 1);
        }
        taskOrderingRepository.saveAllAndFlush(orderedTasks);
    }

    //this method change column in task-entity and save it.
    private void saveTaskWithAnotherColumn(Task task, Long columnId) throws ColumnNotFoundExceptionTrello, ProhibitionMovingException {
        Column column = columnRepository.findById(columnId).orElseThrow(
                () -> new ColumnNotFoundExceptionTrello("Column with id " + columnId + " not found"));

        // need check belong to board
        //====================================
        Long oldColumnId = task.getColumn().getId();
        Column oldColumn = columnRepository.findById(oldColumnId).orElseThrow(
                () -> new ColumnNotFoundExceptionTrello("Column with id " + oldColumnId + " not found"));

        if(!oldColumn.getBoard().getId().equals(column.getBoard().getId())){
            throw new ProhibitionMovingException(
                    "You can't move task to another board. Column id " + columnId +" is not valid for current board");
        }
        //===========================================
        task.setColumn(column);
        taskRepository.saveAndFlush(task);
    }

    // this method return dto(TaskOrder) by columnId but in natural order
    private List<TaskOrder> getTasksByColumnId(Long columnId) {
        return new LinkedList<>(taskOrderingRepository
                .findAllByColumnIdOrderByOrderIndex(columnId));
    }

    //this is complex method that move task within in the same column
    private int reorder(List<TaskOrder> currentColumn, TaskOrder taskOrder) {
        //only change order in column
        checkIndex(currentColumn, taskOrder);
        currentColumn.add(taskOrder.getOrderIndex() - 1, taskOrder);
        updateOrderIndexesAndSave(currentColumn);
        return currentColumn.size();
    }

    //it is a complex method that moves tasks to another column in the specified order.
    // But preserves the order of both columns in new order
    private int moveToAnotherColumn(TaskOrder taskOrder) throws TaskNotFoundExceptionTrello, ColumnNotFoundExceptionTrello, ProhibitionMovingException {
        Task task = getTask(taskOrder);
        saveTaskWithAnotherColumn(task, taskOrder.getColumnId());

        List<TaskOrder> targetColumn = getTasksByColumnId(taskOrder.getColumnId());

        checkIndex(targetColumn, taskOrder);
        targetColumn.add(taskOrder.getOrderIndex() - 1, taskOrder);
        updateOrderIndexesAndSave(targetColumn);
        return targetColumn.size();
    }

    //this method checks index which was passed.
    //if it is more than size of last index that index will set as last
    private void checkIndex(List<TaskOrder> taskOrderList, TaskOrder taskOrder) {
        if (taskOrder.getOrderIndex() > taskOrderList.size()) {
            taskOrder.setOrderIndex(taskOrderList.size() + 1);
        }
    }
}
