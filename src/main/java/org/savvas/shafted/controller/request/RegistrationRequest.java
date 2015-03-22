package org.savvas.shafted.controller.request;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Index;
import javax.persistence.UniqueConstraint;
import javax.validation.Constraint;

public class RegistrationRequest {

    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;

    public RegistrationRequest() { }
    public RegistrationRequest(String email, String name, String password) {

        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
