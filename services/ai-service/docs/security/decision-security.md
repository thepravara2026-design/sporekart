# Decision Engine Security

## RBAC

All `/api/v1/decisions/**` endpoints are configured with `.permitAll()` in `SecurityConfig.java`. No role-based access control is enforced at this phase.

**Current status:** All endpoints publicly accessible.

## Input Validation

- `DecisionRequestDto` accepts raw fields with no explicit validation annotations
- Controller catches unhandled exceptions via `@ExceptionHandler(Exception.class)` and returns RFC 9457 error
- No `@Valid` or `@NotNull` constraints on DTO fields
- Null payloads/contexts/roles are handled with defensive defaults in controller (empty maps/lists)

## Immutable Audit Trail

Audit records in `ai_decision_audit` are append-only via soft-delete:

- Records are never physically deleted (`is_deleted = TRUE` for soft delete)
- No DB triggers enforce immutability (soft delete is application-level)
- Audit records capture: requestId, decisionId, action, status, confidence, reasons, context, userId, processingTimeMs, success, timestamp
- Audit created at the end of every `DecisionEngine.evaluate()` call

## DecisionException

`DecisionException` in `infrastructure/security/` provides error codes:

| Method | Error Code | HTTP Status |
|--------|------------|-------------|
| `notFound()` | DEC_404 | 404 |
| `badRequest()` | DEC_400 | 400 |
| `evaluationFailed()` | DEC_500 | 500 |

## Security Considerations

- No PII or sensitive data handling in this module
- No encryption of audit trail data
- No input sanitization for XSS or injection
- No rate limiting at decision engine level (relies on gateway-level rate limiting)
- Roles passed as request data — not authenticated by the decision engine
- Configuration values stored in-memory (ConcurrentHashMap) — not encrypted
- Decision results cached in Redis with 300s TTL — cache contains decision summaries
