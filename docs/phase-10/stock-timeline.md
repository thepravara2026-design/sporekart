# Stock Timeline & Audit Trail

## Purpose
Every state transition on a Stock Record is recorded in an immutable timeline for audit, compliance, and operational visibility.

## Event Structure
| Field | Type | Description |
|-------|------|-------------|
| `id` | string | Unique event identifier |
| `action` | string | Human-readable action name |
| `fromState` | StockState | Previous state (undefined if first event) |
| `toState` | StockState | New state |
| `quantity` | number | Quantity affected |
| `timestamp` | string | ISO-8601 timestamp |
| `user` | string | User who performed the action |
| `note` | string | Optional note/comment |

## Event Types
Mock service generates events for:
- Stock Record Created
- Quantity Updated
- State Changed
- Health Recalculated
- Reservation Added

## Timeline Features
- Events displayed chronologically (newest first)
- Filter by event type
- Search across action/note/user text
- User attribution column
- Quantity delta visualization (+/- with color)
- Date range filtering (UI ready, data-dependent)

## UI Component
`StockTimeline` renders a vertical timeline with:
- Left column: icon + timestamp
- Middle column: action + from→to state badges
- Right column: quantity delta + user
- Lazy loading at 50 events per batch
