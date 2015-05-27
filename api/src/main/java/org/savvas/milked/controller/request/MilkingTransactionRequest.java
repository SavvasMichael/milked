package org.savvas.milked.controller.request;

public class MilkingTransactionRequest {

    private Long milkerId;
    private Long milkeeId;
    private Long groupId;
    private Integer amount;
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
