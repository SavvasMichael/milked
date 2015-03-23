package org.savvas.shafted.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String email;
    private String name;
    private String password;
    private boolean activated = false;
    private String uuid;

    User(){}

    public User(String email, String name, String password, String uuid) {
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

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, Email='%s', Name='%s', Password='%s']",
                id, email, name, password);
    }
}
