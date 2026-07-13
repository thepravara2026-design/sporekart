package com.sporekart.ai.eventcatalog.infrastructure.persistence;

import com.sporekart.ai.eventcatalog.domain.EventSubscription;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "ec_event_subscription")
public class EventSubscriptionEntity {

    @Id
    @NotBlank
    private String subscriptionId;

    @NotBlank
    @Column(nullable = false)
    private String eventId;

    @NotBlank
    @Column(nullable = false)
    private String consumerName;

    @NotBlank
    @Column(nullable = false)
    private String consumerGroup;

    @NotNull
    @Column(nullable = false)
    private boolean enabled;

    public EventSubscriptionEntity() {
    }

    public EventSubscriptionEntity(EventSubscription subscription) {
        this.subscriptionId = subscription.getSubscriptionId();
        this.eventId = subscription.getEventId();
        this.consumerName = subscription.getConsumerName();
        this.consumerGroup = subscription.getConsumerGroup();
        this.enabled = subscription.isEnabled();
    }

    public EventSubscription toDomain() {
        EventSubscription subscription = new EventSubscription();
        subscription.setSubscriptionId(this.subscriptionId);
        subscription.setEventId(this.eventId);
        subscription.setConsumerName(this.consumerName);
        subscription.setConsumerGroup(this.consumerGroup);
        subscription.setEnabled(this.enabled);
        return subscription;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
