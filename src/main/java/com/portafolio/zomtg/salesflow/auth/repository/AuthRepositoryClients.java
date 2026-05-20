package com.portafolio.zomtg.salesflow.auth.repository;

import com.portafolio.zomtg.salesflow.clients.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepositoryClients extends JpaRepository<Client, Integer> {
    Optional<Client> findByUsername(String username);
    Optional<Client> findClientByEmail(String email);

}
