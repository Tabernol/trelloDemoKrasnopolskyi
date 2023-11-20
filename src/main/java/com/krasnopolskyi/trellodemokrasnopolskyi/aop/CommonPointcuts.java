package com.krasnopolskyi.trellodemokrasnopolskyi.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CommonPointcuts {

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void isRestController() {
    }
}
