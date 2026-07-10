# ADR-001: Adopt Java 21 LTS and Spring Boot 3.x as the backend baseline

- Status: Accepted
- Date: 2026-07-10
- Owners: Enterprise Architecture Review Board

## Context
The platform requires a modern, long-term-support backend stack with strong ecosystem support, security, observability, and maintainability.

## Decision
Adopt Java 21 LTS and Spring Boot 3.x as the baseline runtime and application framework for all backend services.

## Alternatives considered
- Java 17
- Java 11
- Quarkus
- Micronaut
- Custom framework layer

## Trade-offs
Java 21 and Spring Boot 3.x provide strong ecosystem maturity and long-term support, but require disciplined architecture and toolchain alignment.

## Consequences
The platform gains a modern, maintainable, and enterprise-compatible backend baseline.

## Approval
Reviewed and accepted by the ARB.
