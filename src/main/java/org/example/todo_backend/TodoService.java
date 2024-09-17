package org.example.todo_backend;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Service
public class TodoService {
    private final List<Todo> todos = new ArrayList<>();
    private final Stack<Todo> undoStack = new Stack<>();
    private final Stack<Todo> redoStack = new Stack<>();
    private Long idCounter = 1L;

    public List<Todo> getAllTodos() {
        return todos;
    }

    public Todo createTodo(Todo todo) {
        todo.setId(idCounter++);
        todos.add(todo);
        undoStack.push(new Todo(todo.getId(), todo.getTitle(), todo.getDescription(), todo.getStatus()));
        redoStack.clear(); // Clear redo stack after a new action
        return todo;
    }

    public Todo updateTodo(Long id, Todo updatedTodo) {
        Todo existingTodo = todos.stream()
                .filter(todo -> todo.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        undoStack.push(new Todo(existingTodo.getId(), existingTodo.getTitle(), existingTodo.getDescription(), existingTodo.getStatus()));
        redoStack.clear(); // Clear redo stack after a new action

        existingTodo.setTitle(updatedTodo.getTitle());
        existingTodo.setDescription(updatedTodo.getDescription());
        existingTodo.setStatus(updatedTodo.getStatus());

        return existingTodo;
    }

    public void deleteTodo(Long id) {
        Todo todoToDelete = todos.stream()
                .filter(todo -> todo.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        undoStack.push(new Todo(todoToDelete.getId(), todoToDelete.getTitle(), todoToDelete.getDescription(), todoToDelete.getStatus()));
        todos.removeIf(todo -> todo.getId().equals(id));
        redoStack.clear(); // Clear redo stack after a new action
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Todo lastAction = undoStack.pop();
            redoStack.push(lastAction);

            todos.removeIf(todo -> todo.getId().equals(lastAction.getId()));
            todos.add(new Todo(lastAction.getId(), lastAction.getTitle(), lastAction.getDescription(), lastAction.getStatus()));
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Todo redoAction = redoStack.pop();
            undoStack.push(redoAction);

            todos.removeIf(todo -> todo.getId().equals(redoAction.getId()));
            todos.add(new Todo(redoAction.getId(), redoAction.getTitle(), redoAction.getDescription(), redoAction.getStatus()));
        }
    }
}
