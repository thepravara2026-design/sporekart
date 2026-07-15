# Activity Timeline

## Purpose
An enterprise, accessible event feed of everything that has happened to a product — creation, edits, draft saves, reviews, approvals, publishing, archival, restores, duplication, deletion, views, and comments.

## Event Model
```ts
type EditActivityType =
  | 'created' | 'edited' | 'draft_saved' | 'review_requested'
  | 'approved' | 'rejected' | 'published' | 'scheduled'
  | 'archived' | 'restored' | 'duplicated' | 'deleted'
  | 'viewed' | 'comment';

interface EditActivityEvent {
  id: string;
  type: EditActivityType;
  message: string;
  actor: string;
  timestamp: string;
}
```
`mockActivity` (7 seed events) lives in `editing/mockEditData.ts`.

## Component — `ActivityTimeline.tsx`
- Semantic list: `<ol role="list">` → `<li role="listitem" aria-label="…">` with a connector line between nodes.
- Each node: type icon (token-driven), `StatusBadge` (variant by type), message, actor, and `<time dateTime>`.
- Memoized (`React.memo`) for performance.
- Empty state handled gracefully.

## Where it is used
- Editing workspace → `activity` section.
- Detail workspace → sidebar.
- Standalone → `/preview/products/timeline`.

## Accessibility
- Each item has a descriptive `aria-label` ("{Label} by {actor} on {timestamp}: {message}").
- Icons are `aria-hidden`; meaning is conveyed by text + badge.
- Color is never the only signal.

## Future Backend Integration
`useProductEditState` already appends events on every mock action. Swapping the in-memory `activity` array for an audit-log API (with pagination/virtualization for very long feeds) requires no component changes.
