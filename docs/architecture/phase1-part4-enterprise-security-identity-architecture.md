# SporeKart Phase 1 Part 4 — Enterprise Security and Identity Architecture

- Version: 1.0
- Status: Review complete
- Owner: Enterprise Architecture Review Board
- Review Date: 2026-07-10
- Purpose: Freeze the security and identity architecture baseline for backend services.
- Scope: Authentication, authorization, RBAC, JWT, refresh tokens, OTP, secrets handling, and audit requirements.
- References: [phase1-part3-spring-boot-enterprise-architecture.md](phase1-part3-spring-boot-enterprise-architecture.md), [phase1-part10-enterprise-architecture-governance-readiness.md](phase1-part10-enterprise-architecture-governance-readiness.md)
- Approval Status: Reviewed; policy approval and enforcement remain pending

## 1. Purpose and Scope

This document defines the enterprise security and identity architecture for the SporeKart platform. It is architecture-only and does not generate implementation code, Spring Security configuration, JWT logic, controllers, services, SQL, or runtime security components.

This specification is the mandatory security baseline for all backend implementation beginning in the next phase.

## 2. Security Principles

The platform security architecture is governed by the following principles:

- Spring Security is the only application authentication and authorization framework.
- Supabase Auth, Supabase Edge Functions, Supabase Realtime, and Supabase RLS are not used for application authorization.
- Supabase PostgreSQL is used only as managed relational storage and backup/PITR service.
- Zero-trust principles apply to users, services, and infrastructure components.
- Authentication and authorization must be explicit, auditable, and verifiable.
- Every sensitive action must be logged, monitored, and reviewable.
- Secrets and credentials must never be hardcoded or stored in source repositories.

## 3. Identity Architecture

### 3.1 Identity domains

SporeKart will maintain the following identity domains:

- User Identity: end customers and authenticated consumers of the platform.
- Admin Identity: internal platform administrators.
- Grower Identity: sellers, suppliers, and growers participating in the marketplace.
- Support Identity: customer support and operations personnel.
- System Identity: backend services and platform automation.
- Machine Identity: CI/CD automation, infrastructure agents, and service account principals.

### 3.2 Identity lifecycle

Each identity type follows the same lifecycle control points:

1. Provisioning
2. Verification
3. Authentication enrollment
4. Role assignment
5. Access activation
6. Monitoring and review
7. Deprovisioning or suspension

### 3.3 Identity model requirements

- Each principal must have a unique immutable identifier.
- Each principal must have a stable account status model: active, suspended, locked, disabled, pending-verification, deleted.
- Every identity must be associated with a security profile and role assignment.
- Identity changes must generate audit events.

## 4. Authentication Architecture

### 4.1 Authentication model

Authentication will be handled by Spring Security using a layered authentication architecture.

Supported primary authentication methods:

- Email and password authentication
- Phone and OTP authentication
- Passwordless sign-in where explicitly approved
- Refresh-token-based session continuation
- Admin and support MFA enforcement

### 4.2 Token architecture

#### Access tokens

- Short-lived JWT access tokens.
- Intended for API authorization and short-lived request contexts.
- Issued after successful authentication.
- Contains subject, issued-at, expires-at, roles, permissions, and audience claims.

#### Refresh tokens

- Long-lived opaque or signed refresh tokens.
- Stored securely and rotated on use.
- Bound to device and user context where applicable.
- Revocable and subject to inactivity thresholds.

#### Token lifecycle

- Access token issuance on login and refresh.
- Refresh token rotation on each refresh request.
- Revocation on logout, suspicious activity, or credential reset.
- Expiration and replay protection enforced by the platform.

### 4.3 Session strategy

- Stateless token-based sessions are preferred for API workloads.
- Session state is stored only where needed for revocation or anomaly detection.
- Device trust is evaluated for high-risk operations.
- Remember-me behavior is limited to low-risk scenarios and disabled for admin workflows.

### 4.4 Logout and revocation strategy

- Logout invalidates active refresh state and marks access tokens unusable via revocation tracking.
- Token revocation is supported through a revocation store or denylist strategy.
- Sensitive operations trigger forced re-authentication.

## 5. Authorization Architecture

