package com.sporekart.ai.eventcatalog.infrastructure.persistence;

import com.sporekart.ai.eventcatalog.domain.EventCatalogEntry;
import com.sporekart.ai.eventcatalog.domain.EventRetryStrategy;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ec_event_catalog")
public class EventCatalogEntity {

    @Id
    @NotBlank
    private String eventId;

    @NotBlank
    @Column(nullable = false)
    private String eventName;

    @Column(nullable = false)
    private int eventVersion;

    @NotBlank
    @Column(nullable = false)
    private String module;

    @NotBlank
    @Column(nullable = false)
    private String producer;

    @ElementCollection
    @CollectionTable(name = "ec_event_consumers", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "consumer")
    private List<String> consumers = new ArrayList<>();

    @Lob
    private String payloadSchema;

    @Column(nullable = false)
    private int retentionDays;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventRetryStrategy retryStrategy;

    @Column(nullable = false)
    private int maxRetries;

    @Column(nullable = false)
    private boolean dlqEnabled;

    private String dlqTopic;

    @Lob
    private String description;

    @Lob
    private String documentation;

    private Instant createdAt;

    private Instant updatedAt;

    public EventCatalogEntity() {
    }

    public EventCatalogEntity(EventCatalogEntry entry) {
        this.eventId = entry.getEventId();
        this.eventName = entry.getEventName();
        this.eventVersion = entry.getEventVersion();
        this.module = entry.getModule();
        this.producer = entry.getProducer();
        this.consumers = entry.getConsumers() != null ? new ArrayList<>(entry.getConsumers()) : new ArrayList<>();
        this.payloadSchema = entry.getPayloadSchema();
        this.retentionDays = entry.getRetentionDays();
        this.retryStrategy = entry.getRetryStrategy();
        this.maxRetries = entry.getMaxRetries();
        this.dlqEnabled = entry.isDlqEnabled();
        this.dlqTopic = entry.getDlqTopic();
        this.description = entry.getDescription();
        this.documentation = entry.getDocumentation();
        this.createdAt = entry.getCreatedAt();
        this.updatedAt = entry.getUpdatedAt();
    }

    public EventCatalogEntry toDomain() {
        EventCatalogEntry entry = new EventCatalogEntry();
        entry.setEventId(this.eventId);
        entry.setEventName(this.eventName);
        entry.setEventVersion(this.eventVersion);
        entry.setModule(this.module);
        entry.setProducer(this.producer);
        entry.setConsumers(this.consumers != null ? new ArrayList<>(this.consumers) : new ArrayList<>());
        entry.setPayloadSchema(this.payloadSchema);
        entry.setRetentionDays(this.retentionDays);
        entry.setRetryStrategy(this.retryStrategy);
        entry.setMaxRetries(this.maxRetries);
        entry.setDlqEnabled(this.dlqEnabled);
        entry.setDlqTopic(this.dlqTopic);
        entry.setDescription(this.description);
        entry.setDocumentation(this.documentation);
        entry.setCreatedAt(this.createdAt);
        entry.setUpdatedAt(this.updatedAt);
        return entry;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getEventVersion() {
        return eventVersion;
    }

    public void setEventVersion(int eventVersion) {
        this.eventVersion = eventVersion;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public List<String> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<String> consumers) {
        this.consumers = consumers;
    }

    public String getPayloadSchema() {
        return payloadSchema;
    }

    public void setPayloadSchema(String payloadSchema) {
        this.payloadSchema = payloadSchema;
    }

    public int getRetentionDays() {
        return retentionDays;
    }

    public void setRetentionDays(int retentionDays) {
        this.retentionDays = retentionDays;
    }

    public EventRetryStrategy getRetryStrategy() {
        return retryStrategy;
    }

    public void setRetryStrategy(EventRetryStrategy retryStrategy) {
        this.retryStrategy = retryStrategy;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public boolean isDlqEnabled() {
        return dlqEnabled;
    }

    public void setDlqEnabled(boolean dlqEnabled) {
        this.dlqEnabled = dlqEnabled;
    }

    public String getDlqTopic() {
        return dlqTopic;
    }

    public void setDlqTopic(String dlqTopic) {
        this.dlqTopic = dlqTopic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
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
