package com.portafolio.zomtg.salesflow.auth.service;

import com.portafolio.zomtg.salesflow.exception.InvalidCredentials;
import com.portafolio.zomtg.salesflow.auth.dto.AuthResponseDTO;
import com.portafolio.zomtg.salesflow.auth.dto.LoginDTO;
import com.portafolio.zomtg.salesflow.clients.entity.Client;
import com.portafolio.zomtg.salesflow.users.dto.UserResponse;
import com.portafolio.zomtg.salesflow.users.entity.User;
import com.portafolio.zomtg.salesflow.auth.repository.AuthRepository;
import com.portafolio.zomtg.salesflow.auth.repository.AuthRepositoryClients;
import com.portafolio.zomtg.salesflow.security.service.JWTService;
import com.portafolio.zomtg.salesflow.users.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class AuthService {

    AuthRepository authRepository;
    AuthRepositoryClients authRepositoryClients;
    UserMapper userMapper;
    JWTService jwtService;
    private final PasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
    @Autowired
    public AuthService(AuthRepository authRepository,
                       JWTService jwtService,
                       AuthRepositoryClients authRepositoryClients,
                       UserMapper userMapper) {
        this.authRepository = authRepository;
        this.jwtService = jwtService;
        this.authRepositoryClients = authRepositoryClients;
        this.userMapper = userMapper;
    }

    public AuthResponseDTO loginCredentials(LoginDTO request) {
        System.out.println("Login credentials for email: " + request.getUsername());
        User user = findByEmail(request.getUsername()).orElseThrow(()->
                new InvalidCredentials("Invalid email or password"));

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new InvalidCredentials("Invalid password");
            }
            String token=jwtService.getToken(user);
            UserResponse response = userMapper.toResponse(user);
            return new AuthResponseDTO(token,response);

    }
//

 

    public Optional<User> findByEmail(String usernameOrEmail) {
        Optional<User> user = authRepository.findByEmail(usernameOrEmail);
        if (user.isEmpty()) {
            user = authRepository.findByUsername(usernameOrEmail);
        }
        return user;
    }


    public Optional<Client> loginCredentialsClient(LoginDTO request) {
        System.out.println("Login credentials for email: " + request.getUsername());
        Optional<Client> client =authRepositoryClients.findClientByEmail(request.getUsername());
        if(client.isEmpty()){
            client=authRepositoryClients.findByUsername(request.getUsername());
        }
        if(client.isPresent()){
            Client cli=client.get();
            if(passwordEncoder.matches(request.getPassword(), cli.getPassword()))
                return client;
            throw new InvalidCredentials("Invalid password");

        }else {
            throw new InvalidCredentials("Invalid email or password");
        }


    }

    public String loginTokenClient(Client client) {
        return jwtService.getTokenClient(client);
    }
}
