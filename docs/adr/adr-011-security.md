# ADR-011: Security — RBAC, JWT, Audit

- **Status:** Accepted
- **Date:** 2026-07-12
- **Deciders:** Enterprise Architecture Review Board

## Problem

SporeKart is a multi-tenant enterprise platform handling sensitive knowledge, prompts, decisions, and user data. It requires authentication, fine-grained authorization across modules and tenants, and an immutable audit trail for compliance. A weak or inconsistent security model would expose data, allow privilege escalation, and fail regulatory requirements.

## Decision

We implement security centrally using Spring Security with JWT-based authentication and a role-based access control (RBAC) model enriched by tenant context. Authentication issues signed JWTs containing subject, roles, tenant, and scoped permissions; each module authorizes requests using method-level and endpoint-level rules. All security-relevant actions emit audit events to Kafka for immutable, append-only storage and compliance review. Secrets and signing keys are managed externally and never committed. The governance and compliance modules consume audit streams for policy verification.

## Alternatives Considered

- **Session-based auth** — Pros: simple revocation. Cons: poor fit for stateless APIs and future mobile clients.
- **Opaque tokens + introspection** — Pros: revocable. Cons: extra latency per request, external dependency on introspection service.

## Trade-offs

- Stateless, scalable auth at the cost of harder immediate revocation (mitigated by short TTLs and rotation).
- Strong audit at the cost of event volume and storage.

## Consequences

- Positive: secure, tenant-isolated, auditable platform ready for enterprise compliance.
- Negative: JWT revocation nuance; follow-up is a token-revocation/clearance strategy.

## Compliance

Enforced by Spring Security tests per module, RBAC rule reviews, audit-event presence assertions, and security scanning in CI.
