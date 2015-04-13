package org.savvas.milked.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.savvas.milked.Application;
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

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.savvas.milked.controller.MilkedTestUtils.givenTheMilkingGroup;
import static org.savvas.milked.controller.MilkedTestUtils.givenTheUserHasBeenInvitedToTheGroup;
import static org.savvas.milked.controller.MilkedTestUtils.givenTheUserIsRegisteredAndActivated;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
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
        String createGroupUserUrl = baseUrl + "/group/" + milkedGroup.getId() + "/invite/" + friend.getId();
        ResponseEntity<String> createGroupUserResponse = rest.postForEntity(URI.create(createGroupUserUrl), null, String.class);

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
        String createGroupUserUrl = baseUrl + "/group/" + null + "/invite/" + milkedUser.getId();
        ResponseEntity<ErrorResponse> createGroupUserResponse = rest.postForEntity(URI.create(createGroupUserUrl), null, ErrorResponse.class);
        //then
        assertEquals("Unexpected Error", 400, createGroupUserResponse.getStatusCode().value());
    }

    @Test
    public void acceptingGroupInviteAddsTheUserToTheGroup() {
        //given
        MilkedUser owner = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvas", "password");
        MilkedUser friend = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvassfriend", "password");
        MilkingGroup milkingGroup = givenTheMilkingGroup(rest, baseUrl, owner.getId(), "savvasgroup");
        String invitationLocation = givenTheUserHasBeenInvitedToTheGroup(rest, baseUrl, friend.getId(), milkingGroup.getId());

        //when
        String acceptInvitationUrl = baseUrl + invitationLocation + "/accept";
        ResponseEntity<MilkingGroup> acceptInvitationResponse = rest.postForEntity(URI.create(acceptInvitationUrl), null, MilkingGroup.class);

        //then
        GroupInvite[] invites = rest.getForEntity(baseUrl + "/user/" + friend.getId() + "/group/invite", GroupInvite[].class).getBody();
        assertThat(acceptInvitationResponse.getStatusCode().value()).isEqualTo(200);
        MilkingGroup group = acceptInvitationResponse.getBody();
        List<MilkedUser> fetchedMilkingUsers = group.getMilkedUsers();
        assertThat(fetchedMilkingUsers).hasSize(2);
        assertThat(fetchedMilkingUsers.get(1)).isEqualToComparingFieldByField(friend);
        assertThat(invites).isEmpty();
    }

    @Test
    public void decliningInvitationDeletesInvitation() {
        //given
        MilkedUser owner = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvas", "password");
        MilkedUser friend = givenTheUserIsRegisteredAndActivated(rest, baseUrl, "savvassfriend", "password");
        MilkingGroup milkingGroup = givenTheMilkingGroup(rest, baseUrl, owner.getId(), "savvasgroup");
        String invitationLocation = givenTheUserHasBeenInvitedToTheGroup(rest, baseUrl, friend.getId(), milkingGroup.getId());

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

}