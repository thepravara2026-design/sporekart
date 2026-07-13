# Event Catalog

**Version:** 1.0.0
**Last Updated:** 2026-07-12
**Owner:** Enterprise AI Platform Engineering Team
**Module:** `event-catalog`

---

## Purpose

The Event Catalog is the centralized registry tracking every asynchronous event flowing through the SporeKart Enterprise AI Platform. It is the single source of truth for event contracts across all modules, replacing ad-hoc topic/event knowledge scattered in code and docs.

It complements the Kafka + Outbox pattern (ADR-004) by making event schemas, ownership, and operational policies discoverable and reviewable.

---

## Tracked Attributes

Each event entry in the catalog records:

- **Event name** — stable, namespaced identifier (e.g., `PromptPublished`, `PolicyViolationDetected`)
- **Module** — producing bounded context
- **Producer** — service/adapter that emits the event
- **Consumer(s)** — list of subscribed modules/services
- **Payload** — schema/contract reference (versioned), with field types and required flags
- **Version** — schema version of the payload (semantic)
- **Retention** — retention policy per topic/event (e.g., 7d, 30d, indefinite for audit)
- **Retry strategy** — `NONE`, `FIXED`, `EXPONENTIAL_BACKOFF` with max attempts, interval, multiplier, jitter
- **DLQ strategy** — dead-letter topic, max redeliveries, alert threshold
- **Documentation** — link to the owning module's architecture/API doc
- **Visualization** — graph metadata for the event-flow diagram (producer → consumer edges)

---

## Event Inventory (Examples)

The catalog covers all 9 governance Kafka topics and the AI platform topics, including but not limited to:

- `ai-gateway-events` (7 types), `ai-provider-events` (6), `ai-prompt-events` (7), `knowledge-events` (8), `semantic-events` (9), `conversation-events` (5), `content-events` (10), `assistant-events` (10)
- `policy-events` (8), `decision-events` (8), `approval-events` (10), `compliance-events`, `risk-events`, `analytics-events` (7), `admin-events` (7), `automation-events` (9), `governance-events` (12)

Totals: 9+ topics, 75+ event types, all registered in the catalog.

---

## Retention

- Per-event retention is declared in the catalog and reconciled against the Kafka topic configuration.
- Audit-relevant events (governance, compliance, risk, admin) default to longer/indefinite retention.
- Retention changes are versioned and emit a catalog event.

---

## Retry Strategy

- Each event declares its retry policy.
- `EXPONENTIAL_BACKOFF` uses configurable max attempts, interval, multiplier, and jitter (aligned with the Automation retry framework).
- Producers use the Outbox pattern so retries are at-least-once safe.

---

## DLQ Strategy

- Every topic declares a dead-letter topic (`<topic>-dlq`).
- The catalog records max redeliveries and the alert threshold that pages the on-call runbook.
- DLQ consumption is owned by the producing module's monitoring/runbook.

---

## Documentation

- Each event links to its owning module's architecture and API docs.
- The catalog is generated/validated against the actual Kafka publisher payloads via contract tests.
- Schema drift (payload change without version bump) is a CI failure.

---

## Visualization

- The catalog exposes a graph of `producer → event → consumer` edges.
- This powers the event-flow diagram in `docs/architecture/system-integration.md` and the runtime topology view.
- Edges are derived from the consumer list per event, keeping the diagram in sync with reality.

---

## Integration Points

- **All modules** — register emitted/consumed events on startup or via docs build.
- **API Registry** — events referenced by async API docs.
- **Provider/Prompt/Knowledge/Usage/Config Registries** — each emits catalog-registered events.
- **Governance Analytics** — consumes event-flow metrics.
- **Architecture tests** — validate that every published event exists in the catalog and vice versa.

---

## Testing

- `EventCatalogServiceTest` — registration, versioning, consumer resolution
- `EventContractTest` — payload schema vs catalog (drift detection)
- `EventCatalogControllerTest` — discovery endpoints + RFC 9457 errors
- Architecture tests — every Kafka publisher event is catalog-registered
