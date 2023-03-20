package com.rkoyanagui.todowebsitebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.rkoyanagui.todowebsitebackend")
public class TodoWebsiteBackendApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(TodoWebsiteBackendApplication.class, args);
    }
}
