package com.krasnopolskyi.trellodemokrasnopolskyi.exception;

public class TaskNotFoundExceptionTrello extends TrelloEntityNotFoundException {
    public TaskNotFoundExceptionTrello(String message) {
        super(message);
    }
}
