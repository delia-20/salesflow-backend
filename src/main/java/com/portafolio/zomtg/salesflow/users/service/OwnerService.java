package com.portafolio.zomtg.salesflow.users.service;

import com.portafolio.zomtg.salesflow.exception.InvalidCredentials;
import com.portafolio.zomtg.salesflow.exception.ObjectNotFound;
import com.portafolio.zomtg.salesflow.exception.UnauthorizedOperationException;
import com.portafolio.zomtg.salesflow.store.entity.Store;
import com.portafolio.zomtg.salesflow.users.dto.EmployeeUpdateRequest;
import com.portafolio.zomtg.salesflow.users.dto.UserResponse;
import com.portafolio.zomtg.salesflow.users.entity.User;
import com.portafolio.zomtg.salesflow.users.enums.Role;
import com.portafolio.zomtg.salesflow.store.repository.StoreRepository;
import com.portafolio.zomtg.salesflow.users.mapper.UserMapper;
import com.portafolio.zomtg.salesflow.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class OwnerService {
    UserRepository userRepository;
    StoreRepository storeRepository;
    @Autowired
    UserMapper userMapper;
    private final PasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
    @Autowired
    public OwnerService(UserRepository userRepository, StoreRepository storeRepository) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
    }

    public String getOwnerId(String username) {
        User owner =userRepository.findUserByUsername(username).orElseThrow(() ->new InvalidCredentials("Username not found"));

        return owner.getOwnerId().toString();

    }
    public String getStoreId(String username) {
        User owner =userRepository.findUserByUsername(username).orElseThrow(() ->new InvalidCredentials("Username not found"));
        return owner.getStoreId().toString();

    }
    public List<User> getEmployeesByStoreId(String username,UUID storeId) {
        User owner = userRepository.findUserByUsername(username).orElseThrow();
        Store store = storeRepository.findStoreById(storeId).orElseThrow();
        if (store.getOwnerId().equals(owner.getOwnerId())) {
            List<User> employees=userRepository.findByStoreIdAndRole(storeId, Role.EMPLOYEE);
            return employees;
        }

        throw  new UnauthorizedOperationException("Invalid access");
    }


    public List<User> getEmployeesByOwnerId(String username) {
        User owner = userRepository.findUserByUsername(username).orElseThrow(()->new InvalidCredentials("Username not found"));
        UUID ownerId = owner.getOwnerId();
        List<User> employees=userRepository.findByOwnerIdAndRole(ownerId,Role.EMPLOYEE);
        return employees;
    }

    public UserResponse getEmployeeById(String username, UUID id) {
        User owner = userRepository.findUserByUsername(username).orElseThrow(()->new InvalidCredentials("Username not found"));
        UUID ownerId = owner.getOwnerId();
        User employee=userRepository.findUserById(id).orElseThrow(()->new ObjectNotFound("Employee not found"));
        if(employee.getOwnerId().equals(ownerId)) {
            return userMapper.toResponse(employee);
        }else  {
            throw  new UnauthorizedOperationException("Invalid access, this employee does not belong to this store");
        }
    }


    public UserResponse updateEmployee(String username, UUID employeeId,EmployeeUpdateRequest request) {
        User owner = userRepository.findUserByUsername(username).orElseThrow();
        User employe=userRepository.findUserById(employeeId).orElseThrow(()-> new ObjectNotFound("user not found"));
        if(!owner.getOwnerId().equals(employe.getOwnerId())) {
            throw new UnauthorizedOperationException("invalid access, this employee does not belong to this store");
        }
        if(request.name()!=null && !request.name().equals("")) {
            employe.setName(request.name());
        }
        if(request.surname()!=null && !request.surname().equals("")) {
            employe.setSurname(request.surname());
        }
        if(request.password()!=null && !request.password().equals("")) {
            employe.setPassword(request.password());
        }
        if(request.password()!=null && !request.password().equals("")) {
            employe.setPassword(passwordEncoder.encode(request.password()));
        }
        if (request.storeId() != null) {
            Store store= storeRepository.findStoreById(request.storeId()).orElseThrow(()-> new ObjectNotFound("Store not found"));
            if(store.getOwnerId().equals(owner.getOwnerId())) {
                employe.setStoreId(request.storeId());
            }
        }
        userRepository.save(employe);

        return userMapper.toResponse(employe);
    }


}
