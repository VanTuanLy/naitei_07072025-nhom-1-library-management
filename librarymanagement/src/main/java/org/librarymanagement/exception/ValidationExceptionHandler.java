package org.librarymanagement.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.librarymanagement.dto.response.NotEnoughBookErrorResponse;
import org.librarymanagement.dto.response.NotEnoughConditionsBorrow;
import org.librarymanagement.dto.response.ValidationErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ValidationExceptionHandler {
    // Xử lý validate exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String,String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage(),
                        (existing, replacement) -> existing
                ));

        return ValidationErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .error("Validation failed")
                .fieldErrors(fieldErrors)
                .build();
    }
    // Xử lý trùng dữ liệu
    @ExceptionHandler(DuplicateFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleDuplicateFieldException(DuplicateFieldException ex, HttpServletRequest request) {
        return ValidationErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .error("Duplicate field")
                .fieldErrors(ex.getFieldErrors())
                .build();
    }

    // Xu ly sach khong co
    @ExceptionHandler(NotEnoughBookException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public NotEnoughBookErrorResponse handleNotEnoughBookException(NotEnoughBookException ex)
    {
        return NotEnoughBookErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(ex.getMessage())
                .fieldErrors(ex.getFieldErrors())
                .build();
    }

    @ExceptionHandler(NotEnoughConditionsBorrowException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public NotEnoughConditionsBorrow handleNotEnoughBookException(NotEnoughConditionsBorrowException ex)
    {
        return NotEnoughConditionsBorrow.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(ex.getMessage())
                .fieldErrors(ex.getFieldErrors())
                .build();
    }
}
