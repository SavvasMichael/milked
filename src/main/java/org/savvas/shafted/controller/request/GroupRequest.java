package org.savvas.shafted.controller.request;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class GroupRequest {
    @NotEmpty
    private String name;
    @NotNull
    private Long userId;

    public GroupRequest() {
    }

    public GroupRequest(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getUserId() {
        return userId;
    }
}
