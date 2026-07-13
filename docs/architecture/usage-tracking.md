# AI Usage & Cost Foundation

**Version:** 1.0.0
**Last Updated:** 2026-07-12
**Owner:** Enterprise AI Platform Engineering Team
**Module:** `usage-tracking`

---

## Purpose

The AI Usage & Cost Foundation provides a platform-wide, append-only record of AI consumption. It exists to answer operational and capacity questions — how much AI is being used, by whom, on which provider/model, and at what cost *indicator* level.

**Scope boundary:** This module tracks usage and cost *indicators only*. It contains **no billing, invoicing, charging, or quota-enforcement logic**. Billing is explicitly a future extension point (see below) and is not implemented in this sprint.

---

## Tracked Metrics

For every AI execution the foundation records:

- **Requests** — count of inbound AI requests (by module, tenant, user)
- **Responses** — count of completed responses (success + failure split)
- **Provider/model usage** — `providerId` + `modelId` per request (linked to Provider Registry)
- **Prompt/completion counts** — number of prompt and completion tokens
- **Token usage** — `inputTokens`, `outputTokens`, `totalTokens`
- **Execution time** — latency in ms per request and per stage
- **Failures** — error counts by type (timeout, provider error, validation, rate-limit, fallback exhausted)
- **Daily usage** — aggregated rollups by day
- **Monthly usage** — aggregated rollups by month

Cost is captured only as an **indicator** (`estimatedCost`) derived from informational per-token rates declared in the Provider Registry. It is explicitly *not* a financial figure and carries no currency-backed guarantee.

---

## Data Model (Indicative)

- `UsageRecord` — single execution: ids, tokens, latency, status, estimatedCost indicator, timestamp
- `DailyUsageRollup` — aggregated by day / module / provider / model
- `MonthlyUsageRollup` — aggregated by month / module / provider / model
- `UsageByActor` — by tenant / user / role for dashboards

All records are append-only and immutable (aligned with the Governance immutable audit pattern).

---

## Dashboard APIs

REST endpoints under `/api/v1/ai/usage/*`:

- `GET /api/v1/ai/usage/summary?from=&to=` — platform-wide totals
- `GET /api/v1/ai/usage/by-provider` — usage and cost indicator per provider/model
- `GET /api/v1/ai/usage/by-module` — usage per business module
- `GET /api/v1/ai/usage/by-tenant` — usage per tenant (RBAC-scoped)
- `GET /api/v1/ai/usage/daily?month=` — daily rollup
- `GET /api/v1/ai/usage/monthly` — monthly rollup
- `GET /api/v1/ai/usage/failures` — failure breakdown

Responses follow the standard envelope with correlation IDs and RFC 9457 errors. Dashboard data is also published to Governance Analytics for executive views.

---

## Integration Points

- **AI Gateway** — emits a usage event after every execution (success or failure).
- **Provider Registry** — supplies `providerId`/`modelId` and informational token rates.
- **Event Catalog** — emits `UsageRecorded` (one per execution) and `UsageRollupComputed` (periodic).
- **Governance Analytics** — consumes rollups for KPI calculation.
- **API Registry** — usage endpoints auto-registered.

---

## Future Billing Extension Point

The foundation is deliberately structured so billing can be added later **without** touching usage tracking:

- A `BillingExtension` port (interface only) is reserved; its implementation is out of scope.
- `estimatedCost` indicator fields already exist but are clearly marked non-financial.
- Rollups are tenant-scoped, enabling future metered/quota billing services to consume the same data.
- No charging, invoicing, payment, or quota-enforcement code is present in this sprint.

---

## Testing

- `UsageTrackingServiceTest` — record creation, token accounting, failure capture
- `UsageRollupServiceTest` — daily/monthly aggregation correctness
- `UsageControllerTest` — all dashboard endpoints + RFC 9457 errors + RBAC scoping
- Architecture tests — usage-tracking boundary isolation (no billing dependency)
