package com.sporekart.events.model;

public abstract class DomainEvent extends AbstractEvent {
    protected DomainEvent(Builder<?> builder) {
        super(builder);
    }

    public static abstract class Builder<T extends Builder<T>> extends AbstractEvent.Builder<T> {
    }
}
