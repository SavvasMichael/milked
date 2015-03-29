package org.savvas.shafted.controller;

import org.savvas.shafted.controller.error.ValidationException;
import org.savvas.shafted.controller.request.GroupUserRequest;
import org.savvas.shafted.domain.GroupRepository;
import org.savvas.shafted.domain.GroupUser;
import org.savvas.shafted.service.GroupUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class GroupUserController {

    private final GroupUserService groupUserService;

    @Autowired
    public GroupUserController(@RequestBody GroupUserService groupUserService) {
        this.groupUserService = groupUserService;
    }

    @RequestMapping(value = "/group-user", method = RequestMethod.POST)
    public ResponseEntity createGroupUser(@RequestBody @Valid GroupUserRequest groupUserRequest, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new ValidationException("groupId");
        }
        Long groupId = groupUserService.createGroupUser(groupUserRequest);
        URI groupUserLocationURI = URI.create("/group-user/" + groupId);
        return ResponseEntity.created(groupUserLocationURI).build();
    }

    @RequestMapping(value = "/group-user/{id}", method = RequestMethod.GET)
    public GroupUser getGroupUser(@PathVariable("id") Long id) {
        return groupUserService.getGroup(id);
    }

    @RequestMapping(value = "/group-user/{id}/activate", method = RequestMethod.POST)
    public ResponseEntity activateGroupUser(@PathVariable("id") Long id) {

    }
}
