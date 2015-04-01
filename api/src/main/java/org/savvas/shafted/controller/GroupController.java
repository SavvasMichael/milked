package org.savvas.shafted.controller;

import org.savvas.shafted.controller.error.ValidationException;
import org.savvas.shafted.controller.request.GroupRequest;
import org.savvas.shafted.domain.Shaft;
import org.savvas.shafted.domain.ShaftGroup;
import org.savvas.shafted.domain.ShaftRepository;
import org.savvas.shafted.domain.ShaftUser;
import org.savvas.shafted.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.security.acl.Group;
import java.util.List;

@RestController
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @RequestMapping(value = "/group", method = RequestMethod.POST)
    public ResponseEntity createGroup(@RequestBody @Valid GroupRequest groupRequest, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new ValidationException(validation.getFieldErrors());
        }
        Long groupId = groupService.createGroup(groupRequest);
        URI groupLocationURI = URI.create("/group/" + groupId);
        return ResponseEntity.created(groupLocationURI).build();
    }
    @RequestMapping(value = "/group/{id}", method = RequestMethod.GET)
    public ShaftGroup getGroup(@PathVariable("id") Long id) {
        return groupService.getGroup(id);
    }

    @RequestMapping(value = "/user-groups/{id}", method = RequestMethod.GET)
    public List<ShaftGroup> getUserGroups(@PathVariable("id") Long id){
        return groupService.getUserGroups(id);
    }
}
