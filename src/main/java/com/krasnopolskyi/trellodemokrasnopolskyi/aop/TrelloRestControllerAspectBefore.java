package com.krasnopolskyi.trellodemokrasnopolskyi.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j(topic = "TRELLO_REST_CONTROLLER_ASPECT_BEFORE")
public class TrelloRestControllerAspectBefore {

    @Before(value = "com.krasnopolskyi.trellodemokrasnopolskyi.aop.CommonPointcuts.isRestController() " +
            "&& @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "&& args(id) " +
            "&& target(service)")
    public void logGetByIdMethod(Object id, Object service) {
        log.info("{} GetMapping method invoked with id = {}", service, id);
    }


    @Before(value = "com.krasnopolskyi.trellodemokrasnopolskyi.aop.CommonPointcuts.isRestController() " +
            "&& @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "&& args(dto) " +
            "&& target(service)")
    public void logPostMethod(Object service, Object dto) {
        log.info("{} PostMapping method invoked with input: {}", service, dto);
    }

    @Before(value = "com.krasnopolskyi.trellodemokrasnopolskyi.aop.CommonPointcuts.isRestController() " +
            "&& @annotation(org.springframework.web.bind.annotation.DeleteMapping) " +
            "&& args(id) " +
            "&& target(service)")
    public void logDeleteByIdMethod(Object service, Object id) {
        log.info("{} DeleteMapping method invoked wit id {}", service, id);
    }

    @Before(value = "com.krasnopolskyi.trellodemokrasnopolskyi.aop.CommonPointcuts.isRestController() " +
            "&& @annotation(org.springframework.web.bind.annotation.PutMapping) " +
            "&& args(id, dto) " +
            "&& target(service)")
    public void logDeleteByIdMethod(Object service, Object id, Object dto) {
        log.info("{} PutMapping method invoked wit id = {}. and body = {}", service, id, dto);
    }




}
