package com.sporekart.ai.domain.model;

import java.util.UUID;

public class CorporateAccount {
    private final UUID id;
    private final String name;
    private final String gstNumber;
    private final String panNumber;

    public CorporateAccount(UUID id, String name, String gstNumber, String panNumber) {
        this.id = id;
        this.name = name;
        this.gstNumber = gstNumber;
        this.panNumber = panNumber;
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

    public String getPanNumber() {
        return panNumber;
    }
}

