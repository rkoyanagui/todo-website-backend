package com.rkoyanagui.todowebsitebackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotEmpty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TodoDto
{
    @NotEmpty
    @JsonProperty("description")
    private String description;

    private boolean done;
}
