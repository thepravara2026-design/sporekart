package com.sporekart.ai.domain.model;

import java.util.UUID;

public class PurchaseAgreement {
    private final UUID id;
    private final String reference;
    private final String status;

    public PurchaseAgreement(UUID id, String reference, String status) {
        this.id = id;
        this.reference = reference;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public String getStatus() {
        return status;
    }
}
