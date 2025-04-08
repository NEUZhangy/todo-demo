package com.example.demo.controller;

import com.example.demo.mapper.TodoMapper;
import com.example.demo.model.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*") // Allow all origins, similar to the Python app
public class TodoController {

    @Autowired
    private TodoMapper todoMapper;

    // Corresponds to GET /todos
    @GetMapping("/todos")
    public List<Todo> getTodos() {
        return todoMapper.findAll();
    }

    // Corresponds to POST /addtodo
    // Using a Map to accept arbitrary JSON like {"task": "...", "completed": ...}
    // Or create a specific DTO class if preferred
    @PostMapping("/addtodo")
    public ResponseEntity<Map<String, Object>> addTodo(@RequestBody Map<String, Object> payload) {
        String task = (String) payload.get("task");
        Boolean completed = (Boolean) payload.getOrDefault("completed", false); // Default to false if not provided

        if (task == null || task.trim().isEmpty()) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task cannot be empty");
        }

        Todo newTodo = new Todo(task, completed);
        todoMapper.insert(newTodo); // MyBatis populates the ID after insert

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Todo added successfully");
        response.put("todo", newTodo); // Includes the generated ID
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Corresponds to DELETE /deletetodo/{todo_id}
    @DeleteMapping("/deletetodo/{todoId}")
    public ResponseEntity<Map<String, String>> deleteTodo(@PathVariable("todoId") Integer todoId) {
        Todo existingTodo = todoMapper.findById(todoId);
        if (existingTodo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found");
        }
        todoMapper.deleteById(todoId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Todo deleted successfully");
        return ResponseEntity.ok(response);
    }

    // Corresponds to PUT /todos/{todo_id}/complete
    // Using a Map to accept {"completed": true/false}
    @PutMapping("/todos/{todoId}/complete")
    public ResponseEntity<Map<String, Object>> completeTodo(@PathVariable("todoId") Integer todoId, @RequestBody Map<String, Boolean> payload) {
        Todo existingTodo = todoMapper.findById(todoId);
        if (existingTodo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found");
        }

        Boolean completed = payload.get("completed");
        if (completed == null) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing 'completed' field in request body");
        }

        existingTodo.setCompleted(completed);
        todoMapper.updateCompleted(existingTodo);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Todo updated successfully");
        // Fetch the updated todo to return the full object, though the Python version returned the input `todo` object before refresh
        // Returning the existingTodo object after setting completed status is closer to the Python logic before db.refresh()
        response.put("todo", existingTodo);
        return ResponseEntity.ok(response);
    }
}
