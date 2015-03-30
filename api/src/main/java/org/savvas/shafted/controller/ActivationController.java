package org.savvas.shafted.controller;

import org.savvas.shafted.domain.ShaftUser;
import org.savvas.shafted.domain.UserRepository;
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
    public ShaftUser activateUser(@PathVariable("uuid") String uuid) {
        ShaftUser fetchedShaftUser = repository.findByUuid(uuid);
        fetchedShaftUser.setActivated(true);
        ShaftUser savedShaftUser = repository.save(fetchedShaftUser);
        return savedShaftUser;
    }
}
