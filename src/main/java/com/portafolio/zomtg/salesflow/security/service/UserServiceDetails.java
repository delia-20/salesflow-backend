package com.portafolio.zomtg.salesflow.security.service;

import com.portafolio.zomtg.salesflow.model.entities.Client;
import com.portafolio.zomtg.salesflow.model.entities.User;
import com.portafolio.zomtg.salesflow.repository.AuthRepository;
import com.portafolio.zomtg.salesflow.repository.AuthRepositoryClients;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceDetails implements UserDetailsService {
    private  final AuthRepository authRepository;
    private final AuthRepositoryClients authRepositoryClients;

    public UserServiceDetails(AuthRepository authRepository, AuthRepositoryClients authRepositoryClients) {
        this.authRepository = authRepository;
        this.authRepositoryClients = authRepositoryClients;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=authRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("user not found"));
        return  new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }

    public UserDetails loadUserByUsernameClients(String username) throws UsernameNotFoundException {
        Client client = authRepositoryClients.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("client not found"));
        return new org.springframework.security.core.userdetails.User(
                client.getUsername(),
                client.getPassword(),
        List.of(new SimpleGrantedAuthority("ROLE_" + client.getRole().name()))
        );
    }
}
