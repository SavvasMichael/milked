package org.savvas.milked.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FrontEndRestController {

    private static final Logger LOG = LoggerFactory.getLogger(FrontEndRestController.class);
    private static final String BASE_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();
    private final BalanceCalculator balanceCalculator;

    @Autowired
    public FrontEndRestController(BalanceCalculator balanceCalculator) {
        this.balanceCalculator = balanceCalculator;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity registerUser(@RequestBody Map body) {
        try {
            return restTemplate.postForEntity(URI.create(BASE_URL + "/registration"), body, Map.class);
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when trying to register user", e);
            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }

    @RequestMapping(value = "/group", method = RequestMethod.GET)
    public ResponseEntity getGroups(Principal principal) {
        Authentication authentication = (Authentication) principal;
        GrantedAuthority authority = authentication.getAuthorities().iterator().next();
        String userId = authority.getAuthority();
        try {
            return restTemplate.getForEntity(URI.create(BASE_URL + "/user/" + userId + "/group"), List.class);
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when trying to fetch groups", e);
            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }

    @RequestMapping(value = "/group", method = RequestMethod.POST)
    public ResponseEntity createGroup(@RequestBody Map<String, String> body, Principal principal) {
        Authentication authentication = (Authentication) principal;
        GrantedAuthority authority = authentication.getAuthorities().iterator().next();
        String userId = authority.getAuthority();
        body.put("userId", userId);
        try {
            return restTemplate.postForEntity(URI.create(BASE_URL + "/group"), body, Map.class);
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when trying to fetch groups", e);
            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }

    @RequestMapping(value = "/group/{groupId}/users", method = RequestMethod.GET)
    public ResponseEntity getGroupMembers(@PathVariable("groupId") Long groupId, Principal principal) {
        Authentication authentication = (Authentication) principal;
        GrantedAuthority authority = authentication.getAuthorities().iterator().next();
        String userId = authority.getAuthority();
        try {
            MilkedUser[] milkedUsers = restTemplate.getForEntity(URI.create(BASE_URL + "/group/" + groupId + "/users"), MilkedUser[].class).getBody();
            MilkingTransaction[] milkingTransactions = restTemplate.getForEntity(URI.create(BASE_URL + "/group/" + groupId + "/milk"), MilkingTransaction[].class).getBody();
            balanceCalculator.calculateBalances(milkedUsers, milkingTransactions);
            return ResponseEntity.ok().body(new GroupDetails(milkedUsers, milkingTransactions));
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when trying to fetch group members", e);
            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }

    @RequestMapping(value = "/group/{groupId}/leave", method = RequestMethod.POST)
    public ResponseEntity leaveGroup(@PathVariable("groupId") Long groupId, Principal principal) {
        Authentication authentication = (Authentication) principal;
        GrantedAuthority authority = authentication.getAuthorities().iterator().next();
        String userId = authority.getAuthority();
        try {
            return restTemplate.postForEntity(URI.create(BASE_URL + "/user/" + userId + "/group/" + groupId + "/leave"), null, Map.class);
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when deleting group", e);
            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }

    @RequestMapping(value = "/group/{groupId}/invite", method = RequestMethod.POST)
    public ResponseEntity inviteUser(@RequestBody Map<String, String> emailBody, @PathVariable("groupId") Long groupId) {

        try {
            return restTemplate.postForEntity(URI.create(BASE_URL + "/group/" + groupId + "/invite/"), emailBody, Map.class);
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when inviting user", e);
            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }
}