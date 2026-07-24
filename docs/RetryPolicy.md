# Retry Policy

## Configuration

The default retry policy is configured in `RetryPolicy` class:

| Parameter | Default | Description |
|-----------|---------|-------------|
| maxRetries | 3 | Maximum retry attempts |
| initialDelay | 100ms | Delay before first retry |
| maxDelay | 10s | Maximum delay between retries |
| backoffMultiplier | 2.0 | Exponential backoff factor |

## Retry Flow

```
Handler Failure → Record Failure → Check maxRetries
                                        ↓
                                Within limit? → Compute Delay → Schedule Retry
                                        ↓
                                Exceeded limit? → Send to DLQ
```

## Built-in Policies

| Policy | maxRetries | initialDelay | maxDelay | multiplier | Use Case |
|--------|------------|--------------|----------|------------|----------|
| `defaultPolicy()` | 3 | 100ms | 10s | 2.0 | General purpose |
| `aggressive()` | 5 | 50ms | 5s | 2.0 | High-throughput, transient failures |
| `conservative()` | 10 | 1s | 1m | 1.5 | External dependency failures |

## Manual Replay

Events in DLQ can be replayed individually or in batch:

```java
// Replay single event
boolean replayed = eventBus.retry(eventId);

// Replay all DLQ events
int count = deadLetterQueue.replayAll();
```

## Observability

Retry metrics are available via EventStore:

```java
int retryCount = retryEngine.getRetryCount(eventId);
List<RetryRecord> allRecords = retryEngine.getAllRecords();
long totalRetries = retryEngine.getTotalRetries();
```
