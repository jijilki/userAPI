package com.neo.userAPI;

import com.neo.userAPI.model.UserRequest;
import com.neo.userAPI.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public class SignInUserImpl implements  SignInUserInt {


    @Autowired
    UserRepository userRepo;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Override
    public UserResponse validateUser(UserRequest request) {
        UserResponse userResponse = new UserResponse();
        List<UserEntity> userEntityList = userRepo.findByUserName(request.getUserName());
        System.out.println(userEntityList);
        //TODO Don't store password as plain text and compare it here
        if(userEntityList.size() > 0 && request.getPassword().equals(userEntityList.get(0).getPassword())){
            //Generate Token and Add to User Response and send it back.  Do we need to persist
            userResponse.setJwtToken(jwtTokenUtil.generateToken((userEntityList.get(0))));
        }
        return userResponse;
    }
}
