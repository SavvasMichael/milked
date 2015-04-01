package org.savvas.shafted.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.savvas.shafted.Application;
import org.savvas.shafted.controller.error.ErrorResponse;
import org.savvas.shafted.controller.request.GroupRequest;
import org.savvas.shafted.controller.request.RegistrationRequest;
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

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
public class ShaftGroupControllerIntegrationTest {


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
        ResponseEntity<ShaftUser> userResponse = rest.getForEntity(URI.create(baseUrl + userPath), ShaftUser.class);
        GroupRequest groupRequest = new GroupRequest(userResponse.getBody().getId(), "SavvasGroup");
        ResponseEntity<String> createGroupResponse = rest.postForEntity(URI.create(createGroupUrl), groupRequest, String.class);
        String groupLocation = createGroupResponse.getHeaders().getFirst("Location");
        String groupUrl = baseUrl + groupLocation;
        ResponseEntity<ShaftGroup> groupResponse = rest.getForEntity((URI.create(groupUrl)), ShaftGroup.class);
        //then
        assertEquals(201, createGroupResponse.getStatusCode().value());
        assertEquals("/group/1", groupLocation);
        assertEquals("SavvasGroup", groupResponse.getBody().getName());
        assertEquals(1L, (long) groupResponse.getBody().getUserId());
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
        ResponseEntity<ShaftUser> userResponse = rest.getForEntity(URI.create(baseUrl + userPath), ShaftUser.class);
        GroupRequest groupRequest = new GroupRequest(userResponse.getBody().getId(), null);
        ResponseEntity<ErrorResponse> createGroupResponse = rest.postForEntity(URI.create(createGroupUrl), groupRequest, ErrorResponse.class);
        //then
        assertEquals("Unexpected Error Message", 400, createGroupResponse.getStatusCode().value());
        assertEquals("Unexpected Error Message", "name", createGroupResponse.getBody().getErrors().get(0));
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
        ResponseEntity<ShaftUser> userResponse = rest.getForEntity(URI.create(baseUrl + userPath), ShaftUser.class);
        GroupRequest groupRequest = new GroupRequest(userResponse.getBody().getId(), "");
        ResponseEntity<ErrorResponse> createGroupResponse = rest.postForEntity(URI.create(createGroupUrl), groupRequest, ErrorResponse.class);
        //then
        assertEquals("Unexpected Error Message", 400, createGroupResponse.getStatusCode().value());
        assertEquals("Unexpected Error Message", "name", createGroupResponse.getBody().getErrors().get(0));

    }

    @Test
    public void createGroupWithNullIdReturnsBadRequestResponseCode() {
        //given
        String baseUrl = "http://localhost:" + port;
        String createGroupUrl = baseUrl + "/group";
        //when
        GroupRequest groupRequest = new GroupRequest(null, "savvasGroup");
        ResponseEntity<ErrorResponse> createGroupResponse = rest.postForEntity(URI.create(createGroupUrl), groupRequest, ErrorResponse.class);
        //then
        assertEquals("Unexpected Error Message", 400, createGroupResponse.getStatusCode().value());
        assertEquals("Unexpected Error Message", "userId", createGroupResponse.getBody().getErrors().get(0));
    }
    @Test
    public void checkGetUserGroups(){
        //given
        String baseUrl = "http://localhost:" + port;
        String getGroupUrl = baseUrl + "/user-groups/";
        String createGroupUrl = baseUrl + "/group";
        String registrationUrl = baseUrl + "/registration";
        RegistrationRequest request = new RegistrationRequest("michaelsavvas@ymail.com", "savvas", "password");
        //when
        ResponseEntity<String> registrationResponse = rest.postForEntity(URI.create(registrationUrl), request, String.class);
        String userPath = registrationResponse.getHeaders().getFirst("Location");
        GroupRequest groupRequest = new GroupRequest(1L, "savvasgroup");
        ResponseEntity<String> createGroupResponse = rest.postForEntity(URI.create(createGroupUrl), groupRequest, String.class);
        ResponseEntity<ShaftGroup> getGroupResponse = rest.getForEntity(URI.create(getGroupUrl + 1), ShaftGroup.class);
        //then
        String a = "test";
        int x = 4, y = 2;
        System.out.println(x+y+a);
        System.out.println(a+x+y);
        System.out.println(a+(x+y));
        assertEquals(201, createGroupResponse.getStatusCode().value());

    }
}