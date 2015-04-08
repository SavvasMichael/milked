package org.savvas.milked.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.savvas.milked.Application;
import org.savvas.milked.controller.error.ErrorResponse;
import org.savvas.milked.controller.request.GroupRequest;
import org.savvas.milked.controller.request.GroupInviteRequest;
import org.savvas.milked.controller.request.GroupUserState;
import org.savvas.milked.controller.request.RegistrationRequest;
import org.savvas.milked.domain.GroupInvite;
import org.savvas.milked.domain.MilkingGroup;
import org.savvas.milked.domain.MilkedUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.savvas.milked.controller.MilkedTestUtils.randomEmail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")

public class GroupInviteControllerIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    private final TestRestTemplate rest = new TestRestTemplate();

    @Test
    public void createGroupUserInviteReturnsCreatedResponseCode() {
        //given
        String baseUrl = "http://localhost:" + port;
        String createGroupUserUrl = baseUrl + "/group-user";
        //when
        GroupInviteRequest groupInviteRequest = new GroupInviteRequest(1L, 1L);
        ResponseEntity<String> createGroupUserResponse = rest.postForEntity(URI.create(createGroupUserUrl), groupInviteRequest, String.class);
        String groupUserLocation = createGroupUserResponse.getHeaders().getFirst("Location");
        ResponseEntity<GroupInvite> groupUserResponse = rest.getForEntity(URI.create(baseUrl + groupUserLocation), GroupInvite.class);
        GroupInvite groupInvite = groupUserResponse.getBody();
        //then
        assertEquals(201, createGroupUserResponse.getStatusCode().value());
        assertThat(groupUserLocation).matches("^/group-user/\\d+$");
        assertEquals(1L, groupInvite.getUserId().longValue());
        assertEquals(1L, groupInvite.getGroupId().longValue());
        assertEquals(GroupUserState.INVITED, groupInvite.getState());
    }

    @Test
    public void createGroupUserWithNullGroupIdReturns400ResponseCode() {
        //given
        String baseUrl = "http://localhost:" + port;
        String createGroupUserUrl = baseUrl + "/group-user";
        //when
        GroupInviteRequest groupInviteRequest = new GroupInviteRequest(null, 1L);
        ResponseEntity<ErrorResponse> createGroupUserResponse = rest.postForEntity(URI.create(createGroupUserUrl), groupInviteRequest, ErrorResponse.class);
        //then
        assertEquals("Unexpected Error Message", "groupId", createGroupUserResponse.getBody().getErrors().get(0));
    }

    // TODO: change this to actually add the user to the group
    @Test
    public void activateGroupUserChangesGroupUserStateToMember() {
        //given
        String baseUrl = "http://localhost:" + port;
        String createGroupUserUrl = baseUrl + "/group-user";
        String activateGroupUserUrl = "/activate";
        //when
        GroupInviteRequest groupInviteRequest = new GroupInviteRequest(1L, 1L);
        ResponseEntity<String> createGroupUserResponse = rest.postForEntity(URI.create(createGroupUserUrl), groupInviteRequest, String.class);
        String groupUserLocation = createGroupUserResponse.getHeaders().getFirst("Location");
        ResponseEntity<String> activateUserResponse = rest.postForEntity(URI.create(baseUrl + groupUserLocation + activateGroupUserUrl), groupInviteRequest, String.class);
        ResponseEntity<GroupInvite> groupUserResponse = rest.getForEntity(URI.create(baseUrl + groupUserLocation), GroupInvite.class);
        //then
        assertEquals("/group-user/1/activate", activateUserResponse.getHeaders().getFirst("Location"));
        assertEquals(GroupUserState.MEMBER, groupUserResponse.getBody().getState());
    }

    //Null Pointer because of validation, had to change test, see below
//    @Test
//    public void checkDeletesGroupUserRemovesUser() {
//        //given
//        String baseUrl = "http://localhost:" + port;
//        String createGroupUserUrl = baseUrl + "/group-user";
//        //when
//        GroupUserRequest groupUserRequest = new GroupUserRequest(1L, 1L);
//        ResponseEntity<ErrorResponse> createGroupUserResponse = rest.postForEntity(URI.create(createGroupUserUrl), groupUserRequest, ErrorResponse.class);
//        String groupUserLocation = createGroupUserResponse.getHeaders().getFirst("Location");
//        rest.delete(URI.create(baseUrl + groupUserLocation));
//        ResponseEntity<GroupUser> groupUserResponse = rest.getForEntity(URI.create(baseUrl + groupUserLocation), GroupUser.class);
//        //then
//        assertEquals(404, groupUserResponse.getStatusCode().value());
//        assertEquals("Unexpected error message", "User is not a member of this group", createGroupUserResponse.getBody().getErrors().get(0));
//    }
    @Test
    public void checkDeleteUserRemovesUserWithValidData() {
        //given
        String baseUrl = "http://localhost:" + port;
        String createGroupUserUrl = baseUrl + "/group-user";
        String registrationUrl = baseUrl + "/registration";
        String createGroupUrl = baseUrl + "/group";
        //when
        RegistrationRequest request = new RegistrationRequest(randomEmail(), "savvas", "password");
        ResponseEntity<String> registrationResponse = rest.postForEntity(URI.create(registrationUrl), request, String.class);
        String userPath = registrationResponse.getHeaders().getFirst("Location");
        ResponseEntity<MilkedUser> userResponse = rest.getForEntity(URI.create(baseUrl + userPath), MilkedUser.class);
        GroupRequest groupRequest = new GroupRequest(userResponse.getBody().getId(), "SavvasGroup");
        ResponseEntity<String> createGroupResponse = rest.postForEntity(URI.create(createGroupUrl), groupRequest, String.class);
        String groupLocation = createGroupResponse.getHeaders().getFirst("Location");
        String groupUrl = baseUrl + groupLocation;
        ResponseEntity<MilkingGroup> groupResponse = rest.getForEntity((URI.create(groupUrl)), MilkingGroup.class);

        MilkingGroup milkingGroup = groupResponse.getBody();
        GroupInviteRequest groupInviteRequest = new GroupInviteRequest(milkingGroup.getId(), milkingGroup.getOwner().getId());
        ResponseEntity<String> createGroupUserResponse = rest.postForEntity(URI.create(createGroupUserUrl), groupInviteRequest, String.class);
        String groupUserLocation = createGroupUserResponse.getHeaders().getFirst("Location");
        rest.delete(URI.create(baseUrl + groupUserLocation));
        ResponseEntity<ErrorResponse> groupUserResponse = rest.getForEntity(URI.create(baseUrl + groupUserLocation), ErrorResponse.class);
        //then
        assertEquals(404, groupUserResponse.getStatusCode().value());
        assertEquals("Unexpected error message", "User is not a member of this group", groupUserResponse.getBody().getErrors().get(0));
    }

}