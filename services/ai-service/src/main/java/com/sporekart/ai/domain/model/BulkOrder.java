package com.sporekart.ai.domain.model;

import java.util.UUID;

public class BulkOrder {
    private final UUID id;
    private final String reference;
    private final int minimumQuantity;

    public BulkOrder(UUID id, String reference, int minimumQuantity) {
        this.id = id;
        this.reference = reference;
        this.minimumQuantity = minimumQuantity;
    }

    public UUID getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public int getMinimumQuantity() {
        return minimumQuantity;
    }
}

