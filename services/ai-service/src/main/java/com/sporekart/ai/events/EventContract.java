package com.sporekart.ai.events;

public record EventContract(
    String eventType,
    String schemaVersion,
    String description,
    String sourceModule,
    String destinationModule,
    boolean isMandatory,
    boolean isAsync
) {}
