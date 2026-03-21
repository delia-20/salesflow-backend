package com.portafolio.zomtg.salesflow.controller;

import com.portafolio.zomtg.salesflow.model.dto.AuthResponseDTO;
import com.portafolio.zomtg.salesflow.model.dto.LoginDTO;
import com.portafolio.zomtg.salesflow.model.entities.Client;
import com.portafolio.zomtg.salesflow.model.entities.User;
import com.portafolio.zomtg.salesflow.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/auth/")
public class AuthController {

    AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseEntity<?> auth(@RequestBody LoginDTO request) {
        System.out.println("email: " + request.getUsername());
        AuthResponseDTO result =authService.loginCredentials(request);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

@GetMapping("login/client")
    public ResponseEntity<?> authClient(@RequestBody LoginDTO request) {
        System.out.println("email: " + request.getUsername());
        Optional<Client>  client=authService.loginCredentialsClient(request);
        if(client.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }
        String token=authService.loginTokenClient(client.get());
        return ResponseEntity.ok(Map.of(
                "token",token,
                "user",client
        ));
    }

     /*
    @PutMapping("api/todos/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable Long id, @RequestBody Todo todo){
        todo.setId(id);
        Todo result = todoService.update(todo);
        if(result == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("Todo item with id "+ id+" does not exist."));
        }
       return ResponseEntity.ok(result);
    }
     */

}
