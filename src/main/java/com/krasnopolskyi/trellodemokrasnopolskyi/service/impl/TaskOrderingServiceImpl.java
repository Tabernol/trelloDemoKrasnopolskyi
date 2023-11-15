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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskOrderingServiceImpl implements TaskOrderingService {
    private final TaskOrderingRepository taskOrderingRepository;
    private final TaskRepository taskRepository;
    private final ColumnRepository columnRepository;

    public TaskOrderingServiceImpl(TaskOrderingRepository taskOrderingRepository,
                                   TaskRepository taskRepository,
                                   ColumnRepository columnRepository) {
        this.taskOrderingRepository = taskOrderingRepository;
        this.taskRepository = taskRepository;
        this.columnRepository = columnRepository;
    }


    @Override
    public TaskOrder insert(Task task) {
        int size = taskRepository.findAllByColumn(task.getColumn().getId()).size();
        TaskOrder taskOrder = TaskOrder.builder()
                .taskId(task.getId())
                .columnId(task.getColumn().getId())
                .orderIndex(size)
                .build();
        return taskOrderingRepository.save(taskOrder);
    }

    @Override
    public List<Task> findAllByColumnByUserOrder(Long columnId) {
        List<Task> listOfTask = taskRepository.findAllByColumn(columnId);// none sorted
        log.info(listOfTask.toString());
        List<Long> userOrder = findAllIdTasksByColumnInUserOrder(columnId);//only id of Task in user order
        log.info(userOrder.toString());
        Map<Long, Task> tasks = listOfTask
                .stream()
                .collect(Collectors.toMap((Task::getId), Function.identity()));

        List<Task> sortedTasks = userOrder
                .stream()
                .map(tasks::get)
                .collect(Collectors.toList());

        return sortedTasks;
    }

//    @Override
//    public TaskOrder insert(Task task) {
//        int size = taskRepository.findAllByColumn(task.getColumnId()).size();
//        TaskOrder taskOrder = TaskOrder.builder()
//                .taskId(task.getId())
//                .columnId(task.getColumnId())
//                .orderIndex(size)
//                .build();
//        return taskOrderRepository.save(taskOrder);
//    }


    @Override
    public List<Long> findAllIdTasksByColumnInUserOrder(Long columnId) {
        return taskOrderingRepository.findAllByColumnIdOrderByOrderIndex(columnId)
                .stream()
                .map(taskOrder -> taskOrder.getTaskId())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int moveTask(TaskOrder taskOrder) {
        Task task = getTask(taskOrder);
        Long sourceColumnId = task.getColumn().getId();
        Long targetColumnId = taskOrder.getColumnId();

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

    private Task getTask(TaskOrder taskOrder) {
        return taskRepository.findById(taskOrder.getTaskId())
                .orElseThrow(() -> new TaskNotFoundExceptionTrello("Task with id " + taskOrder.getTaskId() + " not found"));

    }

    private void updateOrderIndexesAndSave(List<TaskOrder> orderedTasks) {
        for (int i = 0; i < orderedTasks.size(); i++) {
            orderedTasks.get(i).setOrderIndex(i + 1);
        }
        List<TaskOrder> taskOrders = taskOrderingRepository.saveAllAndFlush(orderedTasks);
        log.info(taskOrders.toString());
    }

    private void saveTaskWithAnotherColumn(Task task, Long columnId) {
        Column column = columnRepository.findById(columnId).orElseThrow(
                () -> new ColumnNotFoundExceptionTrello("Column with id " + columnId + " not found"));

        task.setColumn(column);
        taskRepository.saveAndFlush(task);
    }

    private List<TaskOrder> getTasksByColumnId(Long columnId) {
        return new LinkedList<>(taskOrderingRepository
                .findAllByColumnIdOrderByOrderIndex(columnId));
    }

    private int reorder(List<TaskOrder> currentColumn, TaskOrder taskOrder) {
        //only change order in column
        taskOrder.setOrderIndex(getLastIndex(currentColumn, taskOrder));
        currentColumn.add(taskOrder.getOrderIndex() - 1, taskOrder);
        updateOrderIndexesAndSave(currentColumn);
        return currentColumn.size();
    }

    private int moveToAnotherColumn(TaskOrder taskOrder) {
        Task task = getTask(taskOrder);
        saveTaskWithAnotherColumn(task, taskOrder.getColumnId());

        List<TaskOrder> targetColumn = getTasksByColumnId(taskOrder.getColumnId());

        taskOrder.setOrderIndex(getLastIndex(targetColumn, taskOrder));
        targetColumn.add(taskOrder.getOrderIndex() - 1, taskOrder);
        updateOrderIndexesAndSave(targetColumn);
        return targetColumn.size();
    }


    private int getLastIndex(List<TaskOrder> taskOrderList, TaskOrder taskOrder) {
        if (taskOrder.getOrderIndex() > taskOrderList.size()) {
            log.info("change orderIndex to = " + (taskOrderList.size() + 1));
            return taskOrderList.size() + 1;
        } else {
            return taskOrder.getOrderIndex();
        }
    }
}