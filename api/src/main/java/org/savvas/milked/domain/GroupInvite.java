package org.savvas.milked.domain;


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
    private String uuid;

    GroupInvite() {
    }

    public GroupInvite(Long groupId, Long userId, String uuid) {
        this.groupId = groupId;
        this.userId = userId;
        this.uuid = uuid;
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

    public String getUuid() {
        return uuid;
    }

}