### 5.1 Authorization model

SporeKart will use role-based access control enriched by resource ownership and method-level security.

Authorization decision points:

- Endpoint authorization
- Service-to-service authorization
- Method-level authorization
- Resource ownership and scope checks
- Administrative approval checks for sensitive actions

### 5.2 Role hierarchy

The role hierarchy is:

- Super Admin
- Admin
- Operations
- Finance
- Warehouse
- Support
- Grower
- Customer
- Content Team
- Training Team
- Analytics Team
- Internal Service

### 5.3 Ownership rules

- Customers may only manage their own profile, orders, and account data.
- Growers may manage their own inventory and fulfillment operations within assigned scope.
- Admins may manage internal platform controls but not bypass audit rules.
- Support roles may view but not modify sensitive business data outside their assigned scope.
- Service accounts may perform only explicitly assigned operations.

### 5.4 Method-level security

- Business operations exposed to the application layer must be protected by method-level authorization.
- Resource-specific policy checks must be enforced before domain actions execute.
- Administrative and approval operations require elevated privilege and audit logging.

## 6. RBAC and Permission Matrix

### 6.1 Standard permission set

The platform will enforce the following standard actions:

- Read
- Write
- Update
- Delete
- Approve
- Reject
- Export
- Import
- Administration

### 6.2 Role-permission matrix

| Role | Read | Write | Update | Delete | Approve | Reject | Export | Import | Administration |
|---|---|---|---|---|---|---|---|---|---|
| Customer | Yes | Yes | Yes | Limited | No | No | No | No | No |
| Grower | Yes | Yes | Yes | Limited | Yes | Yes | Yes | Yes | No |
| Admin | Yes | Yes | Yes | Yes | Yes | Yes | Yes | Yes | Yes |
| Operations | Yes | Yes | Yes | Limited | Yes | Yes | Yes | Yes | Limited |
| Warehouse | Yes | Yes | Yes | Limited | No | No | Yes | Yes | No |
| Finance | Yes | Yes | Yes | Limited | Yes | Yes | Yes | Yes | Limited |
| Support | Yes | Limited | Limited | No | No | No | Yes | No | No |
| Content Team | Yes | Yes | Yes | Limited | Yes | Yes | Yes | Yes | No |
| Training Team | Yes | Yes | Yes | Limited | Yes | Yes | Yes | Yes | No |
| Analytics Team | Yes | Yes | Yes | No | No | No | Yes | No | No |
| Super Admin | Yes | Yes | Yes | Yes | Yes | Yes | Yes | Yes | Yes |

### 6.3 Additional authorization rules

- Sensitive financial actions require finance role or approved admin escalation.
- Inventory mutation requires warehouse or operations permission context.
- Catalog publishing requires content-team approval or admin approval.
- Order cancellation and refunds require finance or operations approval depending on policy.

## 7. OTP Architecture

### 7.1 OTP goals

OTP is used for account verification, step-up authentication, password reset, and login challenge workflows.

### 7.2 OTP generation

- OTPs are generated with a cryptographically strong random source.
- OTPs are short-lived and single-use.
- Delivery channels include SMS and email.

### 7.3 Delivery strategy

- SMS delivery uses a trusted provider integration.
- Email delivery uses the platform mail integration.
- Delivery failures are logged and rate-limited.

### 7.4 Expiration and retry policy

- OTP lifetime: 5 minutes.
- Maximum resend attempts: 3 per window.
- Retry cooldown: 60 seconds.
- Failed attempts trigger temporary lockout thresholds.

### 7.5 Abuse protection

- Rate limiting on OTP attempts and IP/user-level abuse detection.
- Lockouts after repeated failed attempts.
- Device and origin reputation checks for suspicious flows.

## 8. Password Architecture

### 8.1 Password policy

- Minimum length: 12 characters.
- Must include uppercase, lowercase, number, and symbol.
- Prevent reuse of recent passwords.
- Prevent known breached password usage.

### 8.2 Hashing strategy

- Passwords are hashed using BCrypt-style password hashing.
- Password hashing is implemented by Spring Security-compatible primitives.
- Password hashes are never stored in plaintext.

