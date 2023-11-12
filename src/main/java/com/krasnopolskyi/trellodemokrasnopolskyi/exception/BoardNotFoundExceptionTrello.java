package com.krasnopolskyi.trellodemokrasnopolskyi.exception;

public class BoardNotFoundExceptionTrello extends TrelloEntityNotFoundException {
    public BoardNotFoundExceptionTrello(String message) {
        super(message);
    }
}
