# Product Lifecycle Management

## States (certified in `products/lifecycle.ts`)
`draft · under_review · approved · scheduled · published · active · inactive · archived · deleted`

Each state carries `label`, `variant` (badge), and `description`.

## Transition Map (`useProductEditState.lifecycleTransitions`)
```
draft        → under_review, deleted
under_review → approved, draft
approved     → published, scheduled, archived
scheduled    → published, draft
published    → active, inactive, archived, deleted
active       → inactive, archived, deleted
inactive     → active, archived, deleted
archived     → draft, deleted
deleted      → draft
```

## UI Components
- **`LifecyclePanel.tsx`** — current state chip + description + available transitions as buttons. Approve/Archive/Restore/Delete are permission-gated (`canApprove`, `canArchive`, `canRestore`, `canDelete`). Reject path and restore-from-archived path are visually distinguished.
- **`PublishingPanel.tsx`** — the action surface that drives transitions (see `publishing-workflow.md`).

## Mock Behaviour
All transitions update the in-memory `lifecycle` state and append an `EditActivityEvent` via `applyTransition(target, note?)`. No backend, no persistence.

## Restrictions
- Destructive actions (archive/restore/delete) always require explicit `Dialog` confirmation.
- Permission matrix (`products/permissions.ts`) controls who may approve/archive/restore/delete; the UI disables the control when not permitted.

## Routes
- In-workspace: `publishing` section (Lifecycle + Publishing panels side by side).
- Standalone: `/preview/products/lifecycle` lists all 9 states with descriptions and the current state highlighted.

## Future Backend Integration
`applyTransition` maps 1:1 to a future state-machine API (e.g. `POST /products/:id/transitions`). The transition map is already a pure function ready to be replaced by server-authoritative rules.
