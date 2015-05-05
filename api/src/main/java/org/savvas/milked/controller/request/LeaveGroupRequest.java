package org.savvas.milked.controller.request;

import org.hibernate.validator.constraints.NotEmpty;

public class LeaveGroupRequest {
    @NotEmpty
    private Long userId;
    @NotEmpty
    private Long groupId;

    public LeaveGroupRequest() {
    }

    public LeaveGroupRequest(Long userId, Long groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getGroupId() {
        return groupId;
    }
}
