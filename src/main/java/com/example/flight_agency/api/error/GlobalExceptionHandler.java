package com.example.flight_agency.api.error;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiError> build(
            HttpStatus status,
            String message,
            HttpServletRequest req
    ) {
        ApiError body = ApiError.of(
                status,
                message,
                req.getRequestURI(),
                null
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler({EntityNotFoundException.class, NoSuchElementException.class, NotFoundException.class})
    public ResponseEntity<ApiError> handleNotFound(
            RuntimeException ex,
            HttpServletRequest req
    ) {
        return build(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                req
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(
            IllegalArgumentException ex,
            HttpServletRequest req
    ) {
        return build(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                req
        );
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ApiError> handleDateParseError(
            DateTimeParseException ex,
            HttpServletRequest req
    ) {
        String msg = "Formato de fecha inválido.";
        return build(
                HttpStatus.BAD_REQUEST,
                msg,
                req
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest req
    ) {
        List<ApiError.FieldViolation> violations = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ApiError.FieldViolation(
                        err.getField(),
                        err.getDefaultMessage()
                ))
                .toList();

        ApiError body = ApiError.of(
                HttpStatus.BAD_REQUEST,
                "Error de validación",
                req.getRequestURI(),
                violations
        );

        return ResponseEntity.badRequest().body(body);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleOther(
            Exception ex,
            HttpServletRequest req
    ) {
        String msg = (ex.getMessage() == null) ? "Error interno del servidor" : ex.getMessage();
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                msg,
                req
        );
    }

}
