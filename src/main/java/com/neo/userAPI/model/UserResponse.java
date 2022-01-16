package com.neo.userAPI.model;

import lombok.Data;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

import java.io.Serializable;
import java.util.Objects;

@Data
public class UserResponse implements Serializable {

    private static final long serialVersionUID = -4075984720098516480L;

    private String jwtToken;

}
