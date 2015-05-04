package org.savvas.milked.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Controller
public class FrontEndController {

    private static final Logger LOG = LoggerFactory.getLogger(FrontEndController.class);
    private static final String BASE_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();


    @RequestMapping(value = "/activation/{uuid}", method = RequestMethod.GET)
    public String activateUser(@PathVariable("uuid") String uuid) {
        try {
            restTemplate.getForEntity(URI.create(BASE_URL + "/activation/" + uuid), String.class);
            return "login";
        } catch (HttpClientErrorException e) {
            LOG.warn("Error when trying to activate user", e);
            return "errorLanding";
        }
    }
}
