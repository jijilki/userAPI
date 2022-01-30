package com.neo.userAPI.intr;

import com.neo.userAPI.model.UserRequest;
import com.neo.userAPI.model.UserResponse;



public interface SignInUserInt  {

    public  UserResponse validateUser(UserRequest request);
}
