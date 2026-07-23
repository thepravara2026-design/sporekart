package com.sporekart.marketing.copilot.domain;

import java.util.List;

public record MarketingPersona(
    String id,
    String name,
    String segment,
    String description,
    List<String> painPoints,
    List<String> goals,
    List<String> preferredChannels,
    String messagingStyle,
    String decisionFactors
) {}
