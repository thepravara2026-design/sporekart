package com.sporekart.ai.configregistry.interfaces.rest.dto;

public class ConfigSnapshotRequestDto {

    private String name;
    private String description;
    private String createdBy;

    public ConfigSnapshotRequestDto() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
