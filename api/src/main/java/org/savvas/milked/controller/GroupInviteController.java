package org.savvas.milked.controller;

import org.savvas.milked.controller.error.ValidationException;
import org.savvas.milked.controller.request.EmailBodyRequest;
import org.savvas.milked.domain.GroupInvite;
import org.savvas.milked.domain.MilkedUser;
import org.savvas.milked.domain.MilkedUserRepository;
import org.savvas.milked.domain.MilkingGroup;
import org.savvas.milked.service.GroupInviteService;
import org.savvas.milked.service.UpdateUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;

@RestController
public class GroupInviteController {
    private static final Logger LOG = LoggerFactory.getLogger(GroupInviteController.class);
    private final GroupInviteService groupInviteService;
    private final UpdateUserService updateUserService;
    private final MilkedUserRepository milkedUserRepository;

    @Autowired
    public GroupInviteController(@RequestBody GroupInviteService groupInviteService, UpdateUserService updateUserService, MilkedUserRepository milkedUserRepository) {
        this.groupInviteService = groupInviteService;
        this.updateUserService = updateUserService;
        this.milkedUserRepository = milkedUserRepository;
    }

    @RequestMapping(value = "/group/{groupId}/invite", method = RequestMethod.POST)
    public ResponseEntity inviteUser(@Valid @RequestBody EmailBodyRequest request, @PathVariable("groupId") Long groupId) throws UnsupportedEncodingException {
        String createdUrl = "";
        String email = request.getEmail();
        MilkedUser user = groupInviteService.inviteGroupUser(groupId, email);
        if ("".equals(user.getName())) {
            createdUrl = "/user/" + user.getId() + "/group/" + groupId;
        } else {
            createdUrl = "/existing-user/" + user.getId() + "/group/" + groupId;
        }
        URI groupUserLocationURI = URI.create(createdUrl);
        return ResponseEntity.created(groupUserLocationURI).build();
    }
    @RequestMapping(value = "/user/{userId}/group/invite", method = RequestMethod.GET)
    public List<GroupInvite> getGroupInvite(@PathVariable("userId") Long userId) {
        return groupInviteService.getGroupInvites(userId);
    }

    @RequestMapping(value = "/existing-user/{uuid}/group/{groupId}/accept", method = RequestMethod.GET)
    public ResponseEntity acceptGroupInvite(@PathVariable("uuid") String uuid, @PathVariable("groupId") Long groupId) {
        MilkingGroup group = groupInviteService.acceptGroupInvite(uuid, groupId);
        return ResponseEntity.ok(group);
    }

    @RequestMapping(value = "/user/{uuid}/group/{groupId}/accept", method = RequestMethod.GET)
    public ResponseEntity acceptGroupInviteAndActivate(@PathVariable("uuid") String uuid, @PathVariable("groupId") Long groupId) {
        MilkedUser fetchedMilkedUser = milkedUserRepository.findByUuid(uuid);
        if (fetchedMilkedUser == null) {
            throw new ValidationException("Invalid UUID");
        }
        fetchedMilkedUser.setActivated(true);
        LOG.warn("Activated the user " + fetchedMilkedUser.getName());
        MilkingGroup group = groupInviteService.acceptGroupInviteForAnonymous(uuid, groupId);
        MilkedUser savedMilkedUser = milkedUserRepository.save(fetchedMilkedUser);
        MilkedUser user = milkedUserRepository.findByUuid(uuid);
        return ResponseEntity.ok(group);
    }

    @RequestMapping(value = "/user/{userId}/group/{groupId}/decline", method = RequestMethod.DELETE)
    public ResponseEntity declineInvitation(@PathVariable("userId") Long userId, @PathVariable("groupId") Long groupId) {
        groupInviteService.declineInvitation(userId, groupId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/existing-user/{userId}/group/{groupId}/decline", method = RequestMethod.DELETE)
    public ResponseEntity declineInvitationForExisting(@PathVariable("userId") Long userId, @PathVariable("groupId") Long groupId) {
        groupInviteService.declineInvitation(userId, groupId);
        return ResponseEntity.ok().build();
    }
}
