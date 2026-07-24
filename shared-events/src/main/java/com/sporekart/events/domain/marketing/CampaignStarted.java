package com.sporekart.events.domain.marketing;

import com.sporekart.events.model.DomainEvent;

public class CampaignStarted extends DomainEvent {
    private final String campaignId;
    private final String campaignName;

    private CampaignStarted(Builder builder) {
        super(builder);
        this.campaignId = builder.campaignId;
        this.campaignName = builder.campaignName;
    }

    public String getCampaignId() { return campaignId; }
    public String getCampaignName() { return campaignName; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String campaignId;
        private String campaignName;

        public Builder campaignId(String campaignId) { this.campaignId = campaignId; return this; }
        public Builder campaignName(String campaignName) { this.campaignName = campaignName; return this; }

        public CampaignStarted build() {
            return new CampaignStarted(this);
        }
    }
}
