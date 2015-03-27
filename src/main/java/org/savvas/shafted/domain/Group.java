package org.savvas.shafted.domain;

import org.savvas.shafted.controller.request.GroupRequest;

import javax.persistence.*;

@Entity
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public Group(String name, Long userId) {
        this.name = name;
        this.userId = userId;
    }

}
