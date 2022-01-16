package com.neo.userAPI;

import com.neo.userAPI.model.UserRequest;
import com.neo.userAPI.model.UserResponse;
import org.springframework.stereotype.Component;


public interface SignInUserInt {

    public  UserResponse validateUser(UserRequest request);
}
