package com.sporekart.ai.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Settlement {
    private final UUID id;
    private final String status;
    private final BigDecimal amount;

    public Settlement(UUID id, String status, BigDecimal amount) {
        this.id = id;
        this.status = status;
        this.amount = amount;
    }

    public UUID getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
