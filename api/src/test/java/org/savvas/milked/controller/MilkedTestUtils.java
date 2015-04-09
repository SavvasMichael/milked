package org.savvas.milked.controller;

import org.savvas.milked.controller.request.GroupInviteRequest;
import org.savvas.milked.controller.request.GroupRequest;
import org.savvas.milked.controller.request.RegistrationRequest;
import org.savvas.milked.domain.MilkedUser;
import org.savvas.milked.domain.MilkingGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.UUID;

public class MilkedTestUtils {

    public static String randomEmail() {
        return UUID.randomUUID().toString()+"@ymail.com";
    }

    public static MilkedUser givenTheUserIsRegisteredAndActivated(RestTemplate rest, String baseUrl, String name, String password) {
        RegistrationRequest milkerRegistrationRequest = new RegistrationRequest(randomEmail(), name, password);
        ResponseEntity<String> milkerRegistrationResponse = rest.postForEntity(URI.create(baseUrl + "/registration"), milkerRegistrationRequest, String.class);
        String milkerPath = milkerRegistrationResponse.getHeaders().getFirst("Location");
        ResponseEntity<MilkedUser> milkerResponse = rest.getForEntity(URI.create(baseUrl + milkerPath), MilkedUser.class);
        return milkerResponse.getBody();
    }

    public static MilkingGroup givenTheMilkingGroup(RestTemplate rest, String baseUrl, Long milkedUserId, String groupName) {
        GroupRequest milkerGroupRequest = new GroupRequest(milkedUserId, groupName);
        ResponseEntity<String> milkerGroupResponse = rest.postForEntity(URI.create(baseUrl +  "/group"), milkerGroupRequest, String.class);
        String milkerGroupLocation = milkerGroupResponse.getHeaders().getFirst("Location");
        String milkerGroupUrl = baseUrl + milkerGroupLocation;
        return rest.getForEntity((URI.create(milkerGroupUrl)), MilkingGroup.class).getBody();
    }

    public static void givenTheUserHasJoinedTheGroup(RestTemplate rest, String baseUrl, Long milkedUserId, Long mikedGroupId) {
        String inviteGroupUserLocation = givenTheUserHasBeenInvitedToTheGroup(rest, baseUrl, milkedUserId, mikedGroupId);
        givenTheUserHasAcceptedTheInvitationToTheGroup(rest, baseUrl, inviteGroupUserLocation);
    }

    public static String givenTheUserHasBeenInvitedToTheGroup(RestTemplate rest, String baseUrl, Long milkedUserId, Long milkedGroupId) {
        GroupInviteRequest groupInviteRequest = new GroupInviteRequest(milkedUserId, milkedGroupId);
        ResponseEntity<String> inviteGroupUserResponse = rest.postForEntity(URI.create(baseUrl + "/group-user"), groupInviteRequest, String.class);
        return inviteGroupUserResponse.getHeaders().getFirst("Location");
    }

    public static void givenTheUserHasAcceptedTheInvitationToTheGroup(RestTemplate rest, String baseUrl, String inviteGroupUserLocation) {
        rest.postForEntity(URI.create(baseUrl + inviteGroupUserLocation + "/activate"), null, String.class);
    }
}
