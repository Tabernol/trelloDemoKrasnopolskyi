package com.krasnopolskyi.trellodemokrasnopolskyi.http.handler;

import com.krasnopolskyi.trellodemokrasnopolskyi.exception.ProhibitionMovingException;
import com.krasnopolskyi.trellodemokrasnopolskyi.exception.TrelloException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for handling various exceptions that may occur during API requests.
 * @author Maksym Krasnopolskyi
 */
@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Error message for validation errors.
     */
    private static final String VALIDATION_ERROR_MESSAGE = "Validation error. Check 'errors' field for details.";

    /**
     * Generic internal server error message.
     */
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Sorry, but something went wrong. Try again later";

    /**
     * Handles validation errors and builds a response with detailed error content.
     *
     * @param ex      The exception containing validation errors.
     * @param headers The headers for the response.
     * @param status  The HTTP status code for the response.
     * @param request The current web request.
     * @return ResponseEntity with a detailed error response for validation errors.
     */
    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        ErrorResponse errorResponse =
                new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), VALIDATION_ERROR_MESSAGE);
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addErrorContent(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handles unknown exceptions and builds a response with a generic internal server error message.
     *
     * @param exception The unknown exception.
     * @param request   The current web request.
     * @return ResponseEntity with a generic internal server error response.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(Exception exception, WebRequest request) {
        log.error("Unknown error occurred", exception);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                INTERNAL_SERVER_ERROR_MESSAGE);
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    /**
     * Handles ProhibitionMovingException and builds a response with an appropriate status code and message.
     *
     * @param exception The ProhibitionMovingException.
     * @param request   The current web request.
     * @return ResponseEntity with a response for ProhibitionMovingException.
     */
    @ExceptionHandler(ProhibitionMovingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleProhibitionMovingException(
            TrelloException exception, WebRequest request) {
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Handles TrelloException and builds a response with an appropriate status code and message.
     *
     * @param itemNotFoundException The TrelloException.
     * @param request               The current web request.
     * @return ResponseEntity with a response for TrelloException.
     */
    @ExceptionHandler(TrelloException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNoSuchElementFoundException(
            TrelloException itemNotFoundException, WebRequest request) {
        log.error("Failed to find the requested entity check passed id", itemNotFoundException);
        return buildErrorResponse(itemNotFoundException, HttpStatus.NOT_FOUND, request);
    }

    /**
     * Builds a generic error response based on the provided exception, HTTP status, and web request.
     *
     * @param exception The exception.
     * @param httpStatus The HTTP status code for the response.
     * @param request The current web request.
     * @return ResponseEntity with a generic error response.
     */
    protected ResponseEntity<Object> buildErrorResponse(Exception exception,
                                                        HttpStatus httpStatus,
                                                        WebRequest request) {
        return ResponseEntity.status(httpStatus).body(
                new ErrorResponse(httpStatus.value(), exception.getMessage()));
    }
}
