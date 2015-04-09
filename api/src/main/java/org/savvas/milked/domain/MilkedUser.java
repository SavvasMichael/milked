package org.savvas.milked.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MilkedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String name;
    private String password;
    private boolean activated = false;
    private String uuid;
    private Long defaultGroupId;

    public MilkedUser() {
    }

    public MilkedUser(String email, String name, String password, String uuid) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getUuid() {
        return uuid;
    }

    public Long getDefaultGroupId() {
        return defaultGroupId;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
