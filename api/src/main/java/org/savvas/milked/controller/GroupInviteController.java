package org.savvas.milked.controller;

import org.savvas.milked.controller.error.ValidationException;
import org.savvas.milked.controller.request.UpdateUserRequest;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity inviteUser(@RequestBody Map<String, String> emailBody, @PathVariable("groupId") Long groupId) throws UnsupportedEncodingException {
        String email = emailBody.get("email");
        MilkedUser user = groupInviteService.inviteGroupUser(groupId, email);
        String createdUrl = "/user/" + user.getId() + "/group/" + groupId;
        URI groupUserLocationURI = URI.create(createdUrl);
        return ResponseEntity.created(groupUserLocationURI).build();
    }
    @RequestMapping(value = "/user/{userId}/group/invite", method = RequestMethod.GET)
    public List<GroupInvite> getGroupInvite(@PathVariable("userId") Long userId) {
        return groupInviteService.getGroupInvites(userId);
    }

    @RequestMapping(value = "/existing-user/{userId}/group/{groupId}/accept", method = RequestMethod.GET)
    public ResponseEntity acceptGroupInvite(@PathVariable("userId") Long userId, @PathVariable("groupId") Long groupId) {
        MilkingGroup group = groupInviteService.acceptGroupInvite(userId, groupId);
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
        MilkedUser savedMilkedUser = milkedUserRepository.save(fetchedMilkedUser);
        MilkingGroup group = groupInviteService.acceptGroupInviteForAnonymous(uuid, groupId);
        MilkedUser user = milkedUserRepository.findByUuid(uuid);
        return ResponseEntity.ok(group);
    }

    @RequestMapping(value = "/user/{userId}/group/{groupId}/decline", method = RequestMethod.DELETE)
    public ResponseEntity declineInvitation(@PathVariable("userId") Long userId, @PathVariable("groupId") Long groupId) {
        groupInviteService.declineInvitation(userId, groupId);
        return ResponseEntity.ok().build();
    }
}
