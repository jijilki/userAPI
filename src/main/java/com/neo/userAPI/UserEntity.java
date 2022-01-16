package com.neo.userAPI;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "User")
public class UserEntity {

    private long userId;
    private String userName;
    private String password;
    private String userRoles;


}
