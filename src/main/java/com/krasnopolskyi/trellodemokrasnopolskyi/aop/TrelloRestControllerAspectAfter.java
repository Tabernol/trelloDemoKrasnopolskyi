package com.krasnopolskyi.trellodemokrasnopolskyi.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j(topic = "TRELLO_REST_CONTROLLER_ASPECT_AFTER")
public class TrelloRestControllerAspectAfter {
    //log after any GetMapping  method in RestController
    @AfterReturning(value = "com.krasnopolskyi.trellodemokrasnopolskyi.aop.CommonPointcuts.isRestController() " +
            "&& @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "&& target(service)",
            returning = "result")
    public void logAfterReturningGetMethod(Object result, Object service) {
        log.info("after returning - invoked findById method in class {}, result {}", service, result);
    }
    //log after any PostMapping method in RestController
    @AfterReturning(value = "com.krasnopolskyi.trellodemokrasnopolskyi.aop.CommonPointcuts.isRestController() " +
            "&& @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "&& target(service)",
            returning = "result")
    public void logAfterReturningPostMethod(Object result, Object service) {
        log.info("after returning - invoked create method in class {}, result {}", service, result);
    }
    //log after any PutMapping method in RestController
    @AfterReturning(value = "com.krasnopolskyi.trellodemokrasnopolskyi.aop.CommonPointcuts.isRestController() " +
            "&& @annotation(org.springframework.web.bind.annotation.PutMapping) " +
            "&& target(service)",
            returning = "result")
    public void logAfterReturningPutMethod(Object result, Object service) {
        log.info("after returning - invoked update method in class {}, result {}", service, result);
    }
    //log after any DeleteMapping method in RestController
    @AfterReturning(value = "com.krasnopolskyi.trellodemokrasnopolskyi.aop.CommonPointcuts.isRestController() " +
            "&& @annotation(org.springframework.web.bind.annotation.DeleteMapping) " +
            "&& target(service)",
            returning = "result")
    public void logAfterReturningDeleteMethod(Object result, Object service) {
        log.info("after returning - invoked update method in class {}, result {}", service, result);
    }
}
