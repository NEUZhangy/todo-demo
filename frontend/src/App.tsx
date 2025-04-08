import React, { useState, useEffect } from 'react';
import { PlusCircle, Trash2, CheckCircle, Circle } from 'lucide-react';

interface Todo {
  id: number;
  task: string;
  completed: boolean;
}

const API_URL = 'http://localhost:8000'; // Assuming backend runs on port 8000

function App() {
  const [todos, setTodos] = useState<Todo[]>([]);
  const [newTodo, setNewTodo] = useState('');

  useEffect(() => {
    fetchTodos();
  }, []);

  const fetchTodos = async () => {
    try {
      const response = await fetch(`${API_URL}/todos`);
      if (!response.ok) {
        throw new Error('Failed to fetch todos');
      }
      const data = await response.json();
      setTodos(data);
    } catch (error) {
      console.error("Error fetching todos:", error);
    }
  };

  const addTodo = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newTodo.trim()) return;

    try {
      const response = await fetch(`${API_URL}/addtodo`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ task: newTodo.trim(), completed: false }),
      });
      if (!response.ok) {
        throw new Error('Failed to add todo');
      }
      const result = await response.json();
      setTodos([...todos, result.todo]); // Add the new todo returned by the backend
      setNewTodo('');
    } catch (error) {
      console.error("Error adding todo:", error);
    }
  };

  const toggleTodo = async (id: number) => {
    const todoToToggle = todos.find(todo => todo.id === id);
    if (!todoToToggle) return;

    try {
      const response = await fetch(`${API_URL}/todos/${id}/complete`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ completed: !todoToToggle.completed }),
      });
      if (!response.ok) {
        throw new Error('Failed to update todo');
      }
      const result = await response.json();
      setTodos(todos.map(todo =>
        todo.id === id ? result.todo : todo
      ));
    } catch (error) {
      console.error("Error toggling todo:", error);
    }
  };

  const deleteTodo = async (id: number) => {
    try {
      const response = await fetch(`${API_URL}/deletetodo/${id}`, {
        method: 'DELETE',
      });
      if (!response.ok) {
        throw new Error('Failed to delete todo');
      }
      setTodos(todos.filter(todo => todo.id !== id));
    } catch (error) {
      console.error("Error deleting todo:", error);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-100 to-blue-100 py-8">
      <div className="max-w-2xl mx-auto px-4">
        <div className="bg-white rounded-xl shadow-lg p-6">
          <h1 className="text-3xl font-bold text-gray-800 mb-8 text-center">
            Todo List
          </h1>

          <form onSubmit={addTodo} className="mb-6">
            <div className="flex gap-2">
              <input
                type="text"
                value={newTodo}
                onChange={(e) => setNewTodo(e.target.value)}
                placeholder="Add a new todo..."
                className="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent"
              />
              <button
                type="submit"
                className="bg-purple-600 text-white px-4 py-2 rounded-lg hover:bg-purple-700 transition-colors duration-200 flex items-center gap-2"
              >
                <PlusCircle size={20} />
                Add
              </button>
            </div>
          </form>

          <div className="space-y-3">
            {todos.map(todo => (
              <div
                key={todo.id}
                className={`flex items-center gap-3 p-3 rounded-lg ${
                  todo.completed ? 'bg-gray-50' : 'bg-white'
                } border border-gray-200 hover:border-purple-200 transition-colors duration-200`}
              >
                <button
                  onClick={() => toggleTodo(todo.id)}
                  className="text-gray-500 hover:text-purple-600 transition-colors duration-200"
                >
                  {todo.completed ? (
                    <CheckCircle className="text-green-500" size={24} />
                  ) : (
                    <Circle size={24} />
                  )}
                </button>
                <span
                  className={`flex-1 text-gray-800 ${
                    todo.completed ? 'line-through text-gray-500' : ''
                  }`}
                >
                  {todo.task}
                </span>
                <button
                  onClick={() => deleteTodo(todo.id)}
                  className="text-gray-400 hover:text-red-500 transition-colors duration-200"
                >
                  <Trash2 size={20} />
                </button>
              </div>
            ))}
            {todos.length === 0 && (
              <p className="text-center text-gray-500 py-6">
                No todos yet. Add one above!
              </p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
