package com.rkoyanagui.todowebsitebackend.controller;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class TodoControllerIntegrationTest
{
    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @BeforeEach
    void beforeEach()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Sql("classpath:data/all.sql")
    void shouldGetAllTodoItems() throws Exception
    {
        var expectedResponse = "[{description:eat,done:true},{description:sleep,done:false}]";
        mockMvc.perform(get("/todo"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    @Sql("classpath:data/all.sql")
    void shouldGetAllDoneTodoItems() throws Exception
    {
        mockMvc.perform(get("/todo?done=true"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{description:eat,done:true}]"));
    }

    @Test
    @Sql("classpath:data/all.sql")
    void shouldGetAllActiveTodoItems() throws Exception
    {
        mockMvc.perform(get("/todo?done=false"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{description:sleep,done:false}]"));
    }

    @Test
    @Sql("classpath:data/all.sql")
    void shouldAddTodoItem() throws Exception
    {
        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content("{\"description\":\"work\",\"done\":false}")
                ).andExpect(status().isOk())
                .andExpect(content().json("{description:work,done:false}"));
    }

    @Test
    @Sql("classpath:data/all.sql")
    void shouldNotAddATodoItemWithTheSameDescriptionAsAnotherPreExistingItem() throws Exception
    {
        var expectedResponse = "{\"status\":409,\"error\":\"Could not save to-do item TodoDto(description=eat, done=false). It may be duplicated.\"}";
        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content("{\"description\":\"eat\",\"done\":false}")
                ).andExpect(status().isConflict())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    @Sql("classpath:data/all.sql")
    void shouldNotAddATodoItemWithAnEmptyDescription() throws Exception
    {
        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content("{\"description\":\"\",\"done\":false}")
                ).andExpect(status().isBadRequest())
                .andExpect(content().json("{description:\"must not be empty\"}"));
    }

    @Test
    @Sql("classpath:data/all.sql")
    void shouldRemoveTodoItem() throws Exception
    {
        mockMvc.perform(delete("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content("{\"description\":\"eat\"}")
        ).andExpect(status().isOk());
    }

    @Test
    @Sql("classpath:data/all.sql")
    void shouldNotRemoveTodoItemWithAnEmptyDescription() throws Exception
    {
        mockMvc.perform(delete("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content("{\"description\":\"\"}")
                ).andExpect(status().isBadRequest())
                .andExpect(content().json("{description:\"must not be empty\"}"));
    }

    @Test
    @Sql("classpath:data/all.sql")
    void shouldUpdateTodoItem() throws Exception
    {
        mockMvc.perform(put("/todo/sleep")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content("{\"description\":\"fly\",\"done\":true}")
                ).andExpect(status().isOk())
                .andExpect(content().json("{description:fly,done:true}"));
    }

    @Test
    @Sql("classpath:data/all.sql")
    void shouldNotUpdateTodoItemWithAnEmptyDescription() throws Exception
    {
        mockMvc.perform(put("/todo/sleep")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content("{\"description\":\"\",\"done\":true}")
                ).andExpect(status().isBadRequest())
                .andExpect(content().json("{description:\"must not be empty\"}"));
    }

    @Test
    @Sql("classpath:data/all.sql")
    void shouldNotUpdateTodoItemWithADuplicateDescription() throws Exception
    {
        var expectedResponse = "{\"status\":409,\"error\":\"Could not update to-do item TodoDto(description=eat, done=true). It may be duplicated.\"}";
        mockMvc.perform(put("/todo/sleep")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content("{\"description\":\"eat\",\"done\":true}")
                ).andExpect(status().isConflict())
                .andExpect(content().string(expectedResponse));
    }
}
