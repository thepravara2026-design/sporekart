package com.sporekart.ai.capabilitydiscovery.domain;

public record CapabilityFeature(String featureName, boolean supported, String minVersion, String maxVersion) {
}
