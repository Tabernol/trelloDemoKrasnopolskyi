package com.krasnopolskyi.trellodemokrasnopolskyi.entity;

import com.krasnopolskyi.trellodemokrasnopolskyi.exception.StatusNotFoundExceptionTrello;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Status {
    CREATED(0),
    PENDING(1),
    IN_PROGRESS(2),
    IN_TESTING(3),
    COMPLETED(4),
    FAILED(5);
    private int order;

    private Status(int order) {
        this.order = order;
    }

    public static Status getStatus(int order) throws StatusNotFoundExceptionTrello {
        return Arrays.stream(Status.values())
                .filter(status -> status.getOrder() == order)
                .findFirst().orElseThrow(() ->
                        new StatusNotFoundExceptionTrello("Status not found with order " + order));
    }
}
