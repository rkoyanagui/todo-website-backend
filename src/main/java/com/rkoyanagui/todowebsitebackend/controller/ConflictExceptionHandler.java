package com.rkoyanagui.todowebsitebackend.controller;

import com.rkoyanagui.todowebsitebackend.dto.ErrorDto;
import com.rkoyanagui.todowebsitebackend.exception.TodoItemPersistenceException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ConflictExceptionHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(TodoItemPersistenceException.class)
    protected ResponseEntity<Object> handleConflict(RuntimeException x, WebRequest request)
    {
        var body = ErrorDto.of(409, x.getMessage());
        return handleExceptionInternal(
                x,
                body,
                new HttpHeaders(),
                HttpStatus.CONFLICT,
                request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException x,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request)
    {
        var errors = new HashMap<String, String>();
        x.getBindingResult()
                .getAllErrors()
                .forEach(error -> mapErrors(error, errors));
        return handleExceptionInternal(
                x,
                errors,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    private void mapErrors(ObjectError error, Map<String, String> acc)
    {
        var fieldName = ((FieldError) error).getField();
        var errorMessage = error.getDefaultMessage();
        acc.put(fieldName, errorMessage);
    }
}
