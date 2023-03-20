package com.rkoyanagui.todowebsitebackend.mapper;

import com.rkoyanagui.todowebsitebackend.domain.Todo;
import com.rkoyanagui.todowebsitebackend.dto.TodoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TodoMapper
{
    @Mapping(target = "description", source = "todo.description")
    TodoDto entityToDto(Todo todo);

    @Mapping(target = "description", source = "todoDto.description")
    Todo dtoToEntity(TodoDto todoDto);
}
