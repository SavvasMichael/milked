package org.savvas.milked.controller.request;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;

public class RegistrationRequest {

    @NotEmpty
    @Email
    @NotNull
    private String email;
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String email, String name, String password) {

        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getEmail() {
        if (email == null) {
            return "";
        }
        return email.toLowerCase();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
