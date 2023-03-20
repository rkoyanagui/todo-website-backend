package com.rkoyanagui.todowebsitebackend.exception;

import java.io.Serial;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Duplicate to-do item description")
public class TodoItemPersistenceException extends RuntimeException
{
    @Serial
    private static final long serialVersionUID = 27456265616771658L;

    public TodoItemPersistenceException()
    {
    }

    public TodoItemPersistenceException(String message)
    {
        super(message);
    }

    public TodoItemPersistenceException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public TodoItemPersistenceException(Throwable cause)
    {
        super(cause);
    }
}
