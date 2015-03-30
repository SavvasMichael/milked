package org.savvas.shafted.controller.request;

import java.math.BigDecimal;

public class ShaftRequest {

    private Long shafterId;
    private Long shafteeId;
    private Long groupId;
    private BigDecimal amount;

    ShaftRequest() {
    }

    public ShaftRequest(Long shafterId, Long shafteeId, Long groupId, BigDecimal amount) {

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

    public BigDecimal getAmount() {
        return amount;
    }
}
