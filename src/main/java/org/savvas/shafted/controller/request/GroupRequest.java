package org.savvas.shafted.controller.request;

import org.hibernate.validator.constraints.NotEmpty;
import org.savvas.shafted.domain.UserRepository;

public class GroupRequest {
    UserRepository repository;
    @NotEmpty
    private String name;
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
