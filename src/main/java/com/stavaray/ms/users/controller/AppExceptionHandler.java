package com.stavaray.ms.users.controller;

import com.stavaray.ms.users.dto.error.ErrorDto;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AppExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> handleNotValidException(MethodArgumentNotValidException e) {

        List<FieldError> errs = e.getFieldErrors();

        List<ErrorDto> errors = errs.stream().map(f -> ErrorDto.builder()
                .fieldName(f.getField())
                .message(f.getDefaultMessage())
                .rejectedValue(f.getRejectedValue())
                .build()).toList();

        return ResponseEntity.badRequest().body(errors);

    }
}
