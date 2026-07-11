package com.sporekart.ai.domain.model;

import java.util.UUID;

public class Distributor {
    private final UUID id;
    private final String name;
    private final String region;

    public Distributor(UUID id, String name, String region) {
        this.id = id;
        this.name = name;
        this.region = region;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }
}

