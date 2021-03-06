package org.savvas.milked.controller.request;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class MilkingTransactionRequest {

    private Long milkerId;
    private Long milkeeId;
    private Long groupId;
    @NotEmpty
    @NotNull
    private Integer amount;
    @NotEmpty
    @NotNull
    private String description;

    MilkingTransactionRequest() {
    }

    public MilkingTransactionRequest(Long groupId, Long milkerId, Long milkeeId, Integer amount, String description) {
        this.milkerId = milkerId;
        this.milkeeId = milkeeId;
        this.groupId = groupId;
        this.amount = amount;
        this.description = description;
    }

    public Long getMilkerId() {
        return milkerId;
    }

    public Long getMilkeeId() {
        return milkeeId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
