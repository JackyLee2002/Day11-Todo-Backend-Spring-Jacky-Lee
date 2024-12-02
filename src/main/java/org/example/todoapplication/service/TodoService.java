package org.example.todoapplication.service;

import org.example.todoapplication.model.Todo;
import org.example.todoapplication.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> getAllTodoItems() {
        return todoRepository.findAll();
    }

    public Todo createTodo(Todo todo) {
        if (todo.getId() != null) {
            todo.setId(null);
        }
        return todoRepository.save(todo);
    }

    public Todo updateTodo(Integer id, Todo todo) throws IllegalStateException {
        Todo todoToUpdate = todoRepository.findById(id).orElseThrow(IllegalStateException::new);
        todoToUpdate.setText(todo.getText());
        todoToUpdate.setDone(todo.isDone());
        return todoRepository.save(todoToUpdate);
    }

    public void deleteTodoById(Integer id) {
        todoRepository.deleteById(id);
    }


}
