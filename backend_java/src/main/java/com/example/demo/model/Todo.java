package com.example.demo.model;

public class Todo {
    private Integer id; // Use Integer to allow null for new objects before saving
    private String task;
    private boolean completed;

    // Default constructor (often needed by frameworks like MyBatis/JPA)
    public Todo() {
    }

    // Constructor for creating instances
    public Todo(String task, boolean completed) {
        this.task = task;
        this.completed = completed;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "Todo{" +
               "id=" + id +
               ", task='" + task + '\'' +
               ", completed=" + completed +
               '}';
    }

    // Optional: equals() and hashCode() methods if needed for comparisons or use in collections
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return completed == todo.completed && java.util.Objects.equals(id, todo.id) && java.util.Objects.equals(task, todo.task);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, task, completed);
    }
}
