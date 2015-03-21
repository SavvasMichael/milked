package org.savvas.shafted.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String email;
    private String name;
    private String password;
    private boolean activated = false;
    private Long uniqueActivationCode;

    User(){}
    public User(String email, String name, String password){}

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, Email='%s', Name='%s', Password='%s']",
                id, email, name, password);
    }
}
