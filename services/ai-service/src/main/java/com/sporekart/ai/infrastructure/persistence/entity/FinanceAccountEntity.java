package com.sporekart.ai.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "finance_accounts")
public class FinanceAccountEntity {
    @Id
    private UUID id;

    @Column(name = "account_code", nullable = false, unique = true)
    private String accountCode;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Column(name = "account_type", nullable = false)
    private String accountType;

    @Column(name = "parent_account_id")
    private UUID parentAccountId;

    @Column(name = "currency")
    private String currency;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public FinanceAccountEntity() {
    }

    public FinanceAccountEntity(UUID id, String accountCode, String accountName, String accountType, String currency,
            BigDecimal balance) {
        this.id = id;
        this.accountCode = accountCode;
        this.accountName = accountName;
        this.accountType = accountType;
        this.currency = currency;
        this.balance = balance;
        this.isActive = true;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
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

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