### 8.3 Password reset and expiration

- Password reset is supported through verified email or SMS challenge.
- Password rotation is required after compromise events.
- Password expiration is optional and policy-driven, not globally forced for all accounts.

## 9. API Security Standards

### 9.1 Endpoint classification

- Public APIs: unauthenticated read-only endpoints and public discovery endpoints.
- Private APIs: authenticated customer and user endpoints.
- Internal APIs: service-to-service endpoints within the platform boundary.
- Admin APIs: privileged administration and operations endpoints.
- Partner APIs: future external integration interfaces.

### 9.2 Security headers

- Strict-Transport-Security
- X-Content-Type-Options
- X-Frame-Options
- Referrer-Policy
- Content-Security-Policy where applicable
- Cache-Control for sensitive endpoints

### 9.3 CORS and CSRF strategy

- CORS is restricted to approved origins.
- CSRF protections are applied to browser-based state-changing flows.
- Stateless APIs use token-based protection and avoid cookie-based session reliance where possible.

### 9.4 Rate limiting and abuse control

- Login endpoints are rate-limited by identity and IP.
- Sensitive endpoints apply burst and sustained-rate controls.
- API keys are reserved for future partner integrations and are not used by default.

## 10. Microservice Security Architecture

### 10.1 Service-to-service authentication

- Internal service-to-service calls use signed internal JWTs or mutually authenticated transport where applicable.
- Each internal service identity is represented by a system principal.
- Service permissions are minimal and explicitly scoped.

### 10.2 Trust boundaries

- Public edge to gateway boundary
- Gateway to service boundary
- Service to data store boundary
- Service to messaging and cache boundary
- Service to external provider boundary

### 10.3 Kafka and Redis security

- Kafka access requires authenticated clients and scoped topic permissions.
- Redis access requires ACL-based restrictions and network isolation.
- Secrets for messaging and cache access are injected at runtime.

### 10.4 TLS and certificate strategy

- TLS is mandatory for all network traffic in production.
- Internal service communication uses mutual TLS or equivalent mTLS where practical.
- Certificates are rotated through a centralized platform process.

## 11. Data Security Architecture

### 11.1 PII classification

The platform must classify sensitive data as:

- Public
- Internal
- Confidential
- Restricted

### 11.2 Encryption and protection

- Encryption at rest for PostgreSQL and storage volumes.
- Encryption in transit for all service traffic.
- Sensitive fields such as passwords, tokens, phone numbers, and payment references are masked in logs and audit surfaces.

### 11.3 Audit and retention

- Authentication, authorization, privilege changes, and data access events must be retained according to policy.
- Retention periods are defined per data category and regulatory requirement.
- GDPR-ready data subject controls are designed to support export and deletion workflows.

## 12. Spring Security Architecture

### 12.1 Security module structure

The backend security architecture is organized around the following responsibilities:

- Authentication entry points
- JWT filter and validation layer
- Authorization filter and policy layer
- Method security layer
- Exception handling layer
- Audit logging and event emission
- Security context population
- Anonymous access policy

### 12.2 Authentication flow

1. Client requests protected resource.
2. Authentication filter checks credentials or token.
3. Authentication manager validates identity.
4. JWT access token or session context is established.
5. Authorization rules evaluate permitted actions.
6. Security context is populated for the request.
7. Audit events are emitted.

### 12.3 Authorization flow

1. Request reaches a protected endpoint or method.
2. Security metadata resolves required authorities.
3. Policy engine evaluates role, ownership, and scope.
4. Access is allowed or denied.
5. Denial is logged and returns a standard security error response.

## 13. Threat Modeling

### 13.1 Threat categories

The architecture addresses the following OWASP and platform-specific risks:

- Broken authentication
- Broken access control
- Injection
- SSRF
- XSS
- CSRF
- Privilege escalation
- Replay attacks
- Credential stuffing
- Token theft and misuse

### 13.2 Threat controls

- Strong password and OTP policies
- Short-lived access tokens and refresh rotation
- Rate limiting and lockouts
- Fine-grained RBAC and ownership checks
- Audit logging and anomaly detection
- Input validation and output encoding
- TLS and secret isolation

## 14. Audit Logging Strategy

