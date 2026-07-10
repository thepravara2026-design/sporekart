# ADR-006: Use contract-first development for APIs and events

- Status: Accepted
- Date: 2026-07-10
- Owners: Enterprise Architecture Review Board

## Context
The platform needs stable interfaces between services, frontend teams, QA, and external integrations.

## Decision
Use OpenAPI 3.1 and AsyncAPI as the canonical contract source for REST and event-driven development.

## Alternatives considered
- Implementation-first development
- Informal shared documentation only
- Service-local API docs without governance

## Trade-offs
Contract-first development adds upfront discipline but reduces drift and rework.

## Consequences
The platform gains stronger interoperability, governance, and implementation consistency.

## Approval
Reviewed and accepted by the ARB.
