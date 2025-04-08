package com.example.demo.mapper;

import com.example.demo.model.Todo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TodoMapper {

    @Select("SELECT id, task, completed FROM todos ORDER BY id")
    List<Todo> findAll();

    @Insert("INSERT INTO todos(task, completed) VALUES(#{task}, #{completed})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Todo todo);

    @Delete("DELETE FROM todos WHERE id = #{id}")
    int deleteById(Integer id);

    @Update("UPDATE todos SET completed = #{completed} WHERE id = #{id}")
    int updateCompleted(Todo todo);

    @Select("SELECT id, task, completed FROM todos WHERE id = #{id}")
    Todo findById(Integer id); // Helper method needed for update/delete checks
}