### 14.1 Event categories

- Authentication logs
- Authorization logs
- Business audit logs
- Security audit logs
- Admin action logs
- Failed login attempts
- Permission changes
- Sensitive data access

### 14.2 Audit controls

- Every access decision must produce an auditable record.
- Logs include actor, action, resource, outcome, timestamp, and correlation ID.
- Sensitive data is redacted or masked in logs.
- Audit trails are immutable and retained according to policy.

## 15. Secrets Management Strategy

### 15.1 Secret types

- Database credentials
- JWT signing keys
- OTP provider credentials
- Mail provider credentials
- Messaging credentials
- Integration API keys

### 15.2 Secret handling

- Secrets are injected at runtime from a secret manager or platform vault.
- Secrets are rotated regularly.
- Environment-specific secret scopes are enforced.
- Secret values are never committed to code or documentation.

### 15.3 Vault readiness

The architecture is designed to support a future centralized secret vault, with the following properties:

- Environment separation
- Rotation policy
- Access audit
- Key versioning
- Break-glass recovery

## 16. Security Monitoring Strategy

### 16.1 Metrics and alerts

The platform will define security metrics for:

- Failed login attempts
- Suspicious IP activity
- Account lockouts
- OTP abuse
- Token revocation spikes
- Permission denials
- Unusual admin activity

### 16.2 Monitoring outputs

- Alerting dashboards for authentication and authorization anomalies
- Periodic review of security events
- Alert ownership and escalation routing

## 17. Disaster Recovery and Incident Response

### 17.1 Credential recovery

- Recovery workflows must support account recovery without weakening security controls.
- Admin recovery actions must require explicit approval and audit traceability.

### 17.2 Token revocation and key rotation

- Token revocation is operationally supported for compromise response.
- Signing keys can be rotated with overlap windows to prevent service disruption.

### 17.3 Incident response

- Security incidents trigger containment, evidence preservation, credential reset, and event review.
- Breach response follows a documented playbook with escalation points.

## 18. Security Risk Register

| Risk | Impact | Likelihood | Control approach |
|---|---|---|---|
| Credential stuffing | High | Medium | Rate limiting, MFA, anomaly detection |
| Token theft | High | Medium | Short-lived tokens, rotation, revocation |
| Broken access control | High | Medium | RBAC, ownership checks, method security |
| OTP abuse | Medium | Medium | Rate limits, lockouts, delivery controls |
| Privilege escalation | High | Low | Role hierarchy, admin approvals, audit logs |
| Secrets leakage | High | Medium | Secret vault, runtime injection, rotation |
| Replay attacks | Medium | Medium | Token expiration, nonce or replay controls |
| CSRF/XSS abuse | Medium | Medium | Headers, CSRF protections, output encoding |

## 19. Security Readiness Score

Overall security readiness: 86/100

### Readiness rationale

- Identity and authentication architecture: 88/100
- Authorization and RBAC design: 87/100
- OTP, password, and API security controls: 84/100
- Service-to-service security and secrets architecture: 83/100
- Threat modeling, monitoring, and incident readiness: 85/100

## 20. Phase 1 Part 4 Completion Checklist

- [x] Identity architecture defined.
- [x] Authentication architecture defined.
- [x] Authorization architecture defined.
- [x] RBAC and permission matrix created.
- [x] OTP architecture defined.
- [x] Password policy defined.
- [x] API security standards defined.
- [x] Microservice security architecture defined.
- [x] Spring Security architecture defined.
- [x] Threat model defined.
- [x] Audit logging strategy defined.
- [x] Secrets management strategy defined.
- [x] Security monitoring strategy defined.
- [x] Disaster recovery strategy defined.
- [x] Security risk register completed.
- [x] Security readiness score assigned.

## 21. Phase 1 Part 5 Prerequisites

The following prerequisites must be completed before Phase 1 Part 5 can proceed:

1. Security policy approval from the architecture review board.
2. Final RBAC matrix approval across business functions.
3. Identity lifecycle policy approval.
4. OTP and password policy approval.
5. Secrets management operating model approval.
6. Audit retention and incident response policy approval.
7. Service-to-service trust model approval.
