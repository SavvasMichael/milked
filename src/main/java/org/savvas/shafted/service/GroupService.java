package org.savvas.shafted.service;


import org.savvas.shafted.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class GroupService {
    private UserRepository repository;

    @Autowired
    public GroupService(UserRepository repository) {
        this.repository = repository;
    }

    public void createGroup() {

    }
}
