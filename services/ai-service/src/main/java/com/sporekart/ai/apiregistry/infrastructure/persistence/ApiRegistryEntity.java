package com.sporekart.ai.apiregistry.infrastructure.persistence;

import com.sporekart.ai.apiregistry.domain.ApiHealthStatus;
import com.sporekart.ai.apiregistry.domain.HttpMethod;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ar_api_registry")
public class ApiRegistryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "api_id", nullable = false, unique = true, length = 255)
    private String apiId;

    @Column(name = "api_name", nullable = false, length = 255)
    private String apiName;

    @Column(name = "api_path", nullable = false, length = 512)
    private String apiPath;

    @Enumerated(EnumType.STRING)
    @Column(name = "http_method", nullable = false, length = 20)
    private HttpMethod httpMethod;

    @Column(length = 255)
    private String module;

    @Column(length = 255)
    private String owner;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String version;

    @Column(nullable = false)
    private boolean deprecated = false;

    @Column(name = "deprecation_notice", columnDefinition = "TEXT")
    private String deprecationNotice;

    @Column(name = "auth_required", nullable = false)
    private boolean authRequired = false;

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "ar_api_registry_roles",
            joinColumns = @JoinColumn(name = "api_registry_id"))
    @Column(name = "role", length = 255)
    private List<String> rolesAllowed = new ArrayList<>();

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "ar_api_registry_consumers",
            joinColumns = @JoinColumn(name = "api_registry_id"))
    @Column(name = "consumer", length = 255)
    private List<String> consumers = new ArrayList<>();

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "ar_api_registry_dependencies",
            joinColumns = @JoinColumn(name = "api_registry_id"))
    @Column(name = "dependency", length = 255)
    private List<String> dependencies = new ArrayList<>();

    @Column(name = "open_api_spec", columnDefinition = "TEXT")
    private String openApiSpec;

    @Enumerated(EnumType.STRING)
    @Column(name = "health_status", length = 20)
    private ApiHealthStatus healthStatus;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public ApiRegistryEntity() {
    }

    public ApiRegistryEntity(UUID id, String apiId, String apiName, String apiPath, HttpMethod httpMethod,
                              String module, String owner, String description, String version,
                              boolean deprecated, String deprecationNotice, boolean authRequired,
                              List<String> rolesAllowed, List<String> consumers, List<String> dependencies,
                              String openApiSpec, ApiHealthStatus healthStatus,
                              Instant createdAt, Instant updatedAt) {
        this.id = id;
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
