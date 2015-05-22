package org.savvas.milked.controller;

import java.sql.Timestamp;
import java.util.Date;

public class MilkingTransaction {
    private Long id;

    private MilkedUser milker;
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
