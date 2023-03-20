package com.rkoyanagui.todowebsitebackend.dto;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(staticName = "of")
public class ErrorDto
{
    int status;
    String error;
}
