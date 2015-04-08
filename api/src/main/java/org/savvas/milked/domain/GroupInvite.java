package org.savvas.milked.domain;

import org.savvas.milked.controller.request.GroupUserState;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GroupInvite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long groupId;
    private Long userId;
    private GroupUserState state;

    GroupInvite() {
    }

    public GroupInvite(Long groupId, Long userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public GroupUserState getState() {
        return state;
    }

    public void setState(GroupUserState state) {
        this.state = state;
    }
}
