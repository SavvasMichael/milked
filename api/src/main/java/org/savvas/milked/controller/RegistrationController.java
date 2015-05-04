package org.savvas.milked.controller;

import org.savvas.milked.controller.error.ValidationException;
import org.savvas.milked.controller.request.RegistrationRequest;
import org.savvas.milked.domain.MilkedUser;
import org.savvas.milked.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URI;

@RestController
public class RegistrationController {
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationController.class);


    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity registerUser(@RequestBody @Valid RegistrationRequest registrationRequest, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new ValidationException(validation.getFieldErrors());
        }
        if (userService.userExists(registrationRequest.getEmail())) {
            throw new ValidationException("This email has already been registered");
        }
        MilkedUser user = userService.createUser(registrationRequest);
        URI userLocationUri = URI.create("/user/" + user.getId());
        userService.sendEmail();
        LOG.info("send email containing following url - {}", "http://milked.io/activation/" + user.getUuid());

        return ResponseEntity.created(userLocationUri).build();
    }
}
