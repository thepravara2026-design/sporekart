package com.sporekart.ai.eventcatalog.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;

public class EventSubscriptionRequestDto {

    @NotBlank
    private String eventId;

    @NotBlank
    private String consumerName;

    @NotBlank
    private String consumerGroup;

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
}
