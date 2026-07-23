package com.sporekart.marketplace.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PluginEventBusTest {

    private PluginEventBus eventBus;

    @Mock
    private PluginEventListener listener1;

    @Mock
    private PluginEventListener listener2;

    @BeforeEach
    void setUp() {
        eventBus = new PluginEventBus();
    }

    @Test
    void subscribe_shouldAddListener() {
        eventBus.subscribe(listener1);
        assertEquals(1, eventBus.getListenerCount());
    }

    @Test
    void subscribe_shouldAllowMultipleListeners() {
        eventBus.subscribe(listener1);
        eventBus.subscribe(listener2);
        assertEquals(2, eventBus.getListenerCount());
    }

    @Test
    void subscribe_shouldAllowSameListenerMultipleTimes() {
        eventBus.subscribe(listener1);
        eventBus.subscribe(listener1);
        assertEquals(2, eventBus.getListenerCount());
    }

    @Test
    void unsubscribe_shouldRemoveListener() {
        eventBus.subscribe(listener1);
        eventBus.subscribe(listener2);
        eventBus.unsubscribe(listener1);

        assertEquals(1, eventBus.getListenerCount());
    }

    @Test
    void unsubscribe_shouldDoNothingWhenListenerNotSubscribed() {
        eventBus.subscribe(listener1);
        eventBus.unsubscribe(listener2);
        assertEquals(1, eventBus.getListenerCount());
    }

    @Test
    void unsubscribe_shouldRemoveOnlySpecifiedListener() {
        eventBus.subscribe(listener1);
        eventBus.subscribe(listener2);
        eventBus.unsubscribe(listener1);

        assertEquals(1, eventBus.getListenerCount());
    }

    @Test
    void publish_shouldNotifyAllSubscribedListeners() {
        eventBus.subscribe(listener1);
        eventBus.subscribe(listener2);

        var event = new PluginEvent(PluginEvent.EventType.PLUGIN_INSTALLED, "plugin1", Map.of());
        eventBus.publish(event);

        verify(listener1).onEvent(event);
        verify(listener2).onEvent(event);
    }

    @Test
    void publish_shouldNotNotifyUnsubscribedListeners() {
        eventBus.subscribe(listener1);
        eventBus.unsubscribe(listener1);

        var event = new PluginEvent(PluginEvent.EventType.PLUGIN_INSTALLED, "plugin1", Map.of());
        eventBus.publish(event);

        verify(listener1, never()).onEvent(any());
    }

    @Test
    void publish_shouldHandleNoListeners() {
        var event = new PluginEvent(PluginEvent.EventType.PLUGIN_INSTALLED, "plugin1", Map.of());
        assertDoesNotThrow(() -> eventBus.publish(event));
    }

    @Test
    void publish_shouldContinueWhenOneListenerFails() {
        doThrow(new RuntimeException("listener failed")).when(listener1).onEvent(any());
        eventBus.subscribe(listener1);
        eventBus.subscribe(listener2);

        var event = new PluginEvent(PluginEvent.EventType.PLUGIN_INSTALLED, "plugin1", Map.of());
        assertDoesNotThrow(() -> eventBus.publish(event));

        verify(listener1).onEvent(event);
        verify(listener2).onEvent(event);
    }

    @Test
    void publishEvent_shouldCreateAndPublishEvent() {
        eventBus.subscribe(listener1);

        eventBus.publishEvent(PluginEvent.EventType.PLUGIN_UPDATED, "plugin1", Map.of("version", "2.0.0"));

        verify(listener1).onEvent(argThat(e ->
            e.getType() == PluginEvent.EventType.PLUGIN_UPDATED
                && "plugin1".equals(e.getPluginId())
                && "2.0.0".equals(e.getData().get("version"))
        ));
    }

    @Test
    void getListenerCount_shouldReturnZeroInitially() {
        assertEquals(0, eventBus.getListenerCount());
    }

    @Test
    void noListenerLeaks_afterUnsubscribe() {
        eventBus.subscribe(listener1);
        eventBus.subscribe(listener2);
        assertEquals(2, eventBus.getListenerCount());

        eventBus.unsubscribe(listener1);
        assertEquals(1, eventBus.getListenerCount());

        var event = new PluginEvent(PluginEvent.EventType.PLUGIN_REMOVED, "plugin1", Map.of());
        eventBus.publish(event);

        verify(listener1, never()).onEvent(any());
        verify(listener2).onEvent(event);
    }

    @Test
    void listenerInvocationCount_shouldMatchEventCount() {
        var invocationCount = new AtomicInteger(0);
        PluginEventListener countingListener = e -> invocationCount.incrementAndGet();
        eventBus.subscribe(countingListener);

        eventBus.publishEvent(PluginEvent.EventType.PLUGIN_INSTALLED, "p1", Map.of());
        eventBus.publishEvent(PluginEvent.EventType.PLUGIN_UPDATED, "p1", Map.of());
        eventBus.publishEvent(PluginEvent.EventType.PLUGIN_REMOVED, "p1", Map.of());

        assertEquals(3, invocationCount.get());
    }

    @Test
    void multipleListeners_eachReceiveAllEvents() {
        var count1 = new AtomicInteger(0);
        var count2 = new AtomicInteger(0);
        eventBus.subscribe(e -> count1.incrementAndGet());
        eventBus.subscribe(e -> count2.incrementAndGet());

        eventBus.publish(new PluginEvent(PluginEvent.EventType.PLUGIN_LOADED, "p1", Map.of()));

        assertEquals(1, count1.get());
        assertEquals(1, count2.get());
    }

    @Test
    void publish_shouldPassEventWithAllFields() {
        eventBus.subscribe(listener1);

        var data = Map.<String, Object>of("reason", "timeout", "retries", 3);
        var event = new PluginEvent(PluginEvent.EventType.PLUGIN_FAILED, "failing-plugin", data);
        eventBus.publish(event);

        verify(listener1).onEvent(argThat(e -> {
            return e.getType() == PluginEvent.EventType.PLUGIN_FAILED
                && "failing-plugin".equals(e.getPluginId())
                && data.equals(e.getData())
                && e.getEventId() != null
                && e.getTimestamp() != null;
        }));
    }

    @Test
    void multiplePublishes_shouldIncrementListenerCallCount() {
        AtomicInteger callCount = new AtomicInteger(0);
        PluginEventListener listener = e -> callCount.incrementAndGet();
        eventBus.subscribe(listener);

        for (int i = 0; i < 5; i++) {
            eventBus.publishEvent(PluginEvent.EventType.CONFIGURATION_CHANGED, "p1", Map.of("i", i));
        }

        assertEquals(5, callCount.get());
    }
}
