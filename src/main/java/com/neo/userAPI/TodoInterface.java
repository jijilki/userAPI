package com.neo.userAPI;

import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.List;

public interface TodoInterface {
    List<TodoEntity> createTodo(TodoEntity todoEntity );

    List<TodoEntity> getTodoTaskList(Long userId);

    List<TodoEntity>  updateTodoTask(TodoEntity todoEntity) throws IOException;

    void deleteTodo(TodoEntity todoEntity);
}
