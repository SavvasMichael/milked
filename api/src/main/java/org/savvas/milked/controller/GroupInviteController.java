package org.savvas.milked.controller;

import org.savvas.milked.domain.GroupInvite;
import org.savvas.milked.domain.MilkedUser;
import org.savvas.milked.domain.MilkingGroup;
import org.savvas.milked.service.GroupInviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
public class GroupInviteController {

    private final GroupInviteService groupInviteService;

    @Autowired
    public GroupInviteController(@RequestBody GroupInviteService groupInviteService) {
        this.groupInviteService = groupInviteService;
    }

    @RequestMapping(value = "/group/{groupId}/invite", method = RequestMethod.POST)
    public ResponseEntity inviteUser(@RequestBody Map<String, String> emailBody, @PathVariable("groupId") Long groupId) throws UnsupportedEncodingException {
        String email = emailBody.get("email");
        MilkedUser user = groupInviteService.inviteGroupUser(groupId, email);
        String createdUrl = "/user/" + user.getId() + "/group/" + groupId;
        URI groupUserLocationURI = URI.create(createdUrl);
        return ResponseEntity.created(groupUserLocationURI).build();
    }

    @RequestMapping(value = "/user/{userId}/group/{groupId}/accept", method = RequestMethod.POST)
    public ResponseEntity acceptGroupInvite(@PathVariable("userId") Long userId, @PathVariable("groupId") Long groupId) {
        MilkingGroup group = groupInviteService.acceptGroupInvite(userId, groupId);
        return ResponseEntity.ok(group);
    }

    @RequestMapping(value = "/user/{userId}/group/invite", method = RequestMethod.GET)
    public List<GroupInvite> getGroupInvite(@PathVariable("userId") Long userId) {
        return groupInviteService.getGroupInvites(userId);
    }

    @RequestMapping(value = "/user/{userId}/group/{groupId}/decline", method = RequestMethod.DELETE)
    public ResponseEntity declineInvitation(@PathVariable("userId") Long userId, @PathVariable("groupId") Long groupId) {
        groupInviteService.declineInvitation(userId, groupId);
        return ResponseEntity.ok().build();
    }
}
