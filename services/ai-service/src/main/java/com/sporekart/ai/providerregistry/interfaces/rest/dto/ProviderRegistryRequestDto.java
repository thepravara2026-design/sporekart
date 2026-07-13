package com.sporekart.ai.providerregistry.interfaces.rest.dto;

import com.sporekart.ai.providerregistry.domain.ProviderCapability;
import com.sporekart.ai.providerregistry.domain.ProviderModelInfo;
import com.sporekart.ai.providerregistry.domain.ProviderType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public class ProviderRegistryRequestDto {

    @NotBlank
    private String providerName;

    @NotNull
    private ProviderType providerType;

    private String version;

    private int priority;

    private List<ProviderModelInfo> supportedModels = List.of();

    private List<ProviderCapability> capabilities = List.of();

    private Map<String, String> metadata = Map.of();

    public ProviderRegistryRequestDto() {}

    public ProviderRegistryRequestDto(String providerName, ProviderType providerType, String version,
                                      int priority, List<ProviderModelInfo> supportedModels,
                                      List<ProviderCapability> capabilities,
                                      Map<String, String> metadata) {
        this.providerName = providerName;
        this.providerType = providerType;
        this.version = version;
        this.priority = priority;
        this.supportedModels = supportedModels;
        this.capabilities = capabilities;
        this.metadata = metadata;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public ProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(ProviderType providerType) {
        this.providerType = providerType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<ProviderModelInfo> getSupportedModels() {
        return supportedModels;
    }

    public void setSupportedModels(List<ProviderModelInfo> supportedModels) {
        this.supportedModels = supportedModels;
    }

    public List<ProviderCapability> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<ProviderCapability> capabilities) {
        this.capabilities = capabilities;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
