package org.savvas.milked.controller;

import org.savvas.milked.controller.error.ValidationException;
import org.savvas.milked.domain.MilkedUser;
import org.savvas.milked.domain.MilkingGroup;
import org.savvas.milked.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public MilkedUser getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    @RequestMapping(value = "/user/{id}/group", method = RequestMethod.GET)
    public List<MilkingGroup> getUserGroups(@PathVariable("id") Long id) {
        return userService.getUserGroups(id);
    }

    @RequestMapping(value = "/group/{groupId}/users", method = RequestMethod.GET)
    public List<MilkedUser> getGroupUsers(@PathVariable("groupId") Long groupId) {
        List<MilkedUser> groupUserList = userService.getGroupUsers(groupId);
        return groupUserList;
    }

    @RequestMapping(value = "/user/{userId}/group/{groupId}/leave", method = RequestMethod.POST)
    public ResponseEntity leaveGroup(@PathVariable("userId") Long userId, @PathVariable("groupId") Long groupId) {
        userService.leaveGroup(userId, groupId);
        return ResponseEntity.ok().build();
    }
}
