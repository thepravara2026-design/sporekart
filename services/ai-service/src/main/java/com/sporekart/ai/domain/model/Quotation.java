package com.sporekart.ai.domain.model;

import java.util.UUID;

public class Quotation {
    private final UUID id;
    private final String reference;
    private final UUID dealerId;
    private final String status;

    public Quotation(UUID id, String reference, UUID dealerId, String status) {
        this.id = id;
        this.reference = reference;
        this.dealerId = dealerId;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public UUID getDealerId() {
        return dealerId;
    }

    public String getStatus() {
        return status;
    }
}
