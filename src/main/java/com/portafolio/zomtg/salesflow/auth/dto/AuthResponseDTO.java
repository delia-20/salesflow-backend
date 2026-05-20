package com.portafolio.zomtg.salesflow.auth.dto;

import com.portafolio.zomtg.salesflow.users.entity.User;

public class AuthResponseDTO {
     private String token;
     private User user;
     public AuthResponseDTO(String token, User user) {
         this.token = token;
         this.user = user;

     }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
