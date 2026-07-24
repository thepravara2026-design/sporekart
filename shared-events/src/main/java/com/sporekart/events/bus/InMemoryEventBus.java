package com.sporekart.events.bus;

import com.sporekart.events.model.Event;
import com.sporekart.events.model.EventPriority;
import com.sporekart.events.registry.EventRegistry;
import com.sporekart.events.routing.EventRouter;
import com.sporekart.events.retry.RetryEngine;
import com.sporekart.events.dlq.DeadLetterQueue;
import com.sporekart.events.dlq.DeadLetterRecord;
import com.sporekart.events.store.EventStore;
import com.sporekart.events.security.EventSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryEventBus implements EventBus {
    private static final Logger log = LoggerFactory.getLogger(InMemoryEventBus.class);

    private final List<Subscription> subscriptions = new CopyOnWriteArrayList<>();
    private final Map<EventPriority, BlockingQueue<Event>> queues = new ConcurrentHashMap<>();
    private final EventRegistry registry;
    private final EventRouter router;
    private final RetryEngine retryEngine;
    private final DeadLetterQueue dlq;
    private final EventStore eventStore;
    private final EventSecurity security;
    private final ExecutorService executor;
    private final AtomicInteger activeCount = new AtomicInteger(0);
    private volatile boolean running = true;

    public InMemoryEventBus(EventRegistry registry, EventRouter router,
                            RetryEngine retryEngine, DeadLetterQueue dlq,
                            EventStore eventStore, EventSecurity security) {
        this(registry, router, retryEngine, dlq, eventStore, security, 4);
    }

    public InMemoryEventBus(EventRegistry registry, EventRouter router,
                            RetryEngine retryEngine, DeadLetterQueue dlq,
                            EventStore eventStore, EventSecurity security,
                            int threadCount) {
        this.registry = registry;
        this.router = router;
        this.retryEngine = retryEngine;
        this.dlq = dlq;
        this.eventStore = eventStore;
        this.security = security;
        for (EventPriority p : EventPriority.values()) {
            queues.put(p, new PriorityBlockingQueue<>(11, (e1, e2) ->
                e2.getPriority().ordinal() - e1.getPriority().ordinal()));
        }
        this.executor = Executors.newFixedThreadPool(threadCount, r -> {
            Thread t = new Thread(r, "event-bus-worker");
            t.setDaemon(true);
            return t;
        });
        startDispatcher();
    }

    private void startDispatcher() {
        for (int i = 0; i < 4; i++) {
            executor.submit(this::dispatchLoop);
        }
    }

    private void dispatchLoop() {
        while (running) {
            try {
                Event event = pollQueue();
                if (event != null) {
                    activeCount.incrementAndGet();
                    try {
                        dispatchSync(event);
                    } finally {
                        activeCount.decrementAndGet();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Event dispatch error", e);
            }
        }
    }

    private Event pollQueue() throws InterruptedException {
        for (EventPriority p : EventPriority.values()) {
            BlockingQueue<Event> q = queues.get(p);
            Event event = q.poll();
            if (event != null) return event;
        }
        Thread.sleep(10);
        return null;
    }

    @Override
    public Subscription subscribe(String eventType, String subscriberName, EventHandler handler) {
        return subscribe(eventType, subscriberName, handler, null);
    }

    @Override
    public Subscription subscribe(String eventType, String subscriberName, EventHandler handler, EventFilter filter) {
        Subscription sub = new Subscription(eventType, subscriberName, handler, filter);
        subscriptions.add(sub);
        log.info("Subscribed {} to events of type {}", subscriberName, eventType);
        return sub;
    }

    @Override
    public void unsubscribe(Subscription subscription) {
        subscriptions.remove(subscription);
        log.info("Unsubscribed {} from events of type {}", subscription.getSubscriberName(), subscription.getEventType());
    }

    @Override
    public void publish(Event event) {
        if (!security.validatePublisher(event)) {
            log.warn("Publisher validation failed for event {}", event.getEventId());
            eventStore.storeFailed(event, "Publisher validation failed");
            return;
        }
        if (registry != null && !registry.isRegistered(event.getEventType())) {
            log.warn("Event type {} is not registered", event.getEventType());
        }
        eventStore.storePublished(event);
        queues.get(event.getPriority()).offer(event);
        log.debug("Published event {} of type {}", event.getEventId(), event.getEventType());
    }

    @Override
    public void publishAll(List<Event> events) {
        events.forEach(this::publish);
    }

    @Override
    public void dispatch(Event event) {
        queues.get(event.getPriority()).offer(event);
    }

    private void dispatchSync(Event event) {
        Instant start = Instant.now();
        List<Subscription> matched = router.route(event, subscriptions);

        if (matched.isEmpty()) {
            log.debug("No subscribers for event {} of type {}", event.getEventId(), event.getEventType());
            return;
        }

        for (Subscription sub : matched) {
            if (sub.getFilter() != null && !sub.getFilter().accept(event)) {
                continue;
            }
            try {
                sub.getHandler().handle(event);
                eventStore.storeConsumed(event, sub.getSubscriberName());
            } catch (Exception e) {
                log.error("Handler {} failed for event {}: {}", sub.getSubscriberName(), event.getEventId(), e.getMessage());
                handleHandlerFailure(event, sub, e);
            }
        }

        Duration elapsed = Duration.between(start, Instant.now());
        log.debug("Dispatched event {} to {} handlers in {}ms",
                event.getEventId(), matched.size(), elapsed.toMillis());
    }

    private void handleHandlerFailure(Event event, Subscription sub, Exception error) {
        if (retryEngine != null && retryEngine.shouldRetry(event.getEventId().toString())) {
            retryEngine.recordFailure(event.getEventId().toString(), error.getMessage());
            retryEngine.scheduleRetry(event, () -> queues.get(event.getPriority()).offer(event));
        } else if (dlq != null) {
            dlq.sendToDeadLetter(event, sub.getSubscriberName(), error.getMessage());
        }
        eventStore.storeFailed(event, sub.getSubscriberName() + ": " + error.getMessage());
    }

    @Override
    public boolean retry(String eventId) {
        Event event = eventStore.getEvent(eventId);
        if (event == null) return false;
        queues.get(event.getPriority()).offer(event);
        return true;
    }

    @Override
    public void deadLetter(String eventId, String reason) {
        Event event = eventStore.getEvent(eventId);
        if (event != null) {
            dlq.sendToDeadLetter(event, "manual", reason);
        }
    }

    @Override
    public List<Event> replay(String eventType, int limit) {
        return eventStore.getEventsByType(eventType).stream()
                .limit(limit)
                .peek(e -> queues.get(e.getPriority()).offer(e))
                .collect(Collectors.toList());
    }

    @Override
    public List<DeadLetterRecord> getDeadLetterQueue() {
        return dlq != null ? dlq.getAll() : List.of();
    }

    @Override
    public void clear() {
        queues.values().forEach(BlockingQueue::clear);
        subscriptions.clear();
        if (dlq != null) dlq.clear();
    }

    @Override
    public int getQueueDepth() {
        return queues.values().stream().mapToInt(BlockingQueue::size).sum();
    }

    @Override
    public int getQueueDepth(EventPriority priority) {
        return queues.get(priority).size();
    }

    public int getActiveCount() {
        return activeCount.get();
    }

    public void shutdown() {
        running = false;
        executor.shutdown();
    }
}
