package org.savvas.shafted.service;

import org.savvas.shafted.controller.request.RegistrationRequest;
import org.savvas.shafted.domain.User;
import org.savvas.shafted.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository){
        this.repository = repository;
    }

    public void createUser(RegistrationRequest registrationRequest) {
        System.out.println("Registering User: " + registrationRequest);
        User user = new User(registrationRequest.getEmail(),registrationRequest.getName(),registrationRequest.getPassword());
        repository.save(user);
    }
}
