package com.sporekart.ai.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class CommissionRule {
    private final UUID id;
    private final String category;
    private final BigDecimal rate;

    public CommissionRule(UUID id, String category, BigDecimal rate) {
        this.id = id;
        this.category = category;
        this.rate = rate;
    }

    public UUID getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getRate() {
        return rate;
    }
}
