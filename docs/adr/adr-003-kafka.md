# ADR-003: Apache Kafka Event-Driven Architecture

- **Status:** Accepted
- **Date:** 2026-07-12
- **Deciders:** Enterprise Architecture Review Board

## Problem

SporeKart's domains must react to each other asynchronously: a prompt update should notify the prompt-registry, a knowledge ingestion should refresh the semantic index, and governance events must be auditable across modules. Synchronous in-process calls would tightly couple modules and prevent the future extraction of services described in ADR-001. The platform needs a durable, replayable event backbone that survives module extraction.

## Decision

We adopt Apache Kafka as the platform-wide event backbone. Domain events are published through Spring Modulith's event publication registry using the transactional outbox pattern (see ADR-004 Kafka overlap in existing records) to guarantee at-least-once, in-order delivery without dual-write inconsistencies. Each module owns its topics, and the event-catalog registry documents every event schema, producer, and consumer. Kafka also serves as the transport for cross-module commands where asynchronous handling is acceptable.

## Alternatives Considered

- **RabbitMQ / AMQP** — Pros: simpler routing, mature Spring support. Cons: weaker replay/retention, less suitable as a durable log for service extraction.
- **In-process Spring events only** — Pros: zero infrastructure. Cons: breaks the extraction strategy and provides no durability or replay.

## Trade-offs

- Durability, replay, and scale at the cost of operational complexity and eventual consistency semantics.
- Loose coupling at the cost of debugging distributed flows.

## Consequences

- Positive: reliable asynchronous integration, replayable history, clean path to microservices.
- Negative: requires Kafka operations skill; follow-up is schema governance via the event-catalog registry.

## Compliance

Enforced by the event-catalog registry (all events registered), outbox pattern tests, and contract tests validating producer/consumer schema compatibility.
