package com.portafolio.zomtg.salesflow.security.service;

import com.portafolio.zomtg.salesflow.clients.entity.Client;
import com.portafolio.zomtg.salesflow.users.entity.User;
import com.portafolio.zomtg.salesflow.auth.repository.AuthRepository;
import com.portafolio.zomtg.salesflow.auth.repository.AuthRepositoryClients;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
