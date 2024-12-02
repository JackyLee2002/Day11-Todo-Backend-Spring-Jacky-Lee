package org.example.todoapplication.controller;


import org.example.todoapplication.model.Todo;
import org.example.todoapplication.repository.TodoRepository;
import org.example.todoapplication.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<Todo> getAllTodoItems(){
        return todoService.getAllTodoItems();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo createTodo(@RequestBody Todo todo){
        return todoService.createTodo(todo);
    }

    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable Integer id, @RequestBody Todo todo) throws IllegalStateException{
        return todoService.updateTodo(id, todo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable Integer id) throws IllegalStateException {
        todoService.deleteTodoById(id);
    }
}