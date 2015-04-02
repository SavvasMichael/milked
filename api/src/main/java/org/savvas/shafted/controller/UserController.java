package org.savvas.shafted.controller;

import org.savvas.shafted.domain.ShaftUser;
import org.savvas.shafted.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ShaftUser getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }
}