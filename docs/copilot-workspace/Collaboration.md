# Collaboration Engine

**Version:** 1.0.0  
**Last Updated:** 2026-07-23  

---

## Overview

The Collaboration Engine enables the Unified Copilot Workspace to engage multiple copilots for a single user query. This is triggered when the Routing Engine detects multiple distinct intents or the user explicitly requests collaboration.

---

## How Multi-Copilot Collaboration Works

```
User: "Create a marketing campaign for our top sales leads"
              │
              ▼
    Routing Engine → detects: campaign_create + lead_score
              │
              ▼
    Collaboration Engine
         ├── Dispatch to Marketing Copilot (campaign_create)
         └── Dispatch to Sales Copilot (lead_score)
              │
              ▼
    Wait for all responses (with timeout)
              │
              ▼
    Merge responses into unified CollaborationResponse
              │
              ▼
    Return to user
```

---

## Response Merging Strategy

The engine uses a **structured merge** strategy:

1. **Collect** — Gather all copilot responses with their intent tags.
2. **Order** — Arrange by a configured priority (or insertion order).
3. **Deduplicate** — Remove overlapping or redundant content.
4. **Section** — Each copilot's response is placed in a named section (e.g., `marketing`, `sales`).
5. **Summarize** — An optional summary section is prepended.

Merged output format:

```json
{
  "summary": "I've created a campaign and identified target leads.",
  "sections": {
    "marketing": { "campaignId": "cmp_123", "status": "draft" },
    "sales": { "leads": 45, "qualified": 22 }
  }
}
```

---

## Collaboration Lifecycle

```
                    ┌─────────┐
                    │  IDLE   │
                    └────┬────┘
                         │ multi-intent detected
                    ┌────▼────┐
                    │ PENDING │  ─── initial dispatch
                    └────┬────┘
                         │
              ┌──────────┼──────────┐
              ▼          ▼          ▼
        ┌────────┐ ┌────────┐ ┌────────┐
        │Running │ │Running │ │Running │  (one per copilot)
        │  C1    │ │  C2    │ │  C3    │
        └───┬────┘ └───┬────┘ └───┬────┘
            │          │          │
            ▼          ▼          ▼
        ┌────────┐ ┌────────┐ ┌────────┐
        │Complete│ │Complete│ │Complete│
        └───┬────┘ └───┬────┘ └───┬────┘
            │          │          │
            ▼──────────▼──────────▼
                  ┌─────────┐
                  │ MERGING │  ─── merge all results
                  └────┬────┘
                       │
                  ┌────▼────┐
                  │  DONE   │
                  └─────────┘
```

---

## Timeout Handling

| Parameter | Default | Description |
|-----------|---------|-------------|
| Per-copilot timeout | 15s | Max wait for a single copilot response |
| Overall timeout | 30s | Max total time for the entire collaboration |
| Partial results | Enabled | If a copilot times out, results from completed copilots are returned |

When a timeout occurs, the timed-out copilot is marked as `timeout` in the response and its section is omitted (or populated with an error message).

---

## Error Recovery

| Error | Behavior |
|-------|----------|
| Copilot unavailable (5xx) | Retry once after 1s; if still failing, mark as `failed` |
| Copilot returns 4xx | Mark as `failed`, include error detail in response |
| Network timeout | Treat as timeout (see above) |
| Merge failure | Return partial responses with a merge error flag |
| All copilots fail | Return 502 with `collaboration_status: all_failed` |
