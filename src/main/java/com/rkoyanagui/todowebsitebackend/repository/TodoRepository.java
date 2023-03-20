package com.rkoyanagui.todowebsitebackend.repository;

import com.rkoyanagui.todowebsitebackend.domain.Todo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer>
{
    List<Todo> findAllByDone(boolean done);

    Optional<Todo> findByDescription(String description);

    long deleteByDescription(String description);
}
