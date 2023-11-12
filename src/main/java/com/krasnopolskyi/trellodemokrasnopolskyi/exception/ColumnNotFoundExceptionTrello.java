package com.krasnopolskyi.trellodemokrasnopolskyi.exception;

public class ColumnNotFoundExceptionTrello extends TrelloEntityNotFoundException {
    public ColumnNotFoundExceptionTrello(String message) {
        super(message);
    }
}
