package org.savvas.milked.controller.request;

public class UpdateUserRequest {
    private String name;
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
