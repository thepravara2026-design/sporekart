# Consumer Guide

## Subscribing to Events

Inject the `EventBus` and subscribe to event types:

```java
@Component
public class OrderEventListener {
    private final EventBus eventBus;
    private Subscription subscription;

    public OrderEventListener(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @PostConstruct
    public void init() {
        subscription = eventBus.subscribe("order.created",
                "inventory-reservation-handler",
                this::handleOrderCreated);
    }

    @PreDestroy
    public void cleanup() {
        if (subscription != null) {
            eventBus.unsubscribe(subscription);
        }
    }

    private void handleOrderCreated(Event event) {
        OrderCreated orderCreated = (OrderCreated) event;
        // Handle the event
    }
}
```

## Using Filters

```java
eventBus.subscribe("inventory.low",
    "restock-handler",
    this::handleLowStock,
    event -> {
        InventoryLow low = (InventoryLow) event;
        return low.getCurrentStock() < 5; // Only urgent low stock
    });
```

## Best Practices

1. **Be idempotent** — Handlers must be safe to execute multiple times for the same event
2. **Handle failures** — Exceptions trigger automatic retry and eventual DLQ
3. **Keep handlers fast** — Target < 50ms execution time
4. **Don't depend on other handlers** — Each handler must be independent
5. **Clean up subscriptions** — Unsubscribe in @PreDestroy to prevent resource leaks
6. **Use strong typing** — Cast to the specific domain event class in the handler

## Failure Handling

1. If handler throws exception → RetryEngine schedules exponential backoff retry
2. After max retries → Event sent to Dead Letter Queue
3. Admin can inspect DLQ and manually replay events
4. Each failure is recorded in the EventStore audit trail
