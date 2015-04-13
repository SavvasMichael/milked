package org.savvas.milked.domain;

import javax.persistence.*;

@Entity
public class MilkingTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "milker_id")
    private MilkedUser milker;

    @ManyToOne
    @JoinColumn(name = "milkee_id")
    private MilkedUser milkee;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private MilkingGroup group;
    private Integer amount;
    private String description;

    MilkingTransaction() {
    }

    public MilkingTransaction(MilkedUser milker, MilkedUser milkee, MilkingGroup group, Integer amount) {
        this.milker = milker;
        this.milkee = milkee;
        this.group = group;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public MilkedUser getMilker() {
        return milker;
    }

    public MilkedUser getMilkee() {
        return milkee;
    }

    public MilkingGroup getGroup() {
        return group;
    }

    public Integer getAmount() {
        return amount;
    }
}
