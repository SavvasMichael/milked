package org.savvas.shafted.controller;

import org.savvas.shafted.domain.User;
import org.savvas.shafted.domain.UserRepository;
import org.savvas.shafted.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivationController {
    private UserRepository repository;

    @Autowired
    public ActivationController(UserRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/activation/{uuid}", method = RequestMethod.POST)
    public User activateUser(@PathVariable("uuid") String uuid) {
        User fetchedUser = repository.findByUuid(uuid);
        fetchedUser.setActivated(true);
        User savedUser = repository.save(fetchedUser);
        return savedUser;
    }
}
