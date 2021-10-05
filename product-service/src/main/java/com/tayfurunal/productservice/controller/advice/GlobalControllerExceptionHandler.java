package com.tayfurunal.productservice.controller.advice;

import com.tayfurunal.productservice.exception.ProductServiceException;
import com.tayfurunal.productservice.exception.ProductServiceNotFoundException;
import com.tayfurunal.productservice.model.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerExceptionHandler {

    private static final Locale EN = new Locale("en");
    private final MessageSource messageSource;

    @ExceptionHandler(ProductServiceException.class)
    public ResponseEntity<ErrorResponse> handleProductServiceException(ProductServiceException exception) {
        final String defaultMessage = "ProductServiceException Occurred.";
        log.error(defaultMessage, exception);
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .exception("ProductServiceException")
                .error(messageSource.getMessage(exception.getKey(), exception.getArgs(), defaultMessage, EN))
                .timestamp(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(errorResponse, exception.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("MethodArgumentNotValidException occurred.", exception);
        final List<String> errorMessages = exception.getBindingResult().getFieldErrors().stream()
                .map(this::getMessage)
                .collect(Collectors.toList());
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .exception("MethodArgumentNotValidException")
                .errors(errorMessages)
                .timestamp(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductServiceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductServiceDomainNotFoundException(ProductServiceNotFoundException exception) {
        log.error("ProductServiceNotFoundException occurred.", exception);
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .exception("ProductServiceNotFoundException")
                .error(messageSource.getMessage(exception.getKey(), exception.getArgs(), EN))
                .timestamp(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handle(BindException exception) {
        log.error("BindException occurred.", exception);
        final List<String> errorMessages = exception.getBindingResult().getFieldErrors().stream()
                .map(this::getMessage)
                .collect(Collectors.toList());
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .exception("BindException")
                .errors(errorMessages)
                .timestamp(System.currentTimeMillis())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        log.error("ConstraintViolationException occurred.", exception);
        final List<String> errorMessages = exception.getConstraintViolations().stream()
                .map(violation -> this.getMessage(violation.getMessageTemplate(), violation.getInvalidValue()))
                .collect(Collectors.toList());
        ErrorResponse errorResponse = new ErrorResponse("ConstraintViolationException", System.currentTimeMillis(), errorMessages);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private String getMessage(FieldError error) {
        final String messageKey = error.getDefaultMessage();
        return messageSource.getMessage(messageKey, error.getArguments(), messageKey, EN);
    }

    private String getMessage(String messageKey, Object... args) {
        return messageSource.getMessage(messageKey, args, messageKey, EN);
    }
}
