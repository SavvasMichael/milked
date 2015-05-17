package org.savvas.milked.controller.request;

import org.hibernate.validator.constraints.NotEmpty;

public class UpdateUserRequest {
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;

    UpdateUserRequest() {
    }

    public UpdateUserRequest(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
