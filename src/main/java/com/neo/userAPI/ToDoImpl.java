package com.neo.userAPI;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ToDoImpl implements  TodoInterface {

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<TodoEntity> createTodo(TodoEntity todoEntity) {
       /* generateTodoEntity(todoRequest);*/
        TodoEntity response = todoRepository.save(todoEntity);
        System.out.println(response + "Saved response");

        List<TodoEntity> toDoList = todoRepository.findAllByUserId(todoEntity.getUserId());

        return toDoList;
    }

/*    private TodoEntity generateTodoEntity(TodoRequest todoRequest) {
        if(todoRequest.get_id() != null ) {
            System.out.println("ID present , Hence must be update");
            return  new TodoEntity(todoRequest.get_id(),todoRequest.getTodoId(),todoRequest.getUserId(),todoRequest.getToDoText());
        }
        return  new TodoEntity(todoRequest.getTodoId(),todoRequest.getUserId(),todoRequest.getToDoText());
    }*/

    @Override
    public List<TodoEntity> getTodoTaskList(Long userId) {

        return todoRepository.findAllByUserId(userId);

    }

    @Override
    public List<TodoEntity> updateTodoTask(TodoEntity todoReq) throws IOException {

        TodoEntity todoEntity = mongoTemplate.findOne(Query.query(Criteria.where("todoId").is(todoReq.getTodoId())),TodoEntity.class);
        todoEntity.setToDoText(todoReq.getToDoText());
        todoRepository.save(todoEntity);

        List<TodoEntity> toDoList = todoRepository.findAllByUserId(todoReq.getUserId());
        return toDoList;
    }

    @Override
    public void deleteTodo(TodoEntity todoReq) {
        System.out.println("Deleting Todo Entity :::" +todoReq);
       // todoRepository.delete(todoEntity);
       // mongoTemplate.remove(todoEntity,"Todo");
        TodoEntity todoEntity = mongoTemplate.findOne(Query.query(Criteria.where("todoId").is(todoReq.getTodoId())),TodoEntity.class);
        todoRepository.delete(todoEntity);
       // todoRepository.deleteAll();
    }

   /* private TodoEntity updateTodoTaskDetails(TodoEntity todoData, TodoEntity response) {
        TodoEntity updatedTodo = response;

        updatedTodo.setUserId(response.getUserId());
        updatedTodo.setTodoId(response.getTodoId());

        //Update these field incase updated TodoData having content
        updatedTodo.setToDoText(todoData.getToDoText()
                                        .isEmpty() ? response.getToDoText() : todoData.getToDoText());
        updatedTodo.setToDoCompleted(todoData.isToDoCompleted() ? todoData.isToDoCompleted() : response.isToDoCompleted());

        return  updatedTodo;
    }*/


}
