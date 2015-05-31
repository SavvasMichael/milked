package org.savvas.milked.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.savvas.milked.MilkedApiApplication;
import org.savvas.milked.controller.request.EmailBodyRequest;
import org.savvas.milked.controller.request.LeaveGroupRequest;
import org.savvas.milked.domain.MilkedUser;
import org.savvas.milked.domain.MilkingGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.UnsupportedEncodingException;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.savvas.milked.controller.MilkedTestUtils.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MilkedApiApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")

public class UserControllerIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    private final TestRestTemplate rest = new TestRestTemplate();

    @Test
    public void checkGetUserGroup(){
        //given
        String baseUrl = "http://localhost:" + port;
        MilkedUser joe = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvas", "password");
        givenTheMilkingGroup(rest, baseUrl, joe.getId(), "joeGroup");
        givenTheMilkingGroup(rest, baseUrl, joe.getId(), "joeGroup2");

        MilkedUser savvas = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvas", "password");
        givenTheMilkingGroup(rest, baseUrl, savvas.getId(), "savvasGroup");
        givenTheMilkingGroup(rest, baseUrl, savvas.getId(), "savvasGroup2");

        //when
        String savvasGroupsUrl = baseUrl + "/user/" + savvas.getId() + "/group";
        ResponseEntity<MilkingGroup[]> savvasGroupsResponse = rest.getForEntity(URI.create(savvasGroupsUrl), MilkingGroup[].class);
        MilkingGroup[] savvasGroups = savvasGroupsResponse.getBody();
        //then
        assertThat(savvasGroups).hasSize(2);

        String joeGroupsUrl = baseUrl + "/user/" + joe.getId() + "/group";
        ResponseEntity<MilkingGroup[]> joeGroupsResponse = rest.getForEntity(URI.create(savvasGroupsUrl), MilkingGroup[].class);
        MilkingGroup[] joeGroups = joeGroupsResponse.getBody();
        //then
        assertThat(joeGroups).hasSize(2);
    }

    @Test
    public void checkLeaveGroupRemovesUserFromTheGroup() throws UnsupportedEncodingException {
        //given
        String baseUrl = "http://localhost:" + port;
        MilkedUser savvas = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "Savvas", "pass");
        MilkedUser joe = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "Joe", "pass");
        MilkingGroup group = givenTheMilkingGroup(rest, baseUrl, savvas.getId(), "savvasGroup");
        givenTheUserHasJoinedTheGroup(rest, baseUrl, joe.getEmail(), group.getId());

        String leaveGroupUrl = baseUrl + "/user/" + joe.getId() + "/group/" + group.getId() + "/leave";
        LeaveGroupRequest leaveGroupRequest = new LeaveGroupRequest(joe.getId(), group.getId());
        String joeGroupUrl = baseUrl + "/user/" + joe.getId() + "/group";

        //when
        MilkingGroup[] joeGroupsResponse1 = rest.getForEntity(URI.create(joeGroupUrl), MilkingGroup[].class).getBody();
        assertThat(joeGroupsResponse1).isNotEmpty();

        ResponseEntity<MilkingGroup> leaveGroupResponse = rest.postForEntity(URI.create(leaveGroupUrl), leaveGroupRequest, MilkingGroup.class);
        MilkingGroup[] joeGroupsResponse = rest.getForEntity(URI.create(joeGroupUrl), MilkingGroup[].class).getBody();

        //then
        assertEquals("Unexpected Response Code", 200, leaveGroupResponse.getStatusCode().value());
        assertThat(joeGroupsResponse).isEmpty();
    }

    @Test
    public void checkGetGroupUsers() throws UnsupportedEncodingException {
        //given
        String baseUrl = "http://localhost:" + port;
        MilkedUser savvas = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "Savvas", "pass");
        MilkingGroup group = givenTheMilkingGroup(rest, baseUrl, savvas.getId(), "savvasGroup");
        MilkedUser joe = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "Joe", "pass");
        givenTheUserHasJoinedTheGroup(rest, baseUrl, joe.getEmail(), group.getId());
        String getGroupUsersUrl = baseUrl + "/group/" + group.getId() + "/users";
        //when
        MilkedUser[] groupUsersResponse = rest.getForEntity(URI.create(getGroupUsersUrl), MilkedUser[].class).getBody();
        //then
        assertThat(groupUsersResponse).hasSize(2);
    }

    @Test
    public void checkForgotPasswordRecoversIt() {
        //given
        String baseUrl = "http://localhost:" + port;
        MilkedUser registeredUser = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvas", "12345");
        String passwordRecoveryUrl = baseUrl + "/user/forgot-password";
        EmailBodyRequest request = new EmailBodyRequest(registeredUser.getEmail());
        //when
        String password = rest.postForEntity(URI.create(passwordRecoveryUrl), request, String.class).getBody();
        //then
        assertEquals("Unexpected Password", "12345", password);
    }

//TODO:FIX THIS
//    @Test
//    public void checkUpdatesUserDetailsOnNewUserInvitation() throws UnsupportedEncodingException {
//        //given
//        String baseUrl = "http://localhost:" + port;
//        MilkedUser savvas = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "Savvas", "pass");
//        MilkingGroup savvasGroup = givenTheMilkingGroup(rest, baseUrl, savvas.getId(), "savvasGroup");
//        givenTheUserHasJoinedTheGroup(rest, baseUrl, "savvas.a.michael@gmail.com", savvasGroup.getId());
//        String getGroupUsersUrl = baseUrl + "/group/" + savvasGroup.getId() + "/users";
//        UpdateUserRequest request = new UpdateUserRequest("Invited Savvas", "pass123");
//        //when
//        MilkedUser[] groupUsersResponse = rest.getForEntity(URI.create(getGroupUsersUrl), MilkedUser[].class).getBody();
//        String updateUrl = baseUrl + "/user/" + groupUsersResponse[1].getId() + "/update";
//        ResponseEntity<MilkedUser> user = rest.postForEntity(URI.create(updateUrl), request, MilkedUser.class);
//        MilkedUser[] groupUsersResponseAfterUpdate = rest.getForEntity(URI.create(getGroupUsersUrl), MilkedUser[].class).getBody();
//        //then
//        assertThat(groupUsersResponseAfterUpdate[1].getName()).matches("Invited Savvas");
//        assertThat(groupUsersResponseAfterUpdate[1].getPassword()).matches("pass123");
//    }
}