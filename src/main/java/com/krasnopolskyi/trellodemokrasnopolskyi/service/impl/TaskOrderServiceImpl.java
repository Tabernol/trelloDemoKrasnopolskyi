package com.krasnopolskyi.trellodemokrasnopolskyi.service.impl;

import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Column;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
import com.krasnopolskyi.trellodemokrasnopolskyi.entity.TaskOrder;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ColumnNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TaskNotFoundExceptionTrello;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.ColumnRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.TaskOrderRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.repository.TaskRepository;
import com.krasnopolskyi.trellodemokrasnopolskyi.service.TaskOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskOrderServiceImpl implements TaskOrderService {
    private final TaskOrderRepository taskOrderRepository;
    private final TaskRepository taskRepository;

    private final ColumnRepository columnRepository;

    public TaskOrderServiceImpl(TaskOrderRepository taskOrderRepository, TaskRepository taskRepository, ColumnRepository columnRepository) {
        this.taskOrderRepository = taskOrderRepository;
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
        return taskOrderRepository.save(taskOrder);
    }

    @Override
    public List<Task> findAllByColumnByUserOrder(Long columnId) {
        List<Task> listOfTask = taskRepository.findAllByColumn(columnId);// none sorted
        List<Long> userOrder = findAllIdTasksByColumnInUserOrder(columnId);//only id of Task in user order
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
        return taskOrderRepository.findAllByColumnIdOrderByOrderIndex(columnId)
                .stream()
                .map(taskOrder -> taskOrder.getTaskId())
                .collect(Collectors.toList());
    }

    @Override
    public int moveTask(TaskOrder taskOrder) {
        Task task = getTask(taskOrder);
        //if columnId == null
        if (taskOrder.getColumnId() == null) {
            taskOrder.setColumnId(task.getColumn().getId());
        }

        List<TaskOrder> sourceColumn =
                new LinkedList<>(taskOrderRepository.findAllByColumnIdOrderByOrderIndex(task.getColumn().getId()));

        sourceColumn.remove(taskOrder);


        if (Objects.equals(taskOrder.getColumnId(), task.getColumn().getId()) || taskOrder.getColumnId() == null) {
            //only change order in column
            sourceColumn.add(taskOrder.getOrderIndex() - 1, taskOrder);

            updateOrderIndexesAndSave(sourceColumn);

            return sourceColumn.size();
        } else {
            // move task to another column
            updateOrderIndexesAndSave(sourceColumn);
            log.info("========updating source column finished");

            saveTaskWithAnotherColumn(task, taskOrder.getColumnId());

            List<TaskOrder> targetColumn =
                    new LinkedList<>(taskOrderRepository.findAllByColumnIdOrderByOrderIndex(taskOrder.getColumnId()));
            targetColumn.add(taskOrder.getOrderIndex() - 1, taskOrder);
            updateOrderIndexesAndSave(targetColumn);
            return sourceColumn.size() + targetColumn.size();
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
        taskOrderRepository.saveAllAndFlush(orderedTasks);
    }

    private void saveTaskWithAnotherColumn(Task task, Long columnId) {
        Column column = columnRepository.findById(columnId).orElseThrow(
                () -> new ColumnNotFoundExceptionTrello("Column with id " + columnId + " not found"));

        task.setColumn(column);
        taskRepository.saveAndFlush(task);
    }


//    private void updateOrderIndexes(List<TaskOrder> orderList) {
//        for (int i = 0; i < orderList.size(); i++) {
//            orderList.get(i).setOrderIndex(i + 1);
//        }
//    }


}
