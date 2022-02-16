package com.neo.userAPI.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "User")
public class UserEntity {
    private String userName;
    private String password;
    private List<String> userRoles;

    }

