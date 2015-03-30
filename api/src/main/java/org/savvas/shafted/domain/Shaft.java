package org.savvas.shafted.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Shaft {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long shafterId;
    private Long shafteeId;
    private Long groupId;
    private Integer amount;

    Shaft() {
    }

    public Shaft(Long shafterId, Long shafteeId, Long groupId, Integer amount) {
        this.shafterId = shafterId;
        this.shafteeId = shafteeId;
        this.groupId = groupId;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public Long getShafterId() {
        return shafterId;
    }

    public Long getShafteeId() {
        return shafteeId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public Integer getAmount() {
        return amount;
    }
}
