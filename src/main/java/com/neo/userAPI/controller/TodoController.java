package com.neo.userAPI.controller;

import com.neo.userAPI.util.JwtTokenUtil;
import com.neo.userAPI.entity.TodoEntity;
import com.neo.userAPI.intr.TodoInterface;
import com.neo.userAPI.util.TodoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class TodoController {

    @Autowired
    TodoInterface todoInterface;

    @Autowired
    KafkaTemplate<String, TodoEntity> kafkaTemplate;

    @RequestMapping(value = "/addTodo", method = RequestMethod.POST)
    public ResponseEntity createTodoTask(@RequestBody TodoEntity todoRequest) {
        ResponseEntity resp;
        if (TodoUtil.hasWriteAccess()) {
            List<TodoEntity> todo = todoInterface.createTodo(todoRequest);
            resp = new ResponseEntity<>(todo, HttpStatus.OK);
            sendMessageToKafkaQueue(todoRequest);
        } else {

            resp = new ResponseEntity<>("No Write access for the user", HttpStatus.UNAUTHORIZED);

        }
        return resp;

    }

    @RequestMapping(value = "getTodoList", method = RequestMethod.GET)
    public ResponseEntity getTodoTasks() {
        if (TodoUtil.hasReadAccess()) {
            Long userId = JwtTokenUtil.getUserIdFromSecurityContext();
            List<TodoEntity> todoEntities = todoInterface.getTodoTaskList(userId);
            return new ResponseEntity(todoEntities, HttpStatus.OK);

        }
        return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);

    }

    @RequestMapping(value = "updateTodoTask", method = RequestMethod.POST)
    public ResponseEntity updateTodoTask(@RequestBody TodoEntity todoRequest) {


        if (TodoUtil.hasWriteAccess()) {
            try {
                List<TodoEntity> todoEntities = todoInterface.updateTodoTask(todoRequest);
                return new ResponseEntity<>(todoEntities, HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity("Authorization missing", HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "deleteTodo", method = RequestMethod.DELETE)
    public ResponseEntity deleteTodoTask(@RequestBody TodoEntity todoEntity) {

        todoInterface.deleteTodo(todoEntity);

        return new ResponseEntity("Deleted Todo", HttpStatus.OK);
    }

    // Controller method for Kafka Consumer


  //  @KafkaListener(topics = "TutorialTopic", groupId = "groupId")
    public void listenGroupFoo(String message) {
        System.out.println("Received Message in group foo: " + message);
    }


    public void sendMessageToKafkaQueue(TodoEntity todoReq) {
       // kafkaTemplate.send("TutorialTopic", todoReq);
    }

}
