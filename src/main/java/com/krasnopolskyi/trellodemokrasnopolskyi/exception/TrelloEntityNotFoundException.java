package com.krasnopolskyi.trellodemokrasnopolskyi.exception;

public class TrelloEntityNotFoundException extends RuntimeException {
    public TrelloEntityNotFoundException(String message) {
        super(message);
    }
}
