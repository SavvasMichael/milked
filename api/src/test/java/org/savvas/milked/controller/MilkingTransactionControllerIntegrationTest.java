package org.savvas.milked.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.savvas.milked.controller.MilkedTestUtils.randomEmail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.savvas.milked.Application;
import org.savvas.milked.controller.request.GroupRequest;
import org.savvas.milked.controller.request.GroupInviteRequest;
import org.savvas.milked.controller.request.RegistrationRequest;
import org.savvas.milked.controller.request.MilkingRequest;
import org.savvas.milked.domain.MilkingTransaction;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")

public class MilkingTransactionControllerIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    private final TestRestTemplate rest = new TestRestTemplate();

    @Test
    public void createShaftReturns201ResponseCode() {
        //given
        String baseUrl = "http://localhost:" + port;
        String shaftUrl = baseUrl + "/shaft";
        //when
        MilkingRequest milkingRequest = new MilkingRequest(1L, 2L, 5L, 1);
        ResponseEntity<String> shaftResponse = rest.postForEntity(URI.create(shaftUrl), milkingRequest, String.class);
        String shaftLocation = shaftResponse.getHeaders().getFirst("Location");
        //then
        assertEquals(201, shaftResponse.getStatusCode().value());
        assertThat(shaftLocation).matches("^/shaft/\\d+$");
    }

    @Test
    public void createMilkingTransactionReturnsCorrectBodyFieldElements() {
        //given
        String baseUrl = "http://localhost:" + port;
        String createGroupUserUrl = baseUrl + "/group-user";
        String registrationUrl = baseUrl + "/registration";
        String activateGroupUserUrl = "/activate";
        String createGroupUrl = baseUrl + "/group";
        String shaftUrl = baseUrl + "/shaft";
        //when
        RegistrationRequest milkerRegistrationRequest = new RegistrationRequest(randomEmail(), "savvas", "password");
        RegistrationRequest milkeeRegistrationRequest = new RegistrationRequest(randomEmail(), "michael", "password");

        ResponseEntity<String> milkerRegistrationResponse = rest.postForEntity(URI.create(registrationUrl), milkerRegistrationRequest, String.class);
        ResponseEntity<String> milkeeRegistrationResponse = rest.postForEntity(URI.create(registrationUrl), milkeeRegistrationRequest, String.class);

        String milkerPath = milkerRegistrationResponse.getHeaders().getFirst("Location");
        String milkeePath = milkeeRegistrationResponse.getHeaders().getFirst("Location");

        ResponseEntity<MilkedUser> milkerResponse = rest.getForEntity(URI.create(baseUrl + milkerPath), MilkedUser.class);
        ResponseEntity<MilkedUser> milkeeResponse = rest.getForEntity(URI.create(baseUrl + milkeePath), MilkedUser.class);
        MilkedUser milker = milkerResponse.getBody();
        MilkedUser milkee = milkeeResponse.getBody();

        GroupRequest milkerGroupRequest = new GroupRequest(milkerResponse.getBody().getId(), "SavvasGroup");
        ResponseEntity<String> milkerGroupResponse = rest.postForEntity(URI.create(createGroupUrl), milkerGroupRequest, String.class);
        String milkerGroupLocation = milkerGroupResponse.getHeaders().getFirst("Location");
        String milkerGroupUrl = baseUrl + milkerGroupLocation;
        MilkingGroup milkerGroup = rest.getForEntity((URI.create(milkerGroupUrl)), MilkingGroup.class).getBody();

        GroupInviteRequest groupInviteRequest = new GroupInviteRequest(milkerGroup.getId(), milkee.getId());
        ResponseEntity<String> inviteGroupUserResponse = rest.postForEntity(URI.create(createGroupUserUrl), groupInviteRequest, String.class);
        String inviteGroupUserLocation = inviteGroupUserResponse.getHeaders().getFirst("Location");
        rest.postForEntity(URI.create(baseUrl + inviteGroupUserLocation + activateGroupUserUrl), groupInviteRequest, String.class);

        MilkingRequest milkingRequest = new MilkingRequest(milker.getId(), milkee.getId(), milkerGroup.getId(), 100);
        ResponseEntity<String> milkingResponse = rest.postForEntity(URI.create(shaftUrl), milkingRequest, String.class);
        String milkedTransactionLocation = milkingResponse.getHeaders().getFirst("Location");
        ResponseEntity<MilkingTransaction> milkedTransactionResponse = rest.getForEntity((URI.create(baseUrl + milkedTransactionLocation)), MilkingTransaction.class);
        //then
        assertEquals(201, milkerGroupResponse.getStatusCode().value());
        assertThat(milkedTransactionLocation).matches("^/shaft/\\d+$");
        MilkingTransaction milkingTransaction = milkedTransactionResponse.getBody();
        assertEquals("Unexpected Group Id", milkerGroup.getId(), milkingTransaction.getGroup().getId());
        assertEquals("Unexpected Milker Id", milker.getId(), milkingTransaction.getMilker().getId());
        assertEquals("Unexpected Milkee Id", milkee.getId(), milkingTransaction.getMilkee().getId());
        assertEquals("Unexpected Transaction Amount", 100, milkingTransaction.getAmount().intValue());
    }
}