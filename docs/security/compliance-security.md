# Compliance Framework Security

## RBAC Model

| Role | Compliance Permissions |
|------|----------------------|
| AI_ADMINISTRATOR | Full CRUD on frameworks, rules, controls, assessments, reports, exceptions, audit |
| AI_COMPLIANCE_OFFICER | Manage frameworks, rules, controls; create/review/approve exceptions; view audit |
| AI_AUDITOR | Read all compliance resources; export audit logs and reports |
| AI_OPERATOR | Submit validation requests; view own assessments and violations; request exceptions |
| AI_VIEWER | Read-only access to dashboards, reports, framework listings |

### Permission Verification Points

| Operation | Required Role | Endpoint |
|-----------|---------------|----------|
| Validate request | AI_OPERATOR or higher | POST /api/v1/compliance/validate |
| List frameworks | AI_VIEWER or higher | GET /api/v1/compliance/frameworks |
| List rules | AI_VIEWER or higher | GET /api/v1/compliance/rules |
| List reports | AI_VIEWER or higher | GET /api/v1/compliance/reports |
| List violations | AI_OPERATOR or higher | GET /api/v1/compliance/violations |
| List exceptions | AI_OPERATOR or higher | GET /api/v1/compliance/exceptions |
| Create exception | AI_OPERATOR or higher | POST /api/v1/compliance/exceptions |
| Approve exception | AI_COMPLIANCE_OFFICER or higher | Part of exception workflow |
| View statistics | AI_VIEWER or higher | GET /api/v1/compliance/statistics |
| Health check | Public (authenticated) | GET /api/v1/compliance/health |

## Compliance Exception Codes

All compliance errors use `CMP_` prefixed codes:

| Code | HTTP Status | Description |
|------|-------------|-------------|
| CMP_400 | 400 | Invalid compliance request |
| CMP_401 | 401 | Authentication required |
| CMP_403 | 403 | Insufficient compliance permissions |
| CMP_404 | 404 | Compliance resource not found |
| CMP_409 | 409 | Invalid state transition (e.g., approve already-approved exception) |
| CMP_422 | 422 | Validation rule failure (e.g., missing framework) |
| CMP_429 | 429 | Rate limit exceeded for compliance validation |
| CMP_500 | 500 | Internal compliance engine error |

## Immutable Audit Trail

The `compliance_audit` table enforces append-only semantics:

- **No UPDATE** — Database trigger prohibits modifications to audit records
- **No DELETE** — Database trigger prohibits deletion of audit records
- **Insert-only** — New audit entries can only be created, never modified

Each audit record captures:
- Action type (validation started, completed, violation detected, etc.)
- Actor ID (user or system principal)
- Related assessment ID
- Full details in JSON format
- Timestamp with millisecond precision

### Audit Event Types

| Event Type | Description |
|------------|-------------|
| VALIDATION_STARTED | Compliance validation request initiated |
| VALIDATION_COMPLETED | Validation completed with result |
| VALIDATION_FAILED | Validation failed with error |
| EVIDENCE_COLLECTED | Evidence collected for assessment |
| RULE_EVALUATED | Individual rule evaluated |
| VIOLATION_DETECTED | Non-compliance detected |
| REPORT_GENERATED | Compliance report generated |
| EXCEPTION_REQUESTED | Exception requested |
| EXCEPTION_APPROVED | Exception approved |
| EXCEPTION_REJECTED | Exception rejected |
| EXCEPTION_EXPIRED | Exception auto-expired |
| EXCEPTION_REVOKED | Exception manually revoked |
| FRAMEWORK_REGISTERED | New framework registered |
| FRAMEWORK_DEACTIVATED | Framework deactivated |

## Evidence Integrity

All compliance evidence is integrity-verified:

1. **SHA-256 hash** — Each evidence record stores a hash of the evidence value
2. **Verification on read** — Evidence integrity verified before use in evaluation
3. **Tamper detection** — Mismatched hash results in evaluation failure
4. **Re-collection on failure** — Failed verification triggers evidence re-collection

## Security Configuration

```yaml
compliance:
  security:
    audit-immutable: true
    evidence-verification: true
    require-auth: true
    exception-requires-approval: true
    rate-limit:
      max-validations-per-min: 100
      max-exception-requests-per-hour: 10
```

## Security Enforcement Points

1. **API Gateway** — All `/api/v1/compliance/*` requests authenticated
2. **Controller** — Method-level `@PreAuthorize` annotations
3. **Service Layer** — Role and permission checks in application services
4. **Repository Layer** — Soft-delete aware queries prevent data loss
5. **Database Layer** — Audit table immutability enforced via triggers
