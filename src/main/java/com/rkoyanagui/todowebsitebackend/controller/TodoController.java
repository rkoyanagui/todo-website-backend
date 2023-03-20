package com.rkoyanagui.todowebsitebackend.controller;

import com.rkoyanagui.todowebsitebackend.dto.TodoDto;
import com.rkoyanagui.todowebsitebackend.exception.TodoItemPersistenceException;
import com.rkoyanagui.todowebsitebackend.service.TodoService;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
@CrossOrigin(originPatterns = "http://localhost:[*]")
@RequiredArgsConstructor
public class TodoController
{
    private final TodoService todoService;

    @GetMapping
    public List<TodoDto> getTodoItems(@RequestParam Optional<Boolean> done)
    {
        return todoService.getTodoItems(done);
    }

    @PostMapping
    public TodoDto addTodoItem(@RequestBody @Valid TodoDto todoDto)
    {
        try
        {
            return todoService.addTodoItem(todoDto);
        }
        catch (DataIntegrityViolationException x)
        {
            var msg = "Could not save to-do item " + todoDto + ". It may be duplicated.";
            throw new TodoItemPersistenceException(msg, x);
        }
    }

    @PutMapping(path = "/{originalDescription}")
    public TodoDto updateTodoItem(
            @PathVariable("originalDescription") String originalDescription,
            @RequestBody @Valid TodoDto updatedItem)
    {
        try
        {
            return todoService.updateTodoItem(updatedItem, originalDescription);
        }
        catch (DataIntegrityViolationException x)
        {
            var msg = "Could not update to-do item " + updatedItem + ". It may be duplicated.";
            throw new TodoItemPersistenceException(msg, x);
        }
    }

    @DeleteMapping
    public void removeTodoItem(@RequestBody @Valid TodoDto todoDto)
    {
        todoService.removeTodoItem(todoDto);
    }
}
