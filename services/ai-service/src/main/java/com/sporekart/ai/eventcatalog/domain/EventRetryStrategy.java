package com.sporekart.ai.eventcatalog.domain;

public enum EventRetryStrategy {
    LINEAR,
    EXPONENTIAL,
    FIXED,
    NONE
}
