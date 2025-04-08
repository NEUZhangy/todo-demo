from fastapi import FastAPI, HTTPException, Depends
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from sqlalchemy.orm import Session
from database import get_db, Todo

app = FastAPI()

# CORS Middleware configuration
origins = [
    "*",  # Allow all origins (adjust in production)
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],  # Allow all methods
    allow_headers=["*"],  # Allow all headers
)

class TodoItem(BaseModel):
    task: str
    completed: bool = False

class TodoUpdate(BaseModel):
    completed: bool

@app.get("/todos")
async def get_todos(db: Session = Depends(get_db)):
    todos = db.query(Todo).all()
    return todos

@app.post("/addtodo")
async def add_todo(todo: TodoItem, db: Session = Depends(get_db)):
    db_todo = Todo(task=todo.task, completed=todo.completed)
    db.add(db_todo)
    db.commit()
    db.refresh(db_todo)
    return {"message": "Todo added successfully", "todo": db_todo}

@app.delete("/deletetodo/{todo_id}")
async def delete_todo(todo_id: int, db: Session = Depends(get_db)):
    todo = db.query(Todo).filter(Todo.id == todo_id).first()
    if not todo:
        raise HTTPException(status_code=404, detail="Todo not found")
    db.delete(todo)
    db.commit()
    return {"message": "Todo deleted successfully"}

@app.put("/todos/{todo_id}/complete")
async def complete_todo(todo_id: int, todo_update: TodoUpdate, db: Session = Depends(get_db)):
    todo = db.query(Todo).filter(Todo.id == todo_id).first()
    if not todo:
        raise HTTPException(status_code=404, detail="Todo not found")
    todo.completed = todo_update.completed
    db.commit()
    db.refresh(todo)
    return {"message": "Todo updated successfully", "todo": todo}
