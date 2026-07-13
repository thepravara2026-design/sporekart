# Policy Engine Security

## Overview

Policy engine security covers endpoint authorization, input validation, expression sandboxing, and audit immutability. This sprint does NOT implement RBAC roles, encryption, or compliance features (those are Sprint 18 Part 3+ scope).

## Endpoint Authorization

Defined in `SecurityConfig.java` — `/api/v1/policies/**` is permitted for all authenticated requests. No role-based access control is enforced at the API layer in this sprint.

```
// SecurityConfig excerpt
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/v1/policies/**").permitAll()
    ...
)
```

## Input Validation

### Request Validation (`PolicyValidatorImpl`)

- `validateRequest(EvaluationRequest)`: Checks module and action are non-blank
- `validatePolicy(Policy)`: Checks name and type are non-null
- `validateRule(PolicyRule)`: Checks rule name is non-blank
- Returns `List<PolicyViolation>` — empty list means valid

### Policy Validation (`PolicyCompilerImpl.validatePolicy`)

- Checks policy name is non-blank
- Returns violations with field-level detail

### Exception Handling

`PolicyController.handleException` catches all unhandled exceptions and returns RFC 9457-style error:
```json
{
  "type": "about:blank",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "Error message",
  "extensions": {}
}
```

## Expression Sandboxing

Current implementation has **no expression sandboxing**:

- `PolicyCompilerImpl` is a pass-through stub — `compile()` always returns compiled=true with language="simple"
- `PolicyEvaluatorImpl.evaluateExpression()` performs simple string matching against module, action, role, and parameter values
- No SpEL, MVEL, or any expression language parsing
- No AST validation, no bytecode generation, no sandbox execution
- No injection protection for expression strings

**Limitation:** Expression strings are evaluated via `String.toLowerCase().contains(...)` pattern matching. This is not a real expression engine and provides no security guarantees against malicious expression injection.

## Condition Evaluation Safety

`ConditionEvaluator` handles type coercion safely:
- Numeric comparisons use `Double.compare()` for safe Number-to-Number comparison
- String comparisons use `toString().equalsIgnoreCase()` — null-safe
- Comparable types use `Comparable.compareTo()` with type checking
- All methods are null-safe (return false for null inputs where appropriate)

## Audit Immutability

Unlike the Governance module (Sprint 18 Part 1), the Policy Engine audit table (`ai_policy_audit`) does **not** have DB-level immutability triggers. Audit records are soft-deletable via `is_deleted` flag.

- Audit records are created during each policy evaluation
- No UPDATE/DELETE trigger protection at the database level
- Record immutability relies on application-layer behavior (never exposed update endpoints)

## Kafka Event Security

Events published to `policy-events` topic:
- No event encryption
- No event schema validation
- Event envelope includes: id, type, timestamp, source, details
- Source is always "policy" — identifies the emitting module

## Cache Security

Redis cache (`PolicyRedisCacheService`):
- No encryption at rest
- No access control on cache namespaces
- Cache keys follow `policy:<namespace>:<key>` pattern
- TTL-based expiration (60s–600s depending on namespace)

## Exception Codes

`PolicyException` provides standard error codes:

| Code | HTTP Status | Trigger |
|------|-------------|---------|
| POL_400 | 400 | Bad request / invalid input |
| POL_404 | 404 | Policy or resource not found |
| POL_500 | 500 | Evaluation failure or internal error |

## Security Limitations (Sprint 18 Part 2)

- No RBAC — all authenticated users can access all policy endpoints
- No encryption — policy data and cache values are plaintext
- No input sanitization — expression strings are not sanitized
- No expression sandboxing — expression "compilation" is a pass-through
- No rate limiting — policy API has no request throttling
- No SQL injection protection beyond JPA parameterized queries
- No audit immutability at DB level — soft-delete allows record hiding
- No secrets management — database credentials and Kafka configs are plaintext in application.yml
