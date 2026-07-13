package com.sporekart.ai.apiregistry.interfaces.rest.dto;

import com.sporekart.ai.apiregistry.domain.ApiDependency;

import java.util.ArrayList;
import java.util.List;

public class ApiDiscoveryDto {

    private String apiId;
    private String apiName;
    private String apiPath;
    private String module;
    private String owner;
    private List<ApiDependency> dependencies = new ArrayList<>();

    public ApiDiscoveryDto() {
    }

    public ApiDiscoveryDto(String apiId, String apiName, String apiPath, String module,
                            String owner, List<ApiDependency> dependencies) {
        this.apiId = apiId;
        this.apiName = apiName;
        this.apiPath = apiPath;
        this.module = module;
        this.owner = owner;
        this.dependencies = dependencies != null ? dependencies : new ArrayList<>();
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<ApiDependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<ApiDependency> dependencies) {
        this.dependencies = dependencies != null ? dependencies : new ArrayList<>();
    }
}
