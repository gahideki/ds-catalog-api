package com.dscatalog.controller.error;

import com.dscatalog.service.exception.DatabaseException;
import com.dscatalog.service.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;

        StandardError standardError = new StandardError();
        standardError.setStatus(notFound.value());
        standardError.setTimestamp(LocalDateTime.now());
        standardError.setError("Resource not found");
        standardError.setMessage(ex.getMessage());
        standardError.setPath(request.getRequestURI());
        return ResponseEntity.status(notFound).body(standardError);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> database(DatabaseException ex, HttpServletRequest request) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        StandardError standardError = new StandardError();
        standardError.setStatus(badRequest.value());
        standardError.setTimestamp(LocalDateTime.now());
        standardError.setError("Database exception");
        standardError.setMessage(ex.getMessage());
        standardError.setPath(request.getRequestURI());
        return ResponseEntity.status(badRequest).body(standardError);
    }

}
