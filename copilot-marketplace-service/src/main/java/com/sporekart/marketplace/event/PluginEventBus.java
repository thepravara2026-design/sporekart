package com.sporekart.marketplace.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class PluginEventBus {

    private static final Logger log = LoggerFactory.getLogger(PluginEventBus.class);

    private final List<PluginEventListener> listeners = new CopyOnWriteArrayList<>();

    public void subscribe(PluginEventListener listener) {
        listeners.add(listener);
        log.info("Event listener subscribed: {}", listener.getClass().getSimpleName());
    }

    public void unsubscribe(PluginEventListener listener) {
        listeners.remove(listener);
    }

    public void publish(PluginEvent event) {
        log.info("Publishing event: {} for plugin {}", event.getType(), event.getPluginId());
        listeners.forEach(listener -> {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                log.error("Listener failed for event {}: {}", event.getType(), e.getMessage());
            }
        });
    }

    public void publishEvent(PluginEvent.EventType type, String pluginId, Map<String, Object> data) {
        publish(new PluginEvent(type, pluginId, data));
    }

    public int getListenerCount() { return listeners.size(); }
}
