package com.neo.userAPI.impl;

import com.neo.userAPI.entity.UserEntity;
import com.neo.userAPI.intr.UserServiceInt;
import com.neo.userAPI.model.UserRequest;
import com.neo.userAPI.model.UserResponse;
import com.neo.userAPI.repo.UserRepository;
import com.neo.userAPI.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.util.*;

@Component
public class UserServiceImpl implements UserServiceInt {


    @Autowired
    UserRepository userRepo;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    PasswordEncoder passwordEncoder;

    // Validating the user , User roles are just collecting and will be added as claims of the token
    @Override
    public UserResponse validateUser(UserRequest request) {
        UserResponse userResponse = new UserResponse();
            Optional<UserEntity> optUser = userRepo.findByUserName(request.getUsername());
            if (optUser.isEmpty()) {
                throw new UsernameNotFoundException("User not found with userName : " + request.getUsername());
            }
            UserEntity user = optUser.get();
            List<String> userRoles = user.getUserRoles();
            Set<GrantedAuthority> ga = new HashSet<>();
            for (String userRole : userRoles) {
                ga.add(new SimpleGrantedAuthority(userRole));
            }
            org.springframework.security.core.userdetails.User springUser = new User(user.getUserName(), user.getPassword(), ga);

            if(passwordEncoder.matches(request.getPassword(),springUser.getPassword())){
                userResponse.setJwtToken(jwtTokenUtil.generateToken(springUser));
            }
        return userResponse;
    }

    //TODO The below isn't being used as the AuthenticationBuilderManager based security config isnt being used.  Not able to implement along with HttpSecurity

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        org.springframework.security.core.userdetails.User springUser = null;
        Optional<UserEntity> optUser = userRepo.findByUserName(userName);

        if (optUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with userName : " + userName);
        } else {

            UserEntity user = optUser.get();

            List<String> userRoles = user.getUserRoles();

            Set<GrantedAuthority> ga = new HashSet<>();

            for (String userRole : userRoles) {
                ga.add(new SimpleGrantedAuthority(userRole));
            }

            springUser = new User(user.getUserName(), user.getPassword(), ga);

        }
        return springUser;
    }
}
