package org.savvas.milked.controller.request;


import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class EmailBodyRequest {
    @Email
    @NotEmpty
    private String email;

    EmailBodyRequest() {
    }

    public EmailBodyRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email.toLowerCase();
    }

}
