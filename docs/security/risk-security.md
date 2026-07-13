# Risk Framework Security

## RBAC Model

| Role | Risk Permissions |
|------|-----------------|
| AI_ADMINISTRATOR | Full CRUD on all risk resources, threshold and rule configuration |
| AI_RISK_OFFICER | Manage risk rules and thresholds; review, escalate, override assessments |
| AI_AUDITOR | Read all risk resources; export audit logs and reports |
| AI_OPERATOR | Submit assessments; view own assessments, factors, trust, confidence |
| AI_VIEWER | Read-only access to dashboards, reports, assessment listings |

### Permission Verification Points

| Operation | Required Role | Endpoint |
|-----------|---------------|----------|
| Initiate assessment | AI_OPERATOR or higher | POST /api/v1/risk/assess |
| List assessments | AI_VIEWER or higher | GET /api/v1/risk/assessments |
| Get assessment details | AI_OPERATOR or higher (own) | GET /api/v1/risk/assessments/{id} |
| Add risk factors | AI_OPERATOR or higher | POST /api/v1/risk/assessments/{id}/factors |
| List risk factors | AI_OPERATOR or higher | GET /api/v1/risk/assessments/{id}/factors |
| Evaluate trust | AI_OPERATOR or higher | POST /api/v1/risk/assess/{id}/trust |
| Get trust evaluation | AI_OPERATOR or higher | GET /api/v1/risk/assess/{id}/trust |
| Calculate confidence | AI_OPERATOR or higher | POST /api/v1/risk/assess/{id}/confidence |
| Get confidence score | AI_OPERATOR or higher | GET /api/v1/risk/assess/{id}/confidence |
| Health check | Public (authenticated) | GET /api/v1/risk/health |

## Risk Authorization

Risk configuration (thresholds, rule definitions, factor weights) requires elevated privileges:

| Operation | Required Role |
|-----------|---------------|
| Read thresholds | AI_RISK_OFFICER or higher |
| Update thresholds | AI_ADMINISTRATOR |
| Create risk rules | AI_RISK_OFFICER or higher |
| Update risk rules | AI_RISK_OFFICER or higher |
| Delete risk rules | AI_ADMINISTRATOR |
| Override assessment recommendation | AI_RISK_OFFICER or higher |

### Assessment Ownership

- Assessments are tied to the requesting user
- Operators can view and manage only their own assessments
- Risk Officers and Administrators can view all assessments
- Auditors have read-only access to all assessments

## Risk Exception Codes

All risk errors use `RSK_` prefixed codes:

| Code | HTTP Status | Description |
|------|-------------|-------------|
| RSK_400 | 400 | Invalid risk request |
| RSK_401 | 401 | Authentication required |
| RSK_403 | 403 | Insufficient risk permissions |
| RSK_404 | 404 | Risk resource not found |
| RSK_409 | 409 | Invalid state transition (e.g., complete already-completed assessment) |
| RSK_422 | 422 | Risk evaluation failure (e.g., missing required metadata) |
| RSK_429 | 429 | Rate limit exceeded for risk assessment |
| RSK_500 | 500 | Internal risk engine error |

## Immutable Audit Trail

The `risk_audit` table enforces append-only semantics:

- **No UPDATE** — Database trigger prohibits modifications to audit records
- **No DELETE** — Database trigger prohibits deletion of audit records
- **Insert-only** — New audit entries can only be created, never modified

Each audit record captures:
- Action type (assessment started, completed, factor identified, trust evaluated, etc.)
- Actor ID (user or system principal)
- Related assessment ID
- Full details in JSON format
- Timestamp with millisecond precision

### Audit Event Types

| Event Type | Description |
|------------|-------------|
| ASSESSMENT_CREATED | Risk assessment initiated |
| ASSESSMENT_STARTED | Assessment processing started |
| ASSESSMENT_COMPLETED | Assessment completed with result |
| ASSESSMENT_FAILED | Assessment failed with error |
| FACTOR_ADDED | Risk factor added to assessment |
| TRUST_EVALUATED | Trust evaluation completed |
| CONFIDENCE_CALCULATED | Confidence calculation completed |
| RECOMMENDATION_GENERATED | Recommendation produced |
| RECOMMENDATION_OVERRIDDEN | Recommendation manually overridden |
| THRESHOLD_UPDATED | Risk threshold configuration changed |
| RULE_CREATED | Risk rule created |
| RULE_UPDATED | Risk rule updated |
| RULE_DELETED | Risk rule deleted |

## Configuration Authorization

Risk configuration (thresholds, weights, rules) is protected:

```yaml
risk:
  security:
    audit-immutable: true
    require-auth: true
    threshold-change-requires: AI_ADMINISTRATOR
    rule-management-requires: AI_RISK_OFFICER
    override-recommendation-requires: AI_RISK_OFFICER
    rate-limit:
      max-assessments-per-min: 50
      max-trust-evaluations-per-min: 100
      max-confidence-calculations-per-min: 100
```

## Security Enforcement Points

1. **API Gateway** — All `/api/v1/risk/*` requests authenticated
2. **Controller** — Method-level `@PreAuthorize` annotations
3. **Service Layer** — Role and permission checks in application services
4. **Repository Layer** — Soft-delete aware queries prevent data loss
5. **Database Layer** — Audit table immutability enforced via triggers
6. **Configuration** — Threshold and rule mutations gated by role checks
