# Enterprise Event Architecture

## Overview

SporeKart's Enterprise Event Backbone provides a production-grade, internal event-driven architecture that enables loose coupling, asynchronous communication, and autonomous AI readiness across all business domains.

## Architecture Principles

1. **Backward Compatibility** — All existing REST APIs continue unchanged. Events are emitted alongside existing business logic.
2. **No External Broker Dependency** — The backbone uses an internal in-memory bus. Future-ready for Kafka, RabbitMQ, Google Pub/Sub, AWS EventBridge, Azure Event Grid via adapter pattern.
3. **Domain Autonomy** — Each business domain owns its events. No domain depends on another domain's handler implementation.
4. **Idempotency** — All event handlers are idempotent and safe to retry.
5. **Observability** — Every event is audited. Publish, consume, and failure metrics are available.

## Core Components

### Event Bus
Central message broker implementing publish/subscribe with priority queues, asynchronous dispatch, and thread pool isolation.

### Event Registry
Centralized catalog of all event types with discovery, validation, versioning, and deprecation.

### Event Router
Routes events to subscribers based on event type matching (exact, wildcard, conditional).

### Retry Engine
Automatic retry with exponential backoff, configurable max retries, and manual replay.

### Dead Letter Queue
Stores failed events with failure reason, subscriber, and replay capability.

### Event Store
Audit trail of all published, consumed, and failed events with type counts and aggregate-level queries.

### Event Security
Publisher validation, consumer authorization, and workspace isolation.

## Component Diagram

```
Producer → EventBus → Priority Queue → Dispatcher → Router → Subscribers
                ↓                                        ↓
           EventStore                              RetryEngine → DLQ
                ↓                                        ↓
           Audit Trail                             Manual Replay
```

## Event Flow

1. Service creates domain event via builder pattern
2. EventBus.publish() validates producer, registers in store, enqueues
3. Dispatcher picks from priority queue (CRITICAL > HIGH > NORMAL > LOW)
4. Router matches event type to subscribers (exact match, wildcard, conditional)
5. Each matched handler executes with error isolation
6. On failure: RetryEngine schedules retry with exponential backoff
7. After max retries: DeadLetterQueue stores event for manual inspection/replay
8. All operations are recorded in EventStore audit trail

## Performance Targets

| Operation | Target |
|-----------|--------|
| Publish | < 5ms |
| Dispatch | < 10ms |
| Handler | < 50ms |
| Retry | Automatic |
| Thread blocking | Zero (async) |
