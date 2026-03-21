package com.portafolio.zomtg.salesflow.controller;


import com.portafolio.zomtg.salesflow.model.dto.UserDTO;
import com.portafolio.zomtg.salesflow.model.entities.User;
import com.portafolio.zomtg.salesflow.service.OwnerService;
import com.portafolio.zomtg.salesflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
public class UserController {
    UserService userService;
    OwnerService ownerService;
    @Autowired
    public void UserController(UserService userService) {
        this.userService = userService;
    }

//    @PostMapping("users/save-user")
//    public void saveUser(@RequestBody User user){
//        userService.saveUser(user);
//    }
//
//    @GetMapping("users/get-all-users")
//    public List<User> getUsers() {
//        return userService.getAllUsers();
//    }

    //registrar usuario
    @PostMapping("auth/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        try {
            boolean result=userService.saveUser(user);
            if(result)
                return ResponseEntity.status(HttpStatus.CREATED).body("User saved successfully");
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error saving user");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving user");
        }

    }

    // 🟢 Crear usuario
    @PostMapping("users")
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        try {
            boolean result=userService.saveUser(user);
            if(result==true)
                return ResponseEntity.status(HttpStatus.CREATED).body("User saved successfully");
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error saving user");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    // 🟢 Obtener todos los usuarios para pruebas
    @GetMapping("users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    //
    @GetMapping("users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        Optional<User> result =userService.getUserById(UUID.fromString(id));
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","no found"));
        }
        return ResponseEntity.ok(result.get());
    }

    @DeleteMapping("/users")//
    public ResponseEntity<?> deleteUserById(@RequestParam String id) {
        UUID uuid = UUID.fromString(id.replace("\"", "").trim());
        if(userService.deleteUserById(uuid)){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

}
