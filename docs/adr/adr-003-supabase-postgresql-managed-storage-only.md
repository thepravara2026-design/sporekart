# ADR-003: Use Supabase PostgreSQL only for managed relational storage and backup services

- Status: Accepted
- Date: 2026-07-10
- Owners: Enterprise Architecture Review Board

## Context
The platform needs managed relational storage with operational resilience, backup, and recovery capabilities.

## Decision
Use Supabase PostgreSQL only for managed PostgreSQL, storage, backups, and point-in-time recovery. Do not use Supabase Auth, RLS, or edge features for application authorization.

## Alternatives considered
- Self-hosted PostgreSQL
- Other managed PostgreSQL providers
- Supabase Auth and RLS for application security

## Trade-offs
Supabase provides operational convenience, but the architecture must remain provider-agnostic for business authorization.

## Consequences
The platform preserves portability and avoids lock-in to Supabase-specific application authorization patterns.

## Approval
Reviewed and accepted by the ARB.
