package com.sporekart.ai.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class CreditAccount {
    private final UUID id;
    private final String customerName;
    private final BigDecimal creditLimit;

    public CreditAccount(UUID id, String customerName, BigDecimal creditLimit) {
        this.id = id;
        this.customerName = customerName;
        this.creditLimit = creditLimit;
    }

    public UUID getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }
}

