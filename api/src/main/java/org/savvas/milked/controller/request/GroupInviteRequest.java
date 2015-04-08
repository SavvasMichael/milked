package org.savvas.milked.controller.request;

import javax.validation.constraints.NotNull;

public class GroupInviteRequest {
    @NotNull
    private Long groupId;
    private Long userId;

    GroupInviteRequest() {
    }

    public GroupInviteRequest(Long groupId, Long userId) {
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
