package org.savvas.milked.controller.request;

public class MilkingRequest {

    private Long milkerId;
    private Long milkeeId;
    private Long groupId;
    private Integer amount;

    MilkingRequest() {
    }

    public MilkingRequest(Long milkerId, Long milkeeId, Long groupId, Integer amount) {
        this.milkerId = milkerId;
        this.milkeeId = milkeeId;
        this.groupId = groupId;
        this.amount = amount;
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
}
