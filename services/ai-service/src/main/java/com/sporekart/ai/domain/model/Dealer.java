package com.sporekart.ai.domain.model;

import java.util.UUID;

public class Dealer {
    private final UUID id;
    private final String name;
    private final String gstNumber;

    public Dealer(UUID id, String name, String gstNumber) {
        this.id = id;
        this.name = name;
        this.gstNumber = gstNumber;
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
}

