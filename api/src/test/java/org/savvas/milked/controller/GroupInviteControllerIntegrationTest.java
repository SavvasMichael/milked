package org.savvas.milked.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.savvas.milked.MilkedApiApplication;
import org.savvas.milked.controller.error.ErrorResponse;
import org.savvas.milked.domain.GroupInvite;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.savvas.milked.controller.MilkedTestUtils.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MilkedApiApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")

public class GroupInviteControllerIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    private final TestRestTemplate rest = new TestRestTemplate();
    private String baseUrl;

    @Before
    public void setUp() throws Exception {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    public void invitingAUserReturnsA201WithNoBody() {
        //given
        MilkedUser groupOwner = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvas", "password");
        MilkedUser friend = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "friend", "password");
        MilkingGroup milkedGroup = givenTheMilkingGroup(rest, baseUrl, groupOwner.getId(), "savvasgroup");

        //when
        Map<String, String> userEmail = new HashMap<>();
        userEmail.put("email", friend.getEmail());
        String createGroupUserUrl = baseUrl + "/group/" + milkedGroup.getId() + "/invite/";
        ResponseEntity<String> createGroupUserResponse = rest.postForEntity(URI.create(createGroupUserUrl), userEmail, String.class);

        //then
        String invitesUrl = baseUrl + "/user/" + friend.getId() + "/group/invite";
        ResponseEntity<GroupInvite[]> getInvitesResponse = rest.getForEntity(URI.create(invitesUrl), GroupInvite[].class);

        assertEquals(201, createGroupUserResponse.getStatusCode().value());
        assertEquals(200, getInvitesResponse.getStatusCode().value());

        GroupInvite[] friendInvites = getInvitesResponse.getBody();
        assertThat(friendInvites).hasSize(1);
        assertThat(friendInvites[0].getGroupId()).isEqualTo(milkedGroup.getId());
        assertThat(friendInvites[0].getUserId()).isEqualTo(friend.getId());
    }

    @Test
    public void invitingAUserWithNullGroupIdReturns400ResponseCode() {
        //given
        MilkedUser milkedUser = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvas", "password");
        //when
        Map<String, String> userEmail = new HashMap<>();
        userEmail.put("email", milkedUser.getEmail());
        String createGroupUserUrl = baseUrl + "/group/" + null + "/invite/";
        ResponseEntity<ErrorResponse> createGroupUserResponse = rest.postForEntity(URI.create(createGroupUserUrl), userEmail, ErrorResponse.class);
        //then
        assertEquals("Unexpected Error", 400, createGroupUserResponse.getStatusCode().value());
    }

    @Test
    public void acceptingGroupInviteAddsTheUserToTheGroup() throws UnsupportedEncodingException {
        //given
        MilkedUser owner = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvas", "password");
        MilkedUser friend = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvassfriend", "password");
        MilkingGroup milkingGroup = givenTheMilkingGroup(rest, baseUrl, owner.getId(), "savvasgroup");
        String invitationLocation = givenTheUserHasBeenInvitedToTheGroup(rest, baseUrl, friend.getEmail(), milkingGroup.getId());

        //when
        String acceptInvitationUrl = baseUrl + invitationLocation + "/accept";
        ResponseEntity<MilkingGroup> acceptInvitationResponse = rest.getForEntity(URI.create(acceptInvitationUrl), MilkingGroup.class);

        //then
        GroupInvite[] invites = rest.getForEntity(baseUrl + "/user/" + friend.getId() + "/group/invite", GroupInvite[].class).getBody();
        assertThat(acceptInvitationResponse.getStatusCode().value()).isEqualTo(200);
        MilkingGroup group = acceptInvitationResponse.getBody();
        List<MilkedUser> fetchedMilkingUsers = group.getMilkedUsers();
        assertThat(fetchedMilkingUsers).hasSize(2);
        assertThat(fetchedMilkingUsers).contains(friend);
        assertThat(invites).isEmpty();
    }

    @Test
    public void decliningInvitationDeletesInvitation() throws UnsupportedEncodingException {
        //given
        MilkedUser owner = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvas", "password");
        MilkedUser friend = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvassfriend", "password");
        MilkingGroup milkingGroup = givenTheMilkingGroup(rest, baseUrl, owner.getId(), "savvasgroup");
        String invitationLocation = givenTheUserHasBeenInvitedToTheGroup(rest, baseUrl, friend.getEmail(), milkingGroup.getId());

        //when
        String acceptInvitationUrl = baseUrl + invitationLocation + "/decline";
        rest.delete(URI.create(acceptInvitationUrl));

        //then
        String getGroup = baseUrl + "/group/" + milkingGroup.getId();
        MilkingGroup group = rest.getForEntity(URI.create(getGroup), MilkingGroup.class).getBody();
        GroupInvite[] invites = rest.getForEntity(baseUrl + "/user/" + friend.getId() + "/group/invite", GroupInvite[].class).getBody();
        List<MilkedUser> fetchedMilkingUsers = group.getMilkedUsers();
        assertThat(fetchedMilkingUsers).hasSize(1);
        assertThat(fetchedMilkingUsers.get(0)).isEqualToComparingFieldByField(owner);
        assertThat(invites).isEmpty();
    }

    @Test
    public void aNewUserGetsCreatedAfterInvitingANonExistentUser() throws UnsupportedEncodingException {
        //given
        MilkedUser owner = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvas", "password");
        MilkingGroup milkingGroup = givenTheMilkingGroup(rest, baseUrl, owner.getId(), "savvasgroup");
        givenTheUserHasBeenInvitedToTheGroup(rest, baseUrl, randomEmail(), milkingGroup.getId());
        Map<String, String> userEmail = new HashMap<>();
        userEmail.put("email", "unregistered@email.com");
        String createGroupUserUrl = baseUrl + "/group/" + milkingGroup.getId() + "/invite/";
        //when
        ResponseEntity<String> createGroupUserResponse = rest.postForEntity(URI.create(createGroupUserUrl), userEmail, String.class);
        String location = createGroupUserResponse.getHeaders().getFirst("Location");
        location = location.replaceFirst("/\\d+$", "");
        GroupInvite[] invites = rest.getForEntity(baseUrl + location + "/invite", GroupInvite[].class).getBody();

        //then
        assertEquals(201, createGroupUserResponse.getStatusCode().value());
        assertThat(invites).isNotEmpty();
    }

    @Test
    public void invitingAMemberReturnsErrorResponseCode() throws UnsupportedEncodingException {
        //given
        MilkedUser user = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "Savvas", "pass");
        MilkingGroup group = givenTheMilkingGroup(rest, baseUrl, user.getId(), "SavvasGroup");
        String inviteUrl = baseUrl + "/user/" + user.getId() + "/group/invite";
        //when
        ResponseEntity<MilkedUser> groupUsersResponse = rest.postForEntity(URI.create(inviteUrl), null, MilkedUser.class);
        //then
        assertEquals("Unexpected Response Code", 405, groupUsersResponse.getStatusCode().value());
    }
}