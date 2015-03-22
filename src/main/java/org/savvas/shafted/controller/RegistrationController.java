package org.savvas.shafted.controller;

import org.savvas.shafted.controller.error.ValidationException;
import org.savvas.shafted.controller.request.RegistrationRequest;
import org.savvas.shafted.domain.User;
import org.savvas.shafted.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.xml.ws.Response;
import java.net.URI;

@RestController
public class RegistrationController {
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity registerUser(@RequestBody @Valid RegistrationRequest registrationRequest, BindingResult validation) {
        if(validation.hasErrors()){
            throw new ValidationException(validation.getFieldErrors());
        }
        Long userId = userService.createUser(registrationRequest);
        URI userLocationUri = URI.create("/user/" + userId);
        //userService.sendEmail();
        return ResponseEntity.created(userLocationUri).build();
    }
}
