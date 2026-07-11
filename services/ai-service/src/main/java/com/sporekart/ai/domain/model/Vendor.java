package com.sporekart.ai.domain.model;

import java.util.UUID;

public class Vendor {
    private final UUID id;
    private final String name;
    private final String gstNumber;
    private final String bankAccount;
    private final VendorStatus status;

    public Vendor(UUID id, String name, String gstNumber, String bankAccount, VendorStatus status) {
        this.id = id;
        this.name = name;
        this.gstNumber = gstNumber;
        this.bankAccount = bankAccount;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public VendorStatus getStatus() {
        return status;
    }
}
