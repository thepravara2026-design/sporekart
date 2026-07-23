package com.sporekart.copilot.event;

public interface EventBus {

    void publish(CopilotEvent event);

    void subscribe(String eventType, EventHandler handler);

    void unsubscribe(String eventType, EventHandler handler);
}
