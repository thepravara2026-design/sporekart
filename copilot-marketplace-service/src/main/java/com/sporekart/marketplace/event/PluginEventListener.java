package com.sporekart.marketplace.event;

@FunctionalInterface
public interface PluginEventListener {
    void onEvent(PluginEvent event);
}
