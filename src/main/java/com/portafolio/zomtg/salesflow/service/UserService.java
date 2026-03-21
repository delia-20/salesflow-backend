package com.portafolio.zomtg.salesflow.service;

import com.portafolio.zomtg.salesflow.model.entities.Store;
import com.portafolio.zomtg.salesflow.model.entities.User;
import com.portafolio.zomtg.salesflow.model.enums.Role;
import com.portafolio.zomtg.salesflow.repository.StoreRepository;
import com.portafolio.zomtg.salesflow.repository.UserRepository;
import lombok.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    UserRepository userRepository;
    StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder= new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, StoreRepository storeRepository) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
    }

    public boolean saveUser(User user) {
        System.out.println("Saving user: " + user);
        if(verifyExistingUser(user.getUsername()) || verifyExistingUser(user.getEmail())){
            System.out.println("User already exists"+user.getRole().name());
            return false;
        }

        if(Role.EMPLOYEE.equals(user.getRole())){
            if(user.getOwnerId() == null || user.getStoreId() == null){
                return false;
            }else if(!userRepository.existsOwnerByOwner(user.getOwnerId()) || !existStoreAndOwner(user.getOwnerId(),user.getStoreId())){
                return false;

            }
        }

       user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (Role.OWNER.equals(user.getRole())) {

            user.setOwnerId(UUID.randomUUID());
          //  user.setStoreId(UUID.randomUUID());
        }

        userRepository.save(user);
        return true;
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

    // 🟢 Get all users
    public List<User> getAllUsers() {
        System.out.println("Fetching all users");
        return userRepository.findAll();
    }

    // 🟢 Find by name + surname
    public Optional<User> getUserByName(String name, String surname) {
        return userRepository.findUserByNameAndSurname(name, surname);
    }

    // 🟢 Find by ID (UUID)
    public Optional<User> getUserById(UUID id) {

         return userRepository.findUserById(id);
    }

    // 🟡 Update password
    public boolean updatePassword(UUID id, String newPassword, String oldPassword) {
        Optional<User> optionalUser = userRepository.findUserById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return true;
            } else {
                throw new RuntimeException("Passwords do not match");
            }
        }
        return false;
    }

    // 🟣 Verify existing user by email OR username
    public boolean verifyExistingUser(String emailOrUsername) {
        return userRepository.existsByEmail(emailOrUsername)
                || userRepository.existsByUsername(emailOrUsername);
    }

    // 🔵 Update email
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

    // 🔴 Delete user by ID
    public boolean deleteUserById(UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }


    



    public boolean updateRole(UUID ownerId, UUID employeeId, String newRole, String ownerPassword) {
        Optional<User> optionalOwner = userRepository.findUserById(ownerId);
        if (optionalOwner.isPresent()) {
            User owner = optionalOwner.get();

            if (!passwordEncoder.matches(ownerPassword, owner.getPassword())) {
                throw new RuntimeException("Owner password is incorrect");
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
            throw new RuntimeException("Invalid role: " + newRole);
        }
    }

    //delete userById

    //delete user byEmail



}
