package com.neo.userAPI.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Generated;
import java.io.Serializable;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Todo")
public class TodoEntity implements Serializable {

    private ObjectId _id;

    private Long todoId;

    private String toDoText;

    private boolean toDoCompleted;

    private Date createdOn;

    private Date updatedOn;

    private Long userId;




}
