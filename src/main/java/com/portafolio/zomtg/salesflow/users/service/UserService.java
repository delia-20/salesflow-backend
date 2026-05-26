package com.portafolio.zomtg.salesflow.users.service;

import com.portafolio.zomtg.salesflow.exception.BusinessOperationException;
import com.portafolio.zomtg.salesflow.exception.InvalidCredentials;
import com.portafolio.zomtg.salesflow.exception.ObjectNotFound;
import com.portafolio.zomtg.salesflow.exception.UnauthorizedOperationException;
import com.portafolio.zomtg.salesflow.store.entity.Store;
import com.portafolio.zomtg.salesflow.users.dto.RegisterUserRequest;
import com.portafolio.zomtg.salesflow.users.dto.UserResponse;
import com.portafolio.zomtg.salesflow.users.entity.User;
import com.portafolio.zomtg.salesflow.users.enums.Role;
import com.portafolio.zomtg.salesflow.store.repository.StoreRepository;
import com.portafolio.zomtg.salesflow.users.mapper.UserMapper;
import com.portafolio.zomtg.salesflow.users.repository.UserRepository;
import lombok.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    UserRepository userRepository;
    StoreRepository storeRepository;
    UserMapper userMapper;
    private final PasswordEncoder passwordEncoder= new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, StoreRepository storeRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.userMapper = userMapper;
    }

    public UserResponse saveUser(RegisterUserRequest request) {
        System.out.println("Saving user: " + request.name());
        User user= userMapper.toEntity(request);
        if(verifyExistingUser(user.getUsername()) || verifyExistingUser(user.getEmail())){
             throw new BusinessOperationException("User already exists");
        }

        if(Role.EMPLOYEE.equals(user.getRole())){
            if(user.getOwnerId() == null || user.getStoreId() == null){
                throw new BusinessOperationException("User doesn't have an owner or store id");
            }else if(!userRepository.existsOwnerByOwner(user.getOwnerId()) || !existStoreAndOwner(user.getOwnerId(),user.getStoreId())){
                throw new InvalidCredentials("user doesn't have an owner or store id");

            }
        }

       user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (Role.OWNER.equals(user.getRole())) {

            user.setOwnerId(UUID.randomUUID());
        }

        user=userRepository.save(user);

        return userMapper.toResponse(user);
    }

    private boolean existStoreAndOwner(UUID ownerId, UUID storeId) {
        Store store=storeRepository.findById(storeId).orElse(null);

        if (store == null) {
            return false;
        } else if (store.getOwnerId().equals(ownerId)) {
            store.setNumEmployees(store.getNumEmployees()+1);
            return true;

        }
        return false;
    }


    public List<User> getAllUsers() {
        System.out.println("Fetching all users");
        return userRepository.findAll();
    }


    public Optional<User> getUserByName(String name, String surname) {
        return userRepository.findUserByNameAndSurname(name, surname);
    }


    public User getUserById(UUID id) {
        User user= userRepository.findUserById(id).orElseThrow(() -> new ObjectNotFound("User not found"));
        return user;
    }


    public boolean updatePassword(UUID id, String newPassword, String oldPassword) {
        Optional<User> optionalUser = userRepository.findUserById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return true;
            } else {
                throw new InvalidCredentials("Password does not match");
            }
        }
        return false;
    }


    public boolean verifyExistingUser(String emailOrUsername) {
        return userRepository.existsByEmail(emailOrUsername)
                || userRepository.existsByUsername(emailOrUsername);
    }


    public boolean updateEmail(UUID id, String newEmail) {
        Optional<User> optionalUser = userRepository.findUserById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEmail(newEmail);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public void deleteUserById(UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);

        }
        throw new UnauthorizedOperationException("User doesn't exist");
    }

    public boolean updateRole(UUID ownerId, UUID employeeId, String newRole, String ownerPassword) {
        Optional<User> optionalOwner = userRepository.findUserById(ownerId);
        if (optionalOwner.isPresent()) {
            User owner = optionalOwner.get();

            if (!passwordEncoder.matches(ownerPassword, owner.getPassword())) {
                throw new InvalidCredentials("Password does not match");
            }

            Optional<User> optionalEmployee = userRepository.findUserById(employeeId);
            if (optionalEmployee.isPresent()) {
                User employee = optionalEmployee.get();
                employee.setRole(getRoleEnum(newRole));
                userRepository.save(employee);
                return true;
            }
        }
        return false;
    }

    private @NonNull Role getRoleEnum(String newRole) {
        try {
            return Role.valueOf(newRole.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedOperationException("Invalid role");
        }
    }



}
