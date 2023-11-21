package com.krasnopolskyi.trellodemokrasnopolskyi.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j(topic = "TRELLO_REST_CONTROLLER_ASPECT_AFTER_THROWING")
public class TrelloRestControllerAspectAfterThrowing {
    //log afterThrowing any GetMapping method in RestController
    @AfterThrowing(value = "com.krasnopolskyi.trellodemokrasnopolskyi.aop.CommonPointcuts.isRestController() " +
            "&& @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "&& target(service)",
            throwing = "ex")
    public void logAfterThrowingGetMethod(Throwable ex, Object service) {
        log.warn("after throwing - invoked findById method in class {}, exception {}: {}", service, ex.getClass(), ex.getMessage());
    }
    //log afterThrowing any PostMapping method in RestController
    @AfterThrowing(value = "com.krasnopolskyi.trellodemokrasnopolskyi.aop.CommonPointcuts.isRestController() " +
            "&& @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "&& target(service)",
            throwing = "ex")
    public void logAfterThrowingPostMethod(Throwable ex, Object service) {
        log.warn("after throwing - invoked create method in class {}, exception {}: {}", service, ex.getClass(), ex.getMessage());
    }
    //log afterThrowing any PutMapping method in RestController
    @AfterThrowing(value = "com.krasnopolskyi.trellodemokrasnopolskyi.aop.CommonPointcuts.isRestController() " +
            "&& @annotation(org.springframework.web.bind.annotation.PutMapping) " +
            "&& target(service)",
            throwing = "ex")
    public void logAfterThrowingPutMethod(Throwable ex, Object service) {
        log.warn("after throwing - invoked update method in class {}, exception {}: {}", service, ex.getClass(), ex.getMessage());
    }
}
