# Publisher Guide

## Creating Events

Use the domain event classes in `com.sporekart.events.domain.{domain}` package:

```java
OrderCreated event = OrderCreated.builder()
    .eventType("order.created")
    .aggregateId(orderId)
    .aggregateType("order")
    .workspaceId(workspaceId)
    .correlationId(correlationId)
    .causationId(causationId)
    .producer("order-service")
    .orderId(orderId)
    .customerId(customerId)
    .totalAmount(total)
    .priority(EventPriority.NORMAL)
    .build();
```

## Publishing Events

Inject the `EventBus` and call `publish()`:

```java
@Service
public class OrderService {
    private final EventBus eventBus;

    public OrderService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public Order createOrder(CreateOrderRequest request) {
        // ... business logic ...

        OrderCreated event = OrderCreated.builder()
            .aggregateId(order.getId())
            .aggregateType("order")
            .producer("order-service")
            .orderId(order.getId())
            .customerId(order.getCustomerId())
            .totalAmount(order.getTotal())
            .build();

        eventBus.publish(event);
        return order;
    }
}
```

## Best Practices

1. **Publish after successful persistence** — Only publish events after domain state is committed
2. **Include correlation IDs** — Always propagate correlationId and causationId for traceability
3. **Set appropriate priority** — Use CRITICAL for payments, NORMAL for most events, LOW for analytics
4. **Register event types** — Ensure all event types are registered in the Registry
5. **Use domain-specific classes** — Use typed domain event classes instead of generic Event
6. **One event per action** — Publish one event per significant domain action
