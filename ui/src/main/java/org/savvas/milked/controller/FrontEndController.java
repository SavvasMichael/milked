package org.savvas.milked.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@RestController
public class FrontEndController {

    private static final String BASE_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity registerUser(@RequestBody Map body) {
        try {
            return restTemplate.postForEntity(URI.create(BASE_URL + "/registration"), body, Map.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }

//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public ResponseEntity login(@RequestBody Map body) {
//        try {
//            return restTemplate.postForEntity(URI.create(BASE_URL + "/login"), body, Map.class);
//        } catch (HttpClientErrorException e) {
//            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
//        }
//    }

    @RequestMapping(value = "/group", method = RequestMethod.POST)
    public ResponseEntity createGroup(@RequestBody Map body) {
        try {
            return restTemplate.postForEntity(URI.create(BASE_URL + "/group"), body, Map.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }
}