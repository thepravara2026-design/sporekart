package com.sporekart.copilot.event;

@FunctionalInterface
public interface EventHandler {

    void handle(CopilotEvent event);
}
