package org.savvas.shafted.controller;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.savvas.shafted.Application;
import org.savvas.shafted.controller.error.ErrorResponse;
import org.savvas.shafted.controller.request.GroupRequest;
import org.savvas.shafted.controller.request.GroupUserRequest;
import org.savvas.shafted.controller.request.RegistrationRequest;
import org.savvas.shafted.controller.request.ShaftRequest;
import org.savvas.shafted.domain.Shaft;
import org.savvas.shafted.domain.ShaftGroup;
import org.savvas.shafted.domain.ShaftUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.net.URI;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")

public class ShaftControllerIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    private final TestRestTemplate rest = new TestRestTemplate();

    @Test
    public void createShaftReturns201ResponseCode() {
        //given
        String baseUrl = "http://localhost:" + port;
        String shaftUrl = baseUrl + "/shaft";
        //when
        ShaftRequest shaftRequest = new ShaftRequest(1L, 2L, 5L, (BigDecimal.valueOf(1)));
        ResponseEntity<String> shaftResponse = rest.postForEntity(URI.create(shaftUrl), shaftRequest, String.class);
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
        ResponseEntity<ShaftUser> userResponse = rest.getForEntity(URI.create(baseUrl + userPath), ShaftUser.class);
        ResponseEntity<ShaftUser> userTwoResponse = rest.getForEntity(URI.create(baseUrl + userTwoUserPath), ShaftUser.class);
        GroupRequest groupRequest = new GroupRequest(userResponse.getBody().getId(), "SavvasGroup");
        ResponseEntity<String> createGroupResponse = rest.postForEntity(URI.create(createGroupUrl), groupRequest, String.class);
        String groupLocation = createGroupResponse.getHeaders().getFirst("Location");
        String groupUrl = baseUrl + groupLocation;
        ResponseEntity<ShaftGroup> groupResponse = rest.getForEntity((URI.create(groupUrl)), ShaftGroup.class);
        GroupUserRequest groupUserRequest = new GroupUserRequest(groupResponse.getBody().getId(), groupResponse.getBody().getUserId());
        ResponseEntity<String> createGroupUserResponse = rest.postForEntity(URI.create(createGroupUserUrl), groupUserRequest, String.class);
        String groupUserLocation = createGroupUserResponse.getHeaders().getFirst("Location");
        ResponseEntity<String> activateUserResponse = rest.postForEntity(URI.create(baseUrl + groupUserLocation + activateGroupUserUrl), groupUserRequest, String.class);
        ShaftRequest shaftRequest = new ShaftRequest(userTwoResponse.getBody().getId(), userResponse.getBody().getId(), groupResponse.getBody().getId(), BigDecimal.valueOf(1));
        ResponseEntity<String> createShaftResponse = rest.postForEntity(URI.create(shaftUrl), shaftRequest, String.class);
        String shaftLocation = createShaftResponse.getHeaders().getFirst("Location");
        ResponseEntity<Shaft> shaftResponse = rest.getForEntity((URI.create(baseUrl + shaftLocation)), Shaft.class);
        //then
        assertEquals(201, createGroupResponse.getStatusCode().value());
        assertEquals("/shaft/1", shaftLocation);
        assertEquals(1, (long) shaftResponse.getBody().getGroupId());
        assertEquals(1, (long) shaftResponse.getBody().getId());
        assertEquals(1, (long) shaftResponse.getBody().getShafteeId());
        assertEquals(2, (long) shaftResponse.getBody().getShafterId());
        BigDecimal amount = shaftResponse.getBody().getAmount();
        assertEquals(1.00, amount);
    }
}