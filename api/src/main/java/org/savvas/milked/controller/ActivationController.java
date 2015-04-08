package org.savvas.milked.controller;

import org.savvas.milked.domain.MilkedUser;
import org.savvas.milked.domain.MilkedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivationController {
    private MilkedUserRepository repository;

    @Autowired
    public ActivationController(MilkedUserRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/activation/{uuid}", method = RequestMethod.POST)
    public MilkedUser activateUser(@PathVariable("uuid") String uuid) {
        MilkedUser fetchedMilkedUser = repository.findByUuid(uuid);
        fetchedMilkedUser.setActivated(true);
        MilkedUser savedMilkedUser = repository.save(fetchedMilkedUser);
        return savedMilkedUser;
    }
}
