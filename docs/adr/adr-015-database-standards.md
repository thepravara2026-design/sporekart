# ADR-015: Database Standards — Flyway, PostgreSQL, H2 Tests

- **Status:** Accepted
- **Date:** 2026-07-12
- **Deciders:** Enterprise Architecture Review Board

## Problem

SporeKart uses Supabase PostgreSQL as its managed system of record. Schema changes across many modules must be safe, reviewable, and reproducible across environments, and tests must run fast and locally without external dependencies. Ad-hoc or unversioned schema changes risk data loss, environment drift, and broken CI.

## Decision

We standardize on Flyway for all PostgreSQL schema migrations, with versioned, peer-reviewed SQL migrations committed alongside code and applied in order across environments. Supabase PostgreSQL is the production and staging database; local and CI tests use H2 in PostgreSQL-compatible mode so the full test suite runs without external infrastructure while staying faithful to production SQL semantics. Each module owns its schema namespace, and cross-module data access is forbidden (ADR-002). The config-registry documents connection and migration conventions, and migrations are part of the deployment pipeline with rollback planning.

## Alternatives Considered

- **Liquibase** — Pros: declarative changelogs. Cons: XML/YAML overhead, team less familiar.
- **JPA auto-DDL** — Pros: zero migration files. Cons: non-deterministic, unsafe for production.

## Trade-offs

- Reproducible, auditable schema at the cost of writing and reviewing migration SQL.
- Fast local H2 tests at the cost of occasional H2/PostgreSQL dialect mismatches (mitigated by CI on real PostgreSQL).

## Consequences

- Positive: safe deployments, environment parity, fast tests, clear ownership.
- Negative: migration discipline required; follow-up is a CI job against real Supabase for dialect checks.

## Compliance

Enforced by Flyway migration reviews, H2-based integration tests in CI, and ArchUnit rules forbidding cross-module schema access.
