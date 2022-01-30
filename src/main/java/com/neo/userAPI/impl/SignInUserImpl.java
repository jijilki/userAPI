package com.neo.userAPI.impl;

import com.neo.userAPI.util.JwtTokenUtil;
import com.neo.userAPI.entity.UserEntity;
import com.neo.userAPI.repo.UserRepository;
import com.neo.userAPI.intr.SignInUserInt;
import com.neo.userAPI.model.UserRequest;
import com.neo.userAPI.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SignInUserImpl implements SignInUserInt {


    @Autowired
    UserRepository userRepo;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse validateUser(UserRequest request) {
        UserResponse userResponse = new UserResponse();
        List<UserEntity> userEntityList = userRepo.findByUserName(request.getUserName());
        System.out.println(userEntityList);
        //TODO Don't store password as plain text and compare it here

       if(passwordEncoder.matches(request.getPassword(),userEntityList.get(0).getPassword())){
           userResponse.setJwtToken(jwtTokenUtil.generateToken((userEntityList.get(0))));
       }
        return userResponse;
    }

   /* @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserRequest(username);
    }*/
}
