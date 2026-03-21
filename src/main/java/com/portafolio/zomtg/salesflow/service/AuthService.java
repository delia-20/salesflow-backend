package com.portafolio.zomtg.salesflow.service;

import com.portafolio.zomtg.salesflow.exception.InvalidCredentials;
import com.portafolio.zomtg.salesflow.model.dto.AuthResponseDTO;
import com.portafolio.zomtg.salesflow.model.dto.LoginDTO;
import com.portafolio.zomtg.salesflow.model.entities.Client;
import com.portafolio.zomtg.salesflow.model.entities.User;
import com.portafolio.zomtg.salesflow.repository.AuthRepository;
import com.portafolio.zomtg.salesflow.repository.AuthRepositoryClients;
import com.portafolio.zomtg.salesflow.security.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class AuthService {

    AuthRepository authRepository;
    AuthRepositoryClients authRepositoryClients;
    JWTService jwtService;
    private final PasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
    @Autowired
    public AuthService(AuthRepository authRepository, JWTService jwtService, AuthRepositoryClients authRepositoryClients) {
        this.authRepository = authRepository;
        this.jwtService = jwtService;
        this.authRepositoryClients = authRepositoryClients;
    }

    public AuthResponseDTO loginCredentials(LoginDTO request) {
        System.out.println("Login credentials for email: " + request.getUsername());
        User user = findByEmail(request.getUsername()).orElseThrow(()->
                new InvalidCredentials("Invalid email or password"));

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new InvalidCredentials("Invalid password");
            }
            String token=jwtService.getToken(user);
            return new AuthResponseDTO(token,user);

    }
//    public String loginToken(User user) {
//        return jwtService.getToken(user);
//    }


    // 🟢 Find by username OR email
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
            if(passwordEncoder.matches(request.getPassword(), cli.getPassword())){
                return client;
            }
        }else {
            return Optional.empty();
        }
        return client;

    }

    public String loginTokenClient(Client client) {
        return jwtService.getTokenClient(client);
    }
}
