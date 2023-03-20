package com.rkoyanagui.todowebsitebackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rkoyanagui.todowebsitebackend.domain.Todo;
import com.rkoyanagui.todowebsitebackend.dto.TodoDto;
import com.rkoyanagui.todowebsitebackend.repository.TodoRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class TodoServiceTest
{
    @MockBean
    TodoRepository todoRepository;

    @Autowired
    TodoService todoService;

    @Test
    void shouldFindAllTodoItems()
    {
        when(todoRepository.findAll()).thenReturn(List.of(
                Todo.of(1, "eat", true),
                Todo.of(2, "sleep", false)
        ));
        var actual = todoService.getTodoItems(Optional.empty());
        var expected = List.of(
                TodoDto.of("eat", true),
                TodoDto.of("sleep", false)
        );
        assertEquals(expected, actual);
    }

    @Test
    void shouldFindDoneTodoItems()
    {
        when(todoRepository.findAllByDone(true))
                .thenReturn(List.of(Todo.of(1, "eat", true)));
        var actual = todoService.getTodoItems(Optional.of(true));
        var expected = List.of(TodoDto.of("eat", true));
        assertEquals(expected, actual);
    }

    @Test
    void shouldFindActiveTodoItems()
    {
        when(todoRepository.findAllByDone(false))
                .thenReturn(List.of(Todo.of(2, "sleep", false)));
        var actual = todoService.getTodoItems(Optional.of(false));
        var expected = List.of(TodoDto.of("sleep", false));
        assertEquals(expected, actual);
    }

    @Test
    void shouldAddTodoItem()
    {
        when(todoRepository.save(any()))
                .thenReturn(Todo.of(1, "work", false));
        var newItem = new TodoDto();
        newItem.setDescription("work");
        newItem.setDone(false);
        var actual = todoService.addTodoItem(newItem);
        var expected = TodoDto.of("work", false);
        assertEquals(expected, actual);
    }

    @Test
    void shouldRemoveTodoItem()
    {
        var argCaptor = ArgumentCaptor.forClass(String.class);
        when(todoRepository.deleteByDescription(argCaptor.capture()))
                .thenReturn(1L);
        var itemToDelete = new TodoDto();
        itemToDelete.setDescription("abc");
        todoService.removeTodoItem(itemToDelete);

        verify(todoRepository).deleteByDescription("abc");
        var actualDescription = argCaptor.getValue();
        assertEquals("abc", actualDescription);
    }

    @Test
    void shouldUpdateTodoItem()
    {
        when(todoRepository.findByDescription("sleep"))
                .thenReturn(Optional.of(Todo.of(2, "sleep", false)));
        when(todoRepository.save(any())).thenAnswer(returnsFirstArg());

        var updatedItem = TodoDto.of("fly", true);
        var actual = todoService.updateTodoItem(updatedItem, "sleep");
        var expected = TodoDto.of("fly", true);
        assertEquals(expected, actual);
    }
}
