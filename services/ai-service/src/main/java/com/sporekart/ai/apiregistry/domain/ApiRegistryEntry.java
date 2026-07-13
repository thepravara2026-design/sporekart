package com.sporekart.ai.apiregistry.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ApiRegistryEntry {

    private String apiId;
    private String apiName;
    private String apiPath;
    private HttpMethod httpMethod;
    private String module;
    private String owner;
    private String description;
    private String version;
    private boolean deprecated;
    private String deprecationNotice;
    private boolean authRequired;
    private List<String> rolesAllowed = new ArrayList<>();
    private List<String> consumers = new ArrayList<>();
    private List<String> dependencies = new ArrayList<>();
    private String openApiSpec;
    private ApiHealthStatus healthStatus;
    private Instant createdAt;
    private Instant updatedAt;

    public ApiRegistryEntry() {
    }

    public ApiRegistryEntry(String apiId, String apiName, String apiPath, HttpMethod httpMethod,
                            String module, String owner, String description, String version,
                            boolean deprecated, String deprecationNotice, boolean authRequired,
                            List<String> rolesAllowed, List<String> consumers, List<String> dependencies,
                            String openApiSpec, ApiHealthStatus healthStatus,
                            Instant createdAt, Instant updatedAt) {
        this.apiId = apiId;
        this.apiName = apiName;
        this.apiPath = apiPath;
        this.httpMethod = httpMethod;
        this.module = module;
        this.owner = owner;
        this.description = description;
        this.version = version;
        this.deprecated = deprecated;
        this.deprecationNotice = deprecationNotice;
        this.authRequired = authRequired;
        this.rolesAllowed = rolesAllowed != null ? rolesAllowed : new ArrayList<>();
        this.consumers = consumers != null ? consumers : new ArrayList<>();
        this.dependencies = dependencies != null ? dependencies : new ArrayList<>();
        this.openApiSpec = openApiSpec;
        this.healthStatus = healthStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public String getDeprecationNotice() {
        return deprecationNotice;
    }

    public void setDeprecationNotice(String deprecationNotice) {
        this.deprecationNotice = deprecationNotice;
    }

    public boolean isAuthRequired() {
        return authRequired;
    }

    public void setAuthRequired(boolean authRequired) {
        this.authRequired = authRequired;
    }

    public List<String> getRolesAllowed() {
        return rolesAllowed;
    }

    public void setRolesAllowed(List<String> rolesAllowed) {
        this.rolesAllowed = rolesAllowed != null ? rolesAllowed : new ArrayList<>();
    }

    public List<String> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<String> consumers) {
        this.consumers = consumers != null ? consumers : new ArrayList<>();
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies != null ? dependencies : new ArrayList<>();
    }

    public String getOpenApiSpec() {
        return openApiSpec;
    }

    public void setOpenApiSpec(String openApiSpec) {
        this.openApiSpec = openApiSpec;
    }

    public ApiHealthStatus getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(ApiHealthStatus healthStatus) {
        this.healthStatus = healthStatus;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
