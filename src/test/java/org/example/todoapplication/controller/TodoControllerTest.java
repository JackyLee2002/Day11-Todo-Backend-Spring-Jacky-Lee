package org.example.todoapplication.controller;

import org.example.todoapplication.model.Todo;
import org.example.todoapplication.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(MockitoExtension.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc client;

    @Autowired
    private TodoController todoController;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private JacksonTester<List<Todo>> todoListJacksonTester;

    @BeforeEach
    public void initJPAData() {
        todoRepository.deleteAll();
        todoRepository.save(Todo.builder().text("todo1").done(false).build());
        todoRepository.save(Todo.builder().text("todo2").done(false).build());
        todoRepository.save(Todo.builder().text("todo3").done(false).build());
        todoRepository.save(Todo.builder().text("todo4").done(false).build());
        todoRepository.save(Todo.builder().text("todo5").done(false).build());
    }

    @Test
    void should_return_todos_when_get_all_todos_given_exist() throws Exception {
        //given
        final List<Todo> givenTodos = todoRepository.findAll();
        //when
        //then
        final String jsonResponse = client.perform(MockMvcRequestBuilders.get("/todos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        final List<Todo> todoResults = todoListJacksonTester.parseObject(jsonResponse);
        assertThat(todoResults)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(givenTodos);
    }

    @Test
    void should_create_todo_success() throws Exception {
        // Given
        todoRepository.deleteAll();
        String givenTodo = "{\"text\": \"abc\", \"done\": \"false\"}";

        // When
        // Then
        client.perform(MockMvcRequestBuilders.post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenTodo)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("abc"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done").value(false));
        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).hasSize(1);
        assertThat(todos.get(0).getId()).isNotNull();
        assertThat(todos.get(0).getText()).isEqualTo("abc");
        assertThat(todos.get(0).isDone()).isFalse();
    }

    @Test
    void should_return_error_when_delete_given_todo_with_invalid_id() throws Exception {
        // Given
        Integer invalidId = -1;
        // When
        // Then
        client.perform(MockMvcRequestBuilders.delete("/todos/" + invalidId))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void should_remove_todo_when_delete_given_valid_todo_id() throws Exception {
        // Given
        final List<Todo> givenTodo = todoRepository.findAll();
        int givenId = givenTodo.get(0).getId();

        // When
        // Then
        client.perform(MockMvcRequestBuilders.delete("/todos/" + givenId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).hasSize(4);
        assertThat(todos.get(0).getId()).isEqualTo(givenTodo.get(1).getId());
        assertThat(todos.get(1).getId()).isEqualTo(givenTodo.get(2).getId());
        assertThat(todos.get(2).getId()).isEqualTo(givenTodo.get(3).getId());
        assertThat(todos.get(3).getId()).isEqualTo(givenTodo.get(4).getId());
    }

    @Test
    void should_update_todo_when_update_given_valid_todo_body() throws Exception {
        // Given
        final List<Todo> givenTodos = todoRepository.findAll();
        int givenId = givenTodos.get(0).getId();
        String newText = "New Text";
        boolean newDoneStatus = true;

        String givenTodo = String.format("{\"id\": %s, \"text\": \"%s\", \"done\": \"%b\"}",
                givenId,newText,newDoneStatus
        );

        // When
        // Then
        client.perform(MockMvcRequestBuilders.put("/todos/" + givenId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenTodo))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(newText))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done").value(newDoneStatus));
        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).hasSize(5);
        assertThat(todos.get(0).getId()).isEqualTo(givenId);
        assertThat(todos.get(0).getText()).isEqualTo(newText);
        assertThat(todos.get(0).isDone()).isTrue();

    }





}
