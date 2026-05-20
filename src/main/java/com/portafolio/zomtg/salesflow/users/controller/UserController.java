package com.portafolio.zomtg.salesflow.users.controller;


import com.portafolio.zomtg.salesflow.users.entity.User;
import com.portafolio.zomtg.salesflow.users.service.OwnerService;
import com.portafolio.zomtg.salesflow.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
public class UserController {
    UserService userService;
    com.portafolio.zomtg.salesflow.users.service.OwnerService ownerService;
    @Autowired
    public void UserController(UserService userService) {
        this.userService = userService;
    }



    @PostMapping("auth/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){

        User result=userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of( "User saved successfully", result));
    }


    @PostMapping("users")
    public ResponseEntity<?> saveUser(@RequestBody User user) {

        User result=userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("User Saved successfully", result));
    }


    @GetMapping("users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @GetMapping("users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        User result =userService.getUserById(UUID.fromString(id));

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUserById(@RequestParam String id) {
        UUID uuid = UUID.fromString(id.replace("\"", "").trim());
        userService.deleteUserById(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully");

    }

}
