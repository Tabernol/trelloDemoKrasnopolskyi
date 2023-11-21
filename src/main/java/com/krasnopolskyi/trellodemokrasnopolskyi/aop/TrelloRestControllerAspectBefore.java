package com.krasnopolskyi.trellodemokrasnopolskyi.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j(topic = "TRELLO_REST_CONTROLLER_ASPECT_BEFORE")
public class TrelloRestControllerAspectBefore {
    //log before any GetMapping in RestController
    @Before(value = "com.krasnopolskyi.trellodemokrasnopolskyi.aop.CommonPointcuts.isRestController() " +
            "&& @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "&& args(id) " +
            "&& target(service)")
    public void logGetByIdMethod(Object id, Object service) {
        log.info("{} GetMapping method invoked with id = {}", service, id);
    }

    //log before any PostMapping method in RestController
    @Before(value = "com.krasnopolskyi.trellodemokrasnopolskyi.aop.CommonPointcuts.isRestController() " +
            "&& @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "&& args(dto) " +
            "&& target(service)")
    public void logPostMethod(Object service, Object dto) {
        log.info("{} PostMapping method invoked with input: {}", service, dto);
    }
    //log before any DeleteMapping method in RestController
    @Before(value = "com.krasnopolskyi.trellodemokrasnopolskyi.aop.CommonPointcuts.isRestController() " +
            "&& @annotation(org.springframework.web.bind.annotation.DeleteMapping) " +
            "&& args(id) " +
            "&& target(service)")
    public void logDeleteByIdMethod(Object service, Object id) {
        log.info("{} DeleteMapping method invoked wit id {}", service, id);
    }
    //log before any PutMapping method in RestController  with args(id, dto)
    @Before(value = "com.krasnopolskyi.trellodemokrasnopolskyi.aop.CommonPointcuts.isRestController() " +
            "&& @annotation(org.springframework.web.bind.annotation.PutMapping) " +
            "&& args(id, dto) " +
            "&& target(service)")
    public void logDeleteByIdMethod(Object service, Object id, Object dto) {
        log.info("{} PutMapping method invoked wit id = {}. and body = {}", service, id, dto);
    }


}
