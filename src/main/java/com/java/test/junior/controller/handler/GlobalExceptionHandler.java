package com.java.test.junior.controller.handler;

import com.java.test.junior.exception.ResourceConflictException;
import com.java.test.junior.exception.ResourceDeletedException;
import com.java.test.junior.exception.ResourceNotFoundException;
import com.java.test.junior.exception.ResourceValidationException;
import com.java.test.junior.model.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<?> handleException(ResourceConflictException ex) {
        logger.error(ex.toString());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ResourceValidationException.class)
    public ResponseEntity<?> handleException(ResourceValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ResourceDeletedException.class)
    public ResponseEntity<?> handleException(ResourceDeletedException ex) {
        return ResponseEntity.status(HttpStatus.GONE)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    //Pre-defined exceptions
    private String lastNodeName(Path path) {
        Path.Node last = null;
        for (Path.Node n : path) last = n;
        return last != null ? last.getName() : "param";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleException(MethodArgumentNotValidException ex) {
        FieldError fieldError = (FieldError) ex.getBindingResult().getAllErrors().getFirst();
        String field = fieldError.getField();
        String message = fieldError.getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(field + " field " + message)
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleException(ConstraintViolationException ex) {
        ConstraintViolation<?> violation = ex.getConstraintViolations().iterator().next();
        String paramName = lastNodeName(violation.getPropertyPath());
        String message = violation.getMessage();
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(paramName + " " + message));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("The request parameter " + ex.getName() + " has the wrong type"));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<?> handleException(HandlerMethodValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("There was an error validating the request"));
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<?> handleException(ServletException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handleException(DataAccessException ex) {
        logger.error(ex.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Database malfunction"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        logger.error(ex.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Internal Server Error"));
    }
}