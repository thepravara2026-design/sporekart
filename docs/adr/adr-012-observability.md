# ADR-012: Observability — Micrometer, Prometheus, Tracing

- **Status:** Accepted
- **Date:** 2026-07-12
- **Deciders:** Enterprise Architecture Review Board

## Problem

SporeKart spans more than twenty modules with asynchronous Kafka flows, AI Gateway calls, and Redis caching. Operating this system requires visibility into metrics, distributed traces, and logs to diagnose latency, failures, and cost spikes. Without standardized observability, incidents would be slow to resolve and AI spend would be uncontrolled.

## Decision

We standardize observability on Micrometer for metrics, Prometheus for scraping and alerting, and distributed tracing (OpenTelemetry / Micrometer Tracing with a compatible backend) propagated across HTTP, Kafka, and Redis boundaries. The monitoring module provides dashboards and alert rules, while usage-tracking captures AI token and cost metrics per tenant and module. All modules emit consistent metric names and trace spans, and critical flows are tagged with tenant and correlation IDs for end-to-end visibility.

## Alternatives Considered

- **Vendor APM only** — Pros: rich UI. Cons: cost, lock-in, less control over metrics pipeline.
- **Logs-only debugging** — Pros: simple. Cons: no quantitative SLOs, poor async visibility.

## Trade-offs

- Deep visibility and SLOs at the cost of instrumentation effort and metric storage.
- Standardized tracing at the cost of context-propagation discipline across async boundaries.

## Consequences

- Positive: faster incident response, cost accountability, SLO management.
- Negative: ongoing dashboard/alert maintenance; follow-up is SLO definition per module.

## Compliance

Enforced by monitoring module conventions, trace-context propagation tests, and Prometheus metric-name linting in CI.
