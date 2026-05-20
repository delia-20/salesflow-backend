package com.portafolio.zomtg.salesflow.clients.service;

import com.portafolio.zomtg.salesflow.clients.entity.Client;
import com.portafolio.zomtg.salesflow.users.enums.Role;
import com.portafolio.zomtg.salesflow.clients.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

   private ClientRepository clientRepository;
   private final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
   @Autowired
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client createClient(Client client) {
       client.setPassword(passwordEncoder.encode(client.getPassword()));
       client.setRole(Role.CUSTOMER);
       return clientRepository.save(client);

    }

    public List<Client> getClients() {
       List<Client> clients;
        clients = clientRepository.findAll();
        return clients;

    }

}
