package com.rkoyanagui.todowebsitebackend.service;

import com.rkoyanagui.todowebsitebackend.dto.TodoDto;
import com.rkoyanagui.todowebsitebackend.mapper.TodoMapper;
import com.rkoyanagui.todowebsitebackend.repository.TodoRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TodoService
{
    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;

    public List<TodoDto> getTodoItems(Optional<Boolean> done)
    {
        return done.map(isDone -> todoRepository.findAllByDone(isDone)
                .stream()
                .map(todoMapper::entityToDto)
                .toList()
        ).orElseGet(() -> todoRepository.findAll()
                .stream()
                .map(todoMapper::entityToDto)
                .toList());
    }

    @Transactional
    public TodoDto addTodoItem(TodoDto todoDto)
    {
        var todo = todoMapper.dtoToEntity(todoDto);
        var savedTodo = todoRepository.save(todo);
        return todoMapper.entityToDto(savedTodo);
    }

    @Transactional
    public void removeTodoItem(TodoDto todoDto)
    {
        todoRepository.deleteByDescription(todoDto.getDescription());
    }

    @Transactional
    public TodoDto updateTodoItem(TodoDto updatedItem, String originalDescription)
    {
        var msg = "Could not find an item with description: " + originalDescription;
        var todo = todoRepository.findByDescription(originalDescription)
                .orElseThrow(() -> new IllegalArgumentException(msg));
        todo.setDescription(updatedItem.getDescription());
        todo.setDone(updatedItem.isDone());
        var savedTodo = todoRepository.save(todo);
        return todoMapper.entityToDto(savedTodo);
    }
}
