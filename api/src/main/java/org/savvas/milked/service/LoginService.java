package org.savvas.milked.service;

import org.savvas.milked.controller.request.LoginRequest;
import org.savvas.milked.domain.MilkedUser;
import org.savvas.milked.domain.MilkedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.savvas.milked.controller.error.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginService {

    private final MilkedUserRepository milkedUserRepository;
    private UserService userService;

    @Autowired
    public LoginService(MilkedUserRepository milkedUserRepository){
        this.milkedUserRepository = milkedUserRepository;
    }

    public MilkedUser login(LoginRequest loginRequest) {
        List<MilkedUser> userList = milkedUserRepository.findByEmail(loginRequest.getEmail());
        for(MilkedUser user : userList){
            if(user == null){
                throw new NotFoundException("User Not found");
            }
        }
        MilkedUser user = userList.get(0);
        return user;
    }
}
