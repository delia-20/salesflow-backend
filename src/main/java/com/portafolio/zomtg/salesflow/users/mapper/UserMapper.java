package com.portafolio.zomtg.salesflow.users.mapper;


import com.portafolio.zomtg.salesflow.users.dto.RegisterUserRequest;
import com.portafolio.zomtg.salesflow.users.dto.UserResponse;
import com.portafolio.zomtg.salesflow.users.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(RegisterUserRequest request){
        User user = new User();
        user.setName(request.name());
        user.setSurname(request.surname());
        user.setRole(request.role());
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setOwnerId(request.ownerId());
        user.setStoreId(request.storeId());
        return user;
    }

    public UserResponse toResponse(User user){
        UserResponse response= new UserResponse(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getOwnerId(),
                user.getStoreId(),
                user.isActive()
        );
        return response;

    }
}
