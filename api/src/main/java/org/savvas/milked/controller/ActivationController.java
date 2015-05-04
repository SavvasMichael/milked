package org.savvas.milked.controller;

import org.savvas.milked.controller.error.ValidationException;
import org.savvas.milked.domain.MilkedUser;
import org.savvas.milked.domain.MilkedUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivationController {
    private static final Logger LOG = LoggerFactory.getLogger(ActivationController.class);
    private MilkedUserRepository repository;

    @Autowired
    public ActivationController(MilkedUserRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/activation/{uuid}", method = RequestMethod.GET)
    public MilkedUser activateUser(@PathVariable("uuid") String uuid) {
        MilkedUser fetchedMilkedUser = repository.findByUuid(uuid);
        if ( fetchedMilkedUser == null ) {
            throw new ValidationException("Invalid UUID");
        }
        fetchedMilkedUser.setActivated(true);
        LOG.warn("Activated the user " + fetchedMilkedUser.getName());
        MilkedUser savedMilkedUser = repository.save(fetchedMilkedUser);
        return savedMilkedUser;
    }
}
