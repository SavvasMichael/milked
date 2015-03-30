package org.savvas.shafted.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.savvas.shafted.Application;
import org.savvas.shafted.controller.error.ErrorResponse;
import org.savvas.shafted.controller.request.GroupRequest;
import org.savvas.shafted.controller.request.GroupUserRequest;
import org.savvas.shafted.controller.request.GroupUserState;
import org.savvas.shafted.controller.request.RegistrationRequest;
import org.savvas.shafted.domain.GroupUser;
import org.savvas.shafted.domain.GroupUserRepository;
import org.savvas.shafted.domain.ShaftGroup;
import org.savvas.shafted.domain.ShaftUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URI;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")

public class GroupUserControllerIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    private final TestRestTemplate rest = new TestRestTemplate();

    @Test
    public void createGroupUserReturnsCreatedResponseCode() {
        //given
        String baseUrl = "http://localhost:" + port;
        String createGroupUserUrl = baseUrl + "/group-user";
        //when
        GroupUserRequest groupUserRequest = new GroupUserRequest(1L, 1L);
        ResponseEntity<String> createGroupUserResponse = rest.postForEntity(URI.create(createGroupUserUrl), groupUserRequest, String.class);
        String groupUserLocation = createGroupUserResponse.getHeaders().getFirst("Location");
        ResponseEntity<GroupUser> groupUserResponse = rest.getForEntity(URI.create(baseUrl + groupUserLocation), GroupUser.class);
        //then
        assertEquals(201, createGroupUserResponse.getStatusCode().value());
        assertEquals("/group-user/1", groupUserLocation);
        assertEquals(1L, (long) groupUserResponse.getBody().getUserId());
        assertEquals(1L, (long) groupUserResponse.getBody().getGroupId());
        assertEquals(GroupUserState.INVITED, groupUserResponse.getBody().getState());
    }

    @Test
    public void createGroupUserWithNullGroupIdReturns400ResponseCode() {
        //given
        String baseUrl = "http://localhost:" + port;
        String createGroupUserUrl = baseUrl + "/group-user";
        //when
        GroupUserRequest groupUserRequest = new GroupUserRequest(null, 1L);
        ResponseEntity<ErrorResponse> createGroupUserResponse = rest.postForEntity(URI.create(createGroupUserUrl), groupUserRequest, ErrorResponse.class);
        //then
        assertEquals("Unexpected Error Message", "groupId", createGroupUserResponse.getBody().getErrors().get(0));
    }

    @Test
    public void activateGroupUserChangesGroupUserStateToMember() {
        //given
        String baseUrl = "http://localhost:" + port;
        String createGroupUserUrl = baseUrl + "/group-user";
        String activateGroupUserUrl = "/activate";
        //when
        GroupUserRequest groupUserRequest = new GroupUserRequest(1L, 1L);
        ResponseEntity<String> createGroupUserResponse = rest.postForEntity(URI.create(createGroupUserUrl), groupUserRequest, String.class);
        String groupUserLocation = createGroupUserResponse.getHeaders().getFirst("Location");
        ResponseEntity<String> activateUserResponse = rest.postForEntity(URI.create(baseUrl + groupUserLocation + activateGroupUserUrl), groupUserRequest, String.class);
        ResponseEntity<GroupUser> groupUserResponse = rest.getForEntity(URI.create(baseUrl + groupUserLocation), GroupUser.class);
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
        RegistrationRequest request = new RegistrationRequest("michaelsavvas@ymail.com", "savvas", "password");
        ResponseEntity<String> registrationResponse = rest.postForEntity(URI.create(registrationUrl), request, String.class);
        String userPath = registrationResponse.getHeaders().getFirst("Location");
        ResponseEntity<ShaftUser> userResponse = rest.getForEntity(URI.create(baseUrl + userPath), ShaftUser.class);
        GroupRequest groupRequest = new GroupRequest(userResponse.getBody().getId(), "SavvasGroup");
        ResponseEntity<String> createGroupResponse = rest.postForEntity(URI.create(createGroupUrl), groupRequest, String.class);
        String groupLocation = createGroupResponse.getHeaders().getFirst("Location");
        String groupUrl = baseUrl + groupLocation;
        ResponseEntity<ShaftGroup> groupResponse = rest.getForEntity((URI.create(groupUrl)), ShaftGroup.class);
        GroupUserRequest groupUserRequest = new GroupUserRequest(groupResponse.getBody().getId(), groupResponse.getBody().getUserId());
        ResponseEntity<String> createGroupUserResponse = rest.postForEntity(URI.create(createGroupUserUrl), groupUserRequest, String.class);
        String groupUserLocation = createGroupUserResponse.getHeaders().getFirst("Location");
        rest.delete(URI.create(baseUrl + groupUserLocation));
        ResponseEntity<ErrorResponse> groupUserResponse = rest.getForEntity(URI.create(baseUrl + groupUserLocation), ErrorResponse.class);
        //then
        assertEquals(404, groupUserResponse.getStatusCode().value());
        assertEquals("Unexpected error message", "User is not a member of this group", groupUserResponse.getBody().getErrors().get(0));
    }
}