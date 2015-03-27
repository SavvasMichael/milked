package org.savvas.shafted.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.savvas.shafted.Application;
import org.savvas.shafted.controller.error.ErrorResponse;
import org.savvas.shafted.controller.request.GroupRequest;
import org.savvas.shafted.controller.request.RegistrationRequest;
import org.savvas.shafted.domain.Group;
import org.savvas.shafted.domain.User;
import org.savvas.shafted.service.GroupService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URI;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
public class GroupControllerIntegrationTest {


    @Value("${local.server.port}")
    private int port;

    private final TestRestTemplate rest = new TestRestTemplate();

    @Test
    public void createGroupReturnsCreatedResponseCode() {
        //given
        String baseUrl = "http://localhost:" + port;
        String createGroupUrl = baseUrl + "/group";
        String registrationUrl = baseUrl + "/registration";
        RegistrationRequest request = new RegistrationRequest("michaelsavvas@ymail.com", "savvas", "password");
        //when
        ResponseEntity<String> registrationResponse = rest.postForEntity(URI.create(registrationUrl), request, String.class);
        String userPath = registrationResponse.getHeaders().getFirst("Location");
        ResponseEntity<User> userResponse = rest.getForEntity(URI.create(baseUrl + userPath), User.class);
        GroupRequest groupRequest = new GroupRequest(userResponse.getBody().getId(), "SavvasGroup");
        ResponseEntity<String> createGroupResponse = rest.postForEntity(URI.create(createGroupUrl), groupRequest, String.class);
        //then
        assertEquals(201, createGroupResponse.getStatusCode().value());
    }

    @Test
    public void createGroupWithNullNameReturnsBadRequestResponseCode() {
        //given
        String baseUrl = "http://localhost:" + port;
        String createGroupUrl = baseUrl + "/group";
        String registrationUrl = baseUrl + "/registration";
        RegistrationRequest request = new RegistrationRequest("michaelsavvas@ymail.com", "savvas", "password");
        //when
        ResponseEntity<String> registrationResponse = rest.postForEntity(URI.create(registrationUrl), request, String.class);
        String userPath = registrationResponse.getHeaders().getFirst("Location");
        ResponseEntity<User> userResponse = rest.getForEntity(URI.create(baseUrl + userPath), User.class);
        GroupRequest groupRequest = new GroupRequest(userResponse.getBody().getId(), null);
        ResponseEntity<ErrorResponse> createGroupResponse = rest.postForEntity(URI.create(createGroupUrl), groupRequest, ErrorResponse.class);
        //then
        assertEquals("Unexpected Error Message", 400, createGroupResponse.getStatusCode().value());
    }

    @Test
    public void createGroupWithMissingNameReturnsBadRequestResponseCode() {
        //given
        String baseUrl = "http://localhost:" + port;
        String createGroupUrl = baseUrl + "/group";
        String registrationUrl = baseUrl + "/registration";
        RegistrationRequest request = new RegistrationRequest("michaelsavvas@ymail.com", "savvas", "password");
        //when
        ResponseEntity<String> registrationResponse = rest.postForEntity(URI.create(registrationUrl), request, String.class);
        String userPath = registrationResponse.getHeaders().getFirst("Location");
        ResponseEntity<User> userResponse = rest.getForEntity(URI.create(baseUrl + userPath), User.class);
        GroupRequest groupRequest = new GroupRequest(userResponse.getBody().getId(), "");
        ResponseEntity<ErrorResponse> createGroupResponse = rest.postForEntity(URI.create(createGroupUrl), groupRequest, ErrorResponse.class);
        //then
        assertEquals("Unexpected Error Message", 400, createGroupResponse.getStatusCode().value());
    }

    @Test
    public void savesGroupWhenCreated() {
        //given
        String baseUrl = "http://localhost:" + port;
        String createGroupUrl = baseUrl + "/group";
        String registrationUrl = baseUrl + "/registration";
        RegistrationRequest request = new RegistrationRequest("michaelsavvas@ymail.com", "savvas", "password");
        //when
        ResponseEntity<String> registrationResponse = rest.postForEntity(URI.create(registrationUrl), request, String.class);
        String userPath = registrationResponse.getHeaders().getFirst("Location");
        ResponseEntity<User> userResponse = rest.getForEntity(URI.create(baseUrl + userPath), User.class);
        GroupRequest groupRequest = new GroupRequest(userResponse.getBody().getId(), userResponse.getBody().getName());
        ResponseEntity<String> createGroupResponse = rest.postForEntity(URI.create(createGroupUrl), groupRequest, String.class);
        //then
    }

}