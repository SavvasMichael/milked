package org.savvas.milked.service;

import org.savvas.milked.controller.request.UpdateUserRequest;
import org.savvas.milked.domain.MilkedUser;
import org.savvas.milked.domain.MilkedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserService {

    private final MilkedUserRepository milkedUserRepository;

    @Autowired
    public UpdateUserService(MilkedUserRepository milkedUserRepository) {
        this.milkedUserRepository = milkedUserRepository;
    }

    public MilkedUser updateUserDetails(Long userId, UpdateUserRequest updateUserRequest) {
        MilkedUser fetchedUser = milkedUserRepository.findOne(userId);
        fetchedUser.setName(updateUserRequest.getName());
        fetchedUser.setPassword(updateUserRequest.getPassword());
        MilkedUser updatedUser = milkedUserRepository.save(fetchedUser);
        return updatedUser;
    }
}
