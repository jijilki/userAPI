package com.neo.userAPI.intr;

import com.neo.userAPI.model.UserRequest;
import com.neo.userAPI.model.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserServiceInt extends UserDetailsService {

    public  UserResponse validateUser(UserRequest request);
}
