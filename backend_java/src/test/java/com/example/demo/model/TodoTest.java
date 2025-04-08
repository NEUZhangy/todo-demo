package com.example.demo.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TodoTest {
    @Test
    public void testDefaultConstructor() {
        Todo todo = new Todo();
        assertNull(todo.getId());
        assertNull(todo.getTask());
        assertFalse(todo.isCompleted());
    }

    @Test
    public void testParameterizedConstructor() {
        Todo todo = new Todo("Test task", true);
        assertNull(todo.getId());
        assertEquals("Test task", todo.getTask());
        assertTrue(todo.isCompleted());
    }

    @Test
    public void testSettersAndGetters() {
        Todo todo = new Todo();
        todo.setId(1);
        todo.setTask("Updated task");
        todo.setCompleted(true);

        assertEquals(1, todo.getId());
        assertEquals("Updated task", todo.getTask());
        assertTrue(todo.isCompleted());
    }

    @Test
    public void testEqualsAndHashCode() {
        Todo todo1 = new Todo("Task 1", false);
        todo1.setId(1);
        Todo todo2 = new Todo("Task 1", false);
        todo2.setId(1);
        Todo todo3 = new Todo("Task 2", true);
        todo3.setId(2);

        assertEquals(todo1, todo2);
        assertNotEquals(todo1, todo3);
        assertEquals(todo1.hashCode(), todo2.hashCode());
        assertNotEquals(todo1.hashCode(), todo3.hashCode());
    }

    @Test
    public void testToString() {
        Todo todo = new Todo("Test toString", false);
        todo.setId(1);
        String expected = "Todo{id=1, task='Test toString', completed=false}";
        assertEquals(expected, todo.toString());
    }
}
