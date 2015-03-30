package org.savvas.shafted.controller.request;

public class ShaftRequest {

    private Long shafterId;
    private Long shafteeId;
    private Long groupId;
    private Integer amount;

    ShaftRequest() {
    }

    public ShaftRequest(Long shafterId, Long shafteeId, Long groupId, Integer amount) {

        this.shafterId = shafterId;
        this.shafteeId = shafteeId;
        this.groupId = groupId;
        this.amount = amount;
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
