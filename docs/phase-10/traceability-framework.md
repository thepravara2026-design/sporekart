# Traceability Framework

## Overview
The Traceability Framework provides end-to-end visibility into batch lifecycle events. Every state transition, quality action, and significant event is recorded in an immutable timeline.

## Event Structure
| Field | Type | Description |
|-------|------|-------------|
| `id` | string | Unique event identifier |
| `batchId` | string | FK to Batch |
| `batchCode` | string | Batch code (denormalized) |
| `event` | string | Event type name |
| `fromStatus` | BatchLifecycleState | Previous state |
| `toStatus` | BatchLifecycleState | New state |
| `user` | string | Actor email |
| `timestamp` | string | ISO-8601 |
| `note` | string | Optional comment |

## Event Types Generated
1. Batch Created
2. Submitted for Quality Review
3. Quality Approved
4. Batch Activated
5. Near Expiry Warning
6. Batch Expired
7. Batch Rejected
8. Batch Blocked
9. Batch Archived

## Traceability Features
- **Event filter**: Filter timeline by event type
- **Search**: Search across event name, batch code, user, and notes
- **User attribution**: Every event records the acting user
- **Status transitions**: Visual from→to status display
- **Timestamps**: Locale-formatted date/time display

## Visual Timeline
Each event is rendered as a vertical timeline node with:
- Dot indicator with connecting line
- Event name (bold)
- Timestamp (top-right)
- From/To status badges
- User attribution
- Optional note (italic)

## Future Extension Points
- Manufacturing events
- Transfer events
- Order assignment events
- Return events
- Recall events
- Disposal events
- External system integration events
