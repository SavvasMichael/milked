package org.savvas.milked.controller;

import org.savvas.milked.controller.error.ValidationException;
import org.savvas.milked.controller.request.GroupRequest;
import org.savvas.milked.domain.MilkingGroup;
import org.savvas.milked.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class MilkingGroupController {

    private final GroupService groupService;

    @Autowired
    public MilkingGroupController(GroupService groupService) {
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
    public MilkingGroup getGroup(@PathVariable("id") Long id) {
        return groupService.getGroup(id);
    }

    @RequestMapping(value = "/user-groups/{id}", method = RequestMethod.GET)
    public List<MilkingGroup> getUserGroups(@PathVariable("id") Long id) {
        return groupService.getUserGroups(id);
    }
}
