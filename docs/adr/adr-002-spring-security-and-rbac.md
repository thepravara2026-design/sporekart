# ADR-002: Adopt Spring Security and RBAC for authentication and authorization

- Status: Accepted
- Date: 2026-07-10
- Owners: Enterprise Architecture Review Board

## Context
The platform requires a unified authentication and authorization model across services and workloads.

## Decision
Use Spring Security as the application security framework. Use JWT access tokens, refresh tokens, OTP workflows, and RBAC for authorization decisions.

## Alternatives considered
- Supabase Auth as the application auth layer
- Custom security middleware
- Role-only access without resource ownership checks

## Trade-offs
Spring Security offers strong enterprise controls, but requires careful configuration, policy ownership, and testing.

## Consequences
The platform gains consistent and auditable authentication and authorization behavior.

## Approval
Reviewed and accepted by the ARB.
