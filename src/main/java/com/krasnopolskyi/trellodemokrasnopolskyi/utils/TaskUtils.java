//package com.krasnopolskyi.trellodemokrasnopolskyi.utils;
//
//import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskEditRequest;
//import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadResponse;
//import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Status;
//import com.krasnopolskyi.trellodemokrasnopolskyi.entity.Task;
//import com.krasnopolskyi.trellodemokrasnopolskyi.exception.IncorrectStatusChangeExceptionTrello;
//import com.krasnopolskyi.trellodemokrasnopolskyi.exception.StatusNotFoundExceptionTrello;
//import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TaskNotFoundExceptionTrello;
//import com.krasnopolskyi.trellodemokrasnopolskyi.mapper.TaskMapper;
//import com.krasnopolskyi.trellodemokrasnopolskyi.repository.TaskRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//import java.util.Random;
//
//@Component
//@Slf4j
//@EnableScheduling
//public class TaskUtils {
//    private final TaskRepository taskRepository;
//    private final TaskManager taskManager;
//    private final Random random;
//
//    public TaskUtils(TaskRepository taskRepository,
//                     TaskManager taskManager) {
//        this.taskRepository = taskRepository;
//        this.taskManager = taskManager;
//        this.random = new Random();
//    }
//
//    public static void checkUpdatingStatus(Task existingTask, TaskEditRequest taskEditRequest)
//            throws IncorrectStatusChangeExceptionTrello {
//        if (existingTask.getStatus().getOrder() > taskEditRequest.getStatus().getOrder()) {
//            throw new IncorrectStatusChangeExceptionTrello("You can't change status below "
//                    + existingTask.getStatus().name());
//        }
//    }
//
//    public Status getRandomStatus(Status status) throws StatusNotFoundExceptionTrello {
//        int randomOrder = random.nextInt(status.getOrder(),
//                Status.FAILED.getOrder() + 1);
//        return Status.getStatus(randomOrder);
//    }
//
//
////    @Scheduled(fixedRate = 5000)
//    public void setRandomStatusToRandoTask() {
//        taskRepository.findAllActiveTask()
//                .forEach(task -> {
//                    try {
//                        String status = taskManager.getStatus(task.getId());
//                        if (task.getStatus() != Status.valueOf(status)) {
//                            log.info("Task id: " + task.getId() + " status before: "
//                                    + task.getStatus().name());
//                            task.setStatus(Status.valueOf(status));
//                            taskRepository.save(task);
//                            log.info("Task id: " + task.getId() + " status after: "
//                                    + task.getStatus().name());
//                        }
//                    } catch (Exception e) {
//                        log.warn("Error from check status task " + task, e);
//                    }
//                });
//    }
//}
