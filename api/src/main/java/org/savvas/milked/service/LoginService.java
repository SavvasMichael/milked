package org.savvas.milked.service;

import org.savvas.milked.controller.error.UnauthorizedException;
import org.savvas.milked.controller.request.LoginRequest;
import org.savvas.milked.domain.MilkedUser;
import org.savvas.milked.domain.MilkedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginService {

    private final MilkedUserRepository milkedUserRepository;

    @Autowired
    public LoginService(MilkedUserRepository milkedUserRepository) {
        this.milkedUserRepository = milkedUserRepository;
    }

    public MilkedUser login(LoginRequest loginRequest) {
        MilkedUser user = validateLogin(loginRequest);
        return user;
    }

    private MilkedUser validateLogin(LoginRequest loginRequest) {
        MilkedUser user = milkedUserRepository.findByEmail(loginRequest.getEmail());
        if (user == null) {
            throw new UnauthorizedException("UNRECOGNIZED_EMAIL");
        }
        if (!Objects.equals(user.getPassword(), loginRequest.getPassword())) {
            throw new UnauthorizedException("INVALID_PASSWORD");
        }
        if (user.isActivated() == false) {
            throw new UnauthorizedException("NOT_ACTIVATED");
        }
        return user;
    }
}
