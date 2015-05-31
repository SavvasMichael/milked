package org.savvas.milked.domain;

import javax.persistence.*;
import java.util.Date;

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

    private Long milkingGroupId;
    private Integer amount;
    private String description;
    private Date createDate;

    MilkingTransaction() {
    }

    public MilkingTransaction(Long milkingGroupId, MilkedUser milker, MilkedUser milkee, Integer amount, String description) {
        this.milkingGroupId = milkingGroupId;
        this.milker = milker;
        this.milkee = milkee;
        this.amount = amount;
        this.description = description;
        this.createDate = new Date();
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

    public Long getMilkingGroupId() {
        return milkingGroupId;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreateDate() {
        return createDate;
    }
}
