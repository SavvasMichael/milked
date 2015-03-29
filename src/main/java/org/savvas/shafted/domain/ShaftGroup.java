package org.savvas.shafted.domain;

import javax.persistence.*;

@Entity
public class ShaftGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Long userId;

    ShaftGroup() {
    }

    public ShaftGroup(String name, Long userId) {
        this.name = name;
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
