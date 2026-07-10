package com.sporekart.training.domain.model;

import java.util.UUID;

public class GrowerProfile {
    private final String id;
    private final String fullName;
    private final String village;
    private final String district;
    private final String state;
    private final String experience;

    public GrowerProfile(String id, String fullName, String village, String district, String state, String experience) {
        this.id = id;
        this.fullName = fullName;
        this.village = village;
        this.district = district;
        this.state = state;
        this.experience = experience;
    }

    public static GrowerProfile register(String fullName, String village, String district, String state,
            String experience) {
        return new GrowerProfile(UUID.randomUUID().toString(), fullName, village, district, state, experience);
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getVillage() {
        return village;
    }

    public String getDistrict() {
        return district;
    }

    public String getState() {
        return state;
    }

    public String getExperience() {
        return experience;
    }
}
