package org.savvas.milked.controller;

import org.savvas.milked.controller.request.GroupRequest;
import org.savvas.milked.controller.request.RegistrationRequest;
import org.savvas.milked.domain.MilkedUser;
import org.savvas.milked.domain.MilkingGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
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
        MilkedUser milkedUser = milkerResponse.getBody();
        return rest.getForEntity(URI.create(baseUrl + "/activation/" + milkedUser.getUuid()), MilkedUser.class).getBody();
    }

    public static MilkedUser givenTheUserIsRegistered(RestTemplate rest, String baseUrl, String name, String password) {
        RegistrationRequest milkerRegistrationRequest = new RegistrationRequest(randomEmail(), name, password);
        ResponseEntity<String> milkerRegistrationResponse = rest.postForEntity(URI.create(baseUrl + "/registration"), milkerRegistrationRequest, String.class);
        String milkerPath = milkerRegistrationResponse.getHeaders().getFirst("Location");
        return rest.getForEntity(URI.create(baseUrl + milkerPath), MilkedUser.class).getBody();
    }

    public static MilkingGroup givenTheMilkingGroup(RestTemplate rest, String baseUrl, Long milkedUserId, String groupName) {
        GroupRequest milkerGroupRequest = new GroupRequest(milkedUserId, groupName);
        ResponseEntity<String> milkerGroupResponse = rest.postForEntity(URI.create(baseUrl +  "/group"), milkerGroupRequest, String.class);
        String milkerGroupLocation = milkerGroupResponse.getHeaders().getFirst("Location");
        String milkerGroupUrl = baseUrl + milkerGroupLocation;
        return rest.getForEntity((URI.create(milkerGroupUrl)), MilkingGroup.class).getBody();
    }

    public static void givenTheUserHasJoinedTheGroup(RestTemplate rest, String baseUrl, String email, Long mikedGroupId) throws UnsupportedEncodingException {
        String inviteGroupUserLocation = givenTheUserHasBeenInvitedToTheGroup(rest, baseUrl, email, mikedGroupId);
        givenTheUserHasAcceptedTheInvitationToTheGroup(rest, baseUrl, inviteGroupUserLocation);
    }

    public static String givenTheUserHasBeenInvitedToTheGroup(RestTemplate rest, String baseUrl, String email, Long milkedGroupId) throws UnsupportedEncodingException {
        String inviteUserUrl = baseUrl + "/group/" + milkedGroupId + "/invite";
        Map<String, String> emailBody = new HashMap<>();
        emailBody.put("email", email);
        ResponseEntity<String> inviteGroupUserResponse = rest.postForEntity(URI.create(inviteUserUrl), emailBody, String.class);
        return inviteGroupUserResponse.getHeaders().getFirst("Location");
    }

    public static void givenTheUserHasAcceptedTheInvitationToTheGroup(RestTemplate rest, String baseUrl, String inviteGroupUserLocation) {
        rest.postForEntity(URI.create(baseUrl + inviteGroupUserLocation + "/accept"), null, String.class);
    }
}
