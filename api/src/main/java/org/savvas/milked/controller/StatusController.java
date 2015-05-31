package org.savvas.milked.controller;

import org.savvas.milked.controller.error.ValidationException;
import org.savvas.milked.controller.request.LoginRequest;
import org.savvas.milked.domain.MilkedUser;
import org.savvas.milked.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class StatusController {

    private final LoginService loginService;

    @Autowired
    public StatusController(LoginService loginService){
        this.loginService = loginService;
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET, produces = "text/plain")
    public String status(){
        return "ok";
    }
}
