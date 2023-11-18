package com.krasnopolskyi.trellodemokrasnopolskyi.http.handler;

import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TrelloException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;


@RestControllerAdvice(basePackages = "com/krasnopolskyi/trellodemokrasnopolskyi/http/rest")
@Slf4j(topic = "TRELLO_EXCEPTION")
public class RestControllerExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler(TrelloException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNoSuchElementFoundException(
            TrelloException itemNotFoundException, WebRequest request) {
        log.error("Failed to find the requested entity check passed id", itemNotFoundException);
        return buildErrorResponse(itemNotFoundException, HttpStatus.NOT_FOUND, request);
    }
}

