package org.savvas.shafted.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ShaftUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String name;
    private String password;
    private boolean activated = false;
    private String uuid;

    ShaftUser() {
    }

    public ShaftUser(String email, String name, String password, String uuid) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.uuid = uuid;
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

    public Long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public boolean isActivated() {
        return activated;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, Email='%s', Name='%s', Password='%s']",
                id, email, name, password);
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
