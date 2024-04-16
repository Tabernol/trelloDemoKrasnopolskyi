//package com.krasnopolskyi.trellodemokrasnopolskyi.utils;
//
//import com.krasnopolskyi.trellodemokrasnopolskyi.dto.column_dto.ColumnReadResponse;
//import com.krasnopolskyi.trellodemokrasnopolskyi.dto.task_dto.TaskReadResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//@Component
//public class TaskManagerImpl implements TaskManager {
//    private final RestTemplate restTemplate;
//
//    public TaskManagerImpl(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    @Override
//    public String getStatus(Long taskId) {
//        return restTemplate.exchange(
//                "http://localhost:8080/api/v1/tasks/status/{id}", HttpMethod.GET, HttpEntity.EMPTY,
//                String.class, taskId).getBody();
//    }
//}
