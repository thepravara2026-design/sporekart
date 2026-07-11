package com.sporekart.ai.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class FinanceAccount {
    private UUID id;
    private String accountCode;
    private String accountName;
    private String accountType; // ASSET, LIABILITY, EQUITY, INCOME, EXPENSE
    private UUID parentAccountId;
    private String currency;
    private BigDecimal balance;
    private boolean isActive;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
    private boolean isDeleted;

    public FinanceAccount(UUID id, String accountCode, String accountName, String accountType,
            String currency, BigDecimal balance) {
        this.id = id;
        this.accountCode = accountCode;
        this.accountName = accountName;
        this.accountType = accountType;
        this.currency = currency;
        this.balance = balance;
        this.isActive = true;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
        this.isDeleted = false;
    }

    public UUID getId() {
        return id;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public UUID getParentAccountId() {
        return parentAccountId;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean isActive() {
        return isActive;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void updateBalance(BigDecimal amount) {
        this.balance = balance.add(amount);
        this.updatedAt = OffsetDateTime.now();
    }

    public void deactivate() {
        this.isActive = false;
        this.updatedAt = OffsetDateTime.now();
    }
}
