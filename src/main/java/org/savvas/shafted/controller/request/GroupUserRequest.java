package org.savvas.shafted.controller.request;

import javax.validation.constraints.NotNull;

public class GroupUserRequest {
    @NotNull
    private Long groupId;
    private Long userId;

    GroupUserRequest() {
    }

    public GroupUserRequest(Long groupId, Long userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public Long getUserId() {
        return userId;
    }
}
