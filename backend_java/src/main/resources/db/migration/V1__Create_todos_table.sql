-- Create the todos table
CREATE TABLE IF NOT EXISTS todos (
    id SERIAL PRIMARY KEY,
    task VARCHAR(255) NOT NULL,
    completed BOOLEAN DEFAULT FALSE
);

-- Create an index on the task column
CREATE INDEX IF NOT EXISTS idx_todos_task ON todos (task);
