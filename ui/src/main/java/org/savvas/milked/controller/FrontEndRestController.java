package org.savvas.milked.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
public class FrontEndRestController {

    private static final Logger LOG = LoggerFactory.getLogger(FrontEndRestController.class);
    private static final String BASE_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity registerUser(@RequestBody Map body) {
        try {
            return restTemplate.postForEntity(URI.create(BASE_URL + "/registration"), body, Map.class);
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when trying to register user", e);
            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }

    @RequestMapping(value = "/group")
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
}