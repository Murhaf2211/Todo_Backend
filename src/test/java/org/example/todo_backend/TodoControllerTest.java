package org.example.todo_backend;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Test
    public void shouldReturnAllTodos() throws Exception {
        Mockito.when(todoService.getAllTodos()).thenReturn(List.of());

        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())  // Verify it's an array
                .andExpect(jsonPath("$.length()").value(0));  // Verify that the array is empty
    }

    @Test
    public void shouldCreateTodo() throws Exception {

        Todo mockTodo = new Todo(1L, "Test Todo", "Description", "pending");

        Mockito.when(todoService.createTodo(any(Todo.class))).thenReturn(mockTodo);

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Test Todo\", \"description\": \"Description\", \"status\": \"pending\"}"))
                .andExpect(status().isOk())  // Check for HTTP 200 status
                .andExpect(jsonPath("$.title").value("Test Todo"))  // Verify response body content
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.status").value("pending"));
    }

    @Test
    public void shouldUpdateTodo() throws Exception {

        Todo existingTodo = new Todo(1L, "Existing Todo", "Existing Description", "pending");


        Todo updatedTodo = new Todo(1L, "Updated Todo", "Updated Description", "completed");


        Mockito.when(todoService.updateTodo(eq(1L), any(Todo.class))).thenReturn(updatedTodo);


        mockMvc.perform(put("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Updated Todo\", \"description\": \"Updated Description\", \"status\": \"completed\"}"))
                .andExpect(status().isOk())  // Check for HTTP 200 status
                .andExpect(jsonPath("$.title").value("Updated Todo"))  // Validate updated title
                .andExpect(jsonPath("$.description").value("Updated Description"))  // Validate updated description
                .andExpect(jsonPath("$.status").value("completed"));  // Validate updated status
    }

    @Test
    public void shouldDeleteTodo() throws Exception {

        Mockito.doNothing().when(todoService).deleteTodo(1L);

        mockMvc.perform(delete("/todos/1"))
                .andExpect(status().isOk());  // Check for HTTP 200 status
    }
}