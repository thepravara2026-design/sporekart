# ADR-006: Governance Foundation Mandatory Pipeline

- **Status:** Accepted
- **Date:** 2026-07-12
- **Deciders:** Enterprise Architecture Review Board

## Problem

As an enterprise AI platform, SporeKart must enforce policy, approval, compliance, risk, and audit controls on every significant action such as prompt deployment, knowledge ingestion, model selection, and automated decisions. Without a mandatory, non-bypassable governance pipeline, business modules would implement ad-hoc checks, leading to inconsistent enforcement, audit gaps, and regulatory exposure.

## Decision

We establish a Governance Foundation composed of the governance, policy, decision, approval, compliance, risk, and analytics modules, wired as a mandatory pipeline that every governed operation must traverse. The pipeline evaluates policies, records decisions, requires approvals where configured, logs risk assessments, and emits immutable audit events to Kafka. The governance module is the orchestrator; business modules request governance evaluation through a well-defined API and cannot proceed without a verdict. The api-registry and capability-discovery advertise which operations are governed, and the compliance module validates adherence.

## Alternatives Considered

- **Per-module governance logic** — Pros: local control. Cons: inconsistent, bypassable, unauditable.
- **External policy engine only (OPA/Rego)** — Pros: declarative policy. Cons: lacks the decision/approval/risk workflow state machine the enterprise needs.

## Trade-offs

- Strong, uniform enforcement and auditability at the cost of added latency and a more complex request flow.
- Centralized governance at the cost of coupling business flows to the governance pipeline availability.

## Consequences

- Positive: consistent compliance, full audit trail, defensible decisions.
- Negative: governance pipeline is critical infrastructure; follow-up is high-availability and SLA hardening.

## Compliance

Enforced by ArchUnit rules requiring governed operations to call the governance API, audit-event presence checks in tests, and mandatory review of new governed operations in the api-registry.
