# Dead Letter Queue

## Overview

The Dead Letter Queue (DLQ) stores events that failed processing after exhausting all retry attempts. It enables manual inspection, replay, and audit of failed events.

## DLQ Record Structure

| Field | Description |
|-------|-------------|
| eventId | Unique event identifier |
| eventType | Type of event |
| subscriberName | Handler that failed |
| failureReason | Exception message |
| failedAt | Timestamp of failure |
| lastReplayedAt | Timestamp of last replay |
| replayCount | Number of times replayed |

## Operations

| Operation | Description |
|-----------|-------------|
| `sendToDeadLetter(event, subscriber, reason)` | Send event to DLQ |
| `getRecord(eventId)` | Inspect DLQ record |
| `getAll()` | List all DLQ records |
| `getByEventType(type)` | Filter by event type |
| `getBySubscriber(subscriber)` | Filter by subscriber |
| `replay(eventId)` | Replay single event |
| `replayAll()` | Replay all DLQ events |
| `count()` | Total DLQ entries |

## Replay Process

1. Admin inspects DLQ via `getAll()` or `getByEventType()`
2. Root cause is identified (e.g., configuration error, temporary outage)
3. Fix is applied
4. Events are replayed via `replay()` or `replayAll()`
5. If handler succeeds, event is removed from active retry
6. If handler fails again, event remains in DLQ with incremented replayCount
