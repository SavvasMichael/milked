package org.savvas.milked.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class MilkingGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private MilkedUser owner;

    @ManyToMany
    private List<MilkedUser> milkedUsers;

    MilkingGroup() {
    }

    public MilkingGroup(String name, MilkedUser owner) {
        this.name = name;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MilkedUser getOwner() {
        return owner;
    }

    public List<MilkedUser> getMilkedUsers() {
        return milkedUsers;
    }
}
