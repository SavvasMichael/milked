package org.savvas.shafted.controller;

import org.savvas.shafted.controller.request.GroupRequest;
import org.savvas.shafted.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupController {
    private UserRepository repository;

    @Autowired
    public GroupController(UserRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/group", method = RequestMethod.POST)
    public void createGroup(@RequestBody GroupRequest groupRequest) {

    }
}
