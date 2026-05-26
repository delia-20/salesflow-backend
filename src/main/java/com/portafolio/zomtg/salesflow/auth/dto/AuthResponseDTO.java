package com.portafolio.zomtg.salesflow.auth.dto;

import com.portafolio.zomtg.salesflow.users.dto.RegisterUserRequest;
import com.portafolio.zomtg.salesflow.users.dto.UserResponse;
import com.portafolio.zomtg.salesflow.users.entity.User;

public class AuthResponseDTO {
     private String token;
     private UserResponse user;
     public AuthResponseDTO(String token, UserResponse user) {
         this.token = token;
         this.user = user;

     }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}
