package com.rkoyanagui.todowebsitebackend.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

import com.rkoyanagui.todowebsitebackend.domain.Todo;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class TodoRepositoryIntegrationTest
{
    @Autowired
    TodoRepository todoRepository;

    @Test
    @Sql("classpath:data/all.sql")
    void shouldFindAllTodoItems()
    {
        var actual = todoRepository.findAll();
        var expected = List.of(
                Todo.of(1, "eat", true),
                Todo.of(2, "sleep", false)
        );
        assertEquals(expected, actual);
    }

    @Test
    @Sql("classpath:data/all.sql")
    void shouldFindAllDoneTodoItems()
    {
        var actual = todoRepository.findAllByDone(true);
        var expected = List.of(Todo.of(1, "eat", true));
        assertEquals(expected, actual);
    }

    @Test
    @Sql("classpath:data/all.sql")
    void shouldFindAllActiveTodoItems()
    {
        var actual = todoRepository.findAllByDone(false);
        var expected = List.of(Todo.of(2, "sleep", false));
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    @Sql("classpath:data/all.sql")
    void shouldDeleteTodoItemByDescription()
    {
        todoRepository.deleteByDescription("eat");
        var maybeEat = todoRepository.findAll()
                .stream()
                .map(Todo::getDescription)
                .filter("eat"::equals)
                .findAny();
        assertTrue(maybeEat.isEmpty());
    }

    @Test
    @Sql("classpath:data/all.sql")
    void shouldFindTodoItemByDescription()
    {
        var actual = todoRepository.findByDescription("eat").orElseThrow();
        var expected = Todo.of(1, "eat", true);
        assertEquals(expected, actual);
    }
}
