# Context Broker

**Version:** 1.0.0  
**Last Updated:** 2026-07-23  

---

## Overview

The Context Broker maintains the current context for each workspace, including the active copilot, recent conversation history, user metadata, and any custom context variables. All copilots within a workspace share this context, enabling seamless transitions and informed responses.

---

## Context Namespaces

| Namespace | Description | Example |
|-----------|-------------|---------|
| `workspace` | Workspace-level settings and state | `active_copilot`, `default_copilot` |
| `session` | Current session data (volatile) | `session_start`, `message_count` |
| `user` | User profile and preferences | `user_id`, `role`, `language` |
| `custom` | Application-specific context variables | `current_order_id`, `active_ticket` |

---

## Context Synchronization

Context is synchronized in real-time across all components:

1. **On Chat** — After every chat response, the Context Broker updates workspace state.
2. **On Switch** — When switching copilots, the current context is snapshotted and passed to the new copilot.
3. **On Context Change** — Any `PUT /context` call immediately broadcasts the change to all active copilots.

The synchronization is push-based (via in-memory event bus or Redis pub/sub) for real-time updates, with a fallback to polling every 5 seconds.

---

## Context Lifecycle

```
                    ┌───────────┐
                    │  CREATED   │  Context namespace initialized
                    └─────┬─────┘
                          │
                    ┌─────▼─────┐
                    │  ACTIVE   │  Context is being used by copilots
                    └─────┬─────┘
                          │
              ┌───────────┼───────────┐
              ▼           ▼           ▼
        ┌─────────┐ ┌─────────┐ ┌─────────┐
        │ UPDATED │ │ SYNCED  │ │ EXPIRED │
        │ (value  │ │ (across │ │ (TTL    │
        │ changed)│ │copilots)│ │ reached)│
        └─────────┘ └─────────┘ └────┬────┘
                                      │
                                ┌─────▼─────┐
                                │  DELETED   │
                                └───────────┘
```

---

## Export / Import

Context can be exported as JSON and re-imported into another workspace:

**Export:**
```
GET /api/v1/workspace/{id}/context/export

Response:
{
  "exported_at": "2026-07-23T10:30:00Z",
  "workspace_id": "ws_123",
  "context": {
    "workspace": { ... },
    "session": { ... },
    "user": { ... },
    "custom": { ... }
  },
  "format_version": "1.0"
}
```

**Import:**
```
POST /api/v1/workspace/{id}/context/import
{
  "context": { ... },
  "mode": "merge" | "replace"
}
```

- `merge` — Deep-merges the imported context into the current context.
- `replace` — Replaces the current context entirely.
