# ADR-001: Microservices vs Modular Monolith

- **Status:** Accepted
- **Date:** 2026-07-12
- **Deciders:** Enterprise Architecture Review Board

## Problem

SporeKart must scale as an enterprise AI platform serving many business domains including chat, content, workflow, governance, compliance, risk, analytics, and assistant capabilities. The architecture must support independent evolution of these domains, future horizontal scaling, and a clean service boundary model, while avoiding the operational overhead and distributed-systems complexity of a fine-grained microservice fleet during the early phases of the platform. A premature microservice split would introduce network latency, distributed transaction complexity, and deployment sprawl before the domain boundaries are proven.

## Decision

We adopt a modular monolith as the initial delivery model, structured explicitly along domain module boundaries using Spring Modulith, with the explicit intent and architectural provisions to extract modules into independently deployable services when justified by scaling or organizational needs. The platform ships as a single deployable Spring Boot 3.3.3 / Java 21 application composed of well-isolated modules (core, gateway, provider, prompt, rag, search, chat, content, workflow, monitoring, knowledge, semantic, conversation, assistant, governance, policy, decision, approval, compliance, risk, analytics, admin, automation, and the eight registries). Inter-module communication uses published domain events on Apache Kafka and an explicit module API surface, never direct cross-module database access. This keeps the monolith deployable and testable as one unit while preserving the option to carve out services.

## Alternatives Considered

- **Fine-grained Microservices from day one** — Pros: independent scaling and deployment, team autonomy. Cons: high operational cost, distributed transactions, network failures, premature decomposition before domains stabilize.
- **Single undisciplined monolith** — Pros: simplest to build. Cons: no boundaries, tight coupling, high regression risk, impossible to extract later.

## Trade-offs

- Reduced operational complexity and faster initial delivery at the cost of coarser independent scaling.
- Strong in-process performance and transactional consistency at the cost of eventual extraction effort later.
- Clear module boundaries add discipline overhead in day-to-day development.

## Consequences

- Positive: faster time to market, simpler testing, preserved extraction path, clear ownership per module.
- Negative: single deployment artifact means a fault in one module can affect the whole process; follow-up is a documented extraction playbook and the eight registries to enable service discovery.

## Compliance

Enforced through Spring Modulith module tests (verify no illegal cross-module references), ArchUnit rules rejecting direct database access across modules, and mandatory architecture review before any module is promoted to an independent service.
