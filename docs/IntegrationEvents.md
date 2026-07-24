# Integration Events

## Purpose

Integration events enable cross-service communication. Unlike domain events (which stay within a bounded context), integration events carry payloads across service boundaries and include source/target service metadata.

## Model

Integration events extend `IntegrationEvent` which extends `AbstractEvent` with:

| Field | Description |
|-------|-------------|
| `sourceService` | Name of the publishing service |
| `targetService` | Name of the intended consuming service (optional) |

## Usage

```java
import com.sporekart.events.model.IntegrationEvent;

IntegrationEvent event = IntegrationEvent.builder()
    .eventType("order.payment-completed")
    .aggregateId(orderId)
    .aggregateType("order")
    .sourceService("order-service")
    .targetService("notification-service")
    .correlationId(CorrelationIds.newId().getCorrelationId())
    .producer("order-service")
    .build();

eventBus.publish(event);
```

## Cross-Service Flow

1. **Order Service** publishes `order.created` IntegrationEvent
2. **Inventory Service** subscriber reserves stock
3. **Notification Service** subscriber sends confirmation
4. **Analytics Service** subscriber records the event

All services communicate through the Event Backbone without direct coupling.
