# ADR-004: Use Apache Kafka with the outbox pattern for inter-service events

- Status: Accepted
- Date: 2026-07-10
- Owners: Enterprise Architecture Review Board

## Context
The platform requires asynchronous integration, resilient event propagation, and decoupled service collaboration.

## Decision
Use Apache Kafka as the event backbone and adopt the outbox pattern for reliable publication of domain events.

## Alternatives considered
- Direct synchronous calls between services
- RabbitMQ
- Event publication without transactional outbox support

## Trade-offs
Kafka adds operational complexity but enables resilience, replay, and loose coupling.

## Consequences
The platform gains scalable and durable integration patterns for cross-service workflows.

## Approval
Reviewed and accepted by the ARB.
