# Inventory Lifecycle

## Overview
The Inventory Lifecycle defines the stages an Inventory Item passes through from creation to archival. Each stage transition is recorded as an auditable Lifecycle Event.

## Lifecycle Stages
| Stage | Label | Description |
|-------|-------|-------------|
| `draft` | Draft | Item record created but not yet activated |
| `pending_approval` | Pending Approval | Awaiting review and approval |
| `approved` | Approved | Approved for operations |
| `active` | Active | Item is live in inventory operations |
| `frozen` | Frozen | Temporarily frozen — no transactions allowed |
| `suspended` | Suspended | Suspended due to quality or compliance issues |
| `discontinued` | Discontinued | No longer produced or procured |
| `archived` | Archived | Historical record — no further transactions |

## Lifecycle Event
```typescript
interface LifecycleEvent {
  id: string;
  stage: LifecycleStage;
  timestamp: string;    // ISO 8601
  user: string;         // Who performed the transition
  note?: string;        // Reason or description
}
```

## Transition Rules (future)
- draft → pending_approval: submit for review
- pending_approval → approved: reviewer approves
- pending_approval → draft: reviewer rejects
- approved → active: system activates
- active → frozen: quality hold or investigation
- active → suspended: quality or compliance failure
- active → discontinued: end of life
- frozen → active: hold released
- suspended → active: issue resolved
- discontinued → archived: retention period expired
- any → draft: revert (admin only)

## UI
- `LifecyclePage`: shows distribution of items across stages + sample timeline
- `LifecycleTimeline`: vertical timeline component showing events in reverse chronological order
- Registry table includes lifecycle column with colored badges
- Profile page shows full lifecycle timeline for a single item

## Timeline Component
The `LifecycleTimeline` component renders events with:
- Colored dot (matching stage variant)
- Connecting line between events
- Stage badge, timestamp, note, and user
- Sorted newest-first
