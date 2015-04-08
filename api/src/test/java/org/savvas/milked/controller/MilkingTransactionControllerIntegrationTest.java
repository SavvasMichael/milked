package org.savvas.milked.controller;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.savvas.milked.Application;
import org.savvas.milked.controller.request.GroupRequest;
import org.savvas.milked.controller.request.GroupUserRequest;
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
        assertEquals("/shaft/1", shaftLocation);
    }

    @Test
    public void createShaftReturnsCorrectBodyFieldElements() {
        //given
        String baseUrl = "http://localhost:" + port;
        String createGroupUserUrl = baseUrl + "/group-user";
        String registrationUrl = baseUrl + "/registration";
        String activateGroupUserUrl = "/activate";
        String createGroupUrl = baseUrl + "/group";
        String shaftUrl = baseUrl + "/shaft";
        //when
        RegistrationRequest request = new RegistrationRequest("michaelsavvas@ymail.com", "savvas", "password");
        RegistrationRequest userTwoRequest = new RegistrationRequest("savvas.a.michael@gmail.com", "michael", "password");

        ResponseEntity<String> registrationResponse = rest.postForEntity(URI.create(registrationUrl), request, String.class);
        ResponseEntity<String> userTwoRegistrationResponse = rest.postForEntity(URI.create(registrationUrl), userTwoRequest, String.class);

        String userPath = registrationResponse.getHeaders().getFirst("Location");
        String userTwoUserPath = userTwoRegistrationResponse.getHeaders().getFirst("Location");

        ResponseEntity<MilkedUser> userResponse = rest.getForEntity(URI.create(baseUrl + userPath), MilkedUser.class);
        ResponseEntity<MilkedUser> userTwoResponse = rest.getForEntity(URI.create(baseUrl + userTwoUserPath), MilkedUser.class);

        GroupRequest groupRequest = new GroupRequest(userResponse.getBody().getId(), "SavvasGroup");
        ResponseEntity<String> createGroupResponse = rest.postForEntity(URI.create(createGroupUrl), groupRequest, String.class);
        String groupLocation = createGroupResponse.getHeaders().getFirst("Location");
        String groupUrl = baseUrl + groupLocation;
        ResponseEntity<MilkingGroup> groupResponse = rest.getForEntity((URI.create(groupUrl)), MilkingGroup.class);

        GroupUserRequest groupUserRequest = new GroupUserRequest(groupResponse.getBody().getId(), groupResponse.getBody().getOwner().getId());
        ResponseEntity<String> createGroupUserResponse = rest.postForEntity(URI.create(createGroupUserUrl), groupUserRequest, String.class);
        String groupUserLocation = createGroupUserResponse.getHeaders().getFirst("Location");
        rest.postForEntity(URI.create(baseUrl + groupUserLocation + activateGroupUserUrl), groupUserRequest, String.class);

        MilkingRequest milkingRequest = new MilkingRequest(userTwoResponse.getBody().getId(), userResponse.getBody().getId(), groupResponse.getBody().getId(), 100);
        ResponseEntity<String> createShaftResponse = rest.postForEntity(URI.create(shaftUrl), milkingRequest, String.class);
        String shaftLocation = createShaftResponse.getHeaders().getFirst("Location");
        ResponseEntity<MilkingTransaction> shaftResponse = rest.getForEntity((URI.create(baseUrl + shaftLocation)), MilkingTransaction.class);
        //then
        assertEquals(201, createGroupResponse.getStatusCode().value());
        assertEquals("/shaft/1", shaftLocation);
        MilkingTransaction milkingTransaction = shaftResponse.getBody();
        assertEquals("Unexpected Group Id", 1L, milkingTransaction.getGroup().getId().longValue());
        assertEquals("Unexpected Milking Transaction Id", 1L, milkingTransaction.getId().longValue());
        assertEquals("Unexpected Milker Id", 2L, milkingTransaction.getMilker().getId().longValue());
        assertEquals("Unexpected Milkee Id", 1L, milkingTransaction.getMilkee().getId().longValue());
        assertEquals("Unexpected Transaction Amount", 100, milkingTransaction.getAmount().intValue());
    }
}