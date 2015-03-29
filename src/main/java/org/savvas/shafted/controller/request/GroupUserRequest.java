package org.savvas.shafted.controller.request;

public class GroupUserRequest {

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
