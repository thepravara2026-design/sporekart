# Implementation Gap Register — Security

| # | Security Control | Current State | Expected State | Business Impact | Suggested Sprint |
|---|-----------------|---------------|----------------|-----------------|------------------|
| GAP-SEC-001 | Real authentication | AuthClient is a UX stub (mock) with simulated latency | Supabase Auth with OTP, password, OAuth | No real user authentication possible | S3 |
| GAP-SEC-002 | Server-side auth enforcement | All auth is client-side React state | Server-side session validation, JWT verification | Auth can be bypassed by manipulating local state | S3 |
| GAP-SEC-003 | Role persistence | Role resets to admin on page reload | Role persisted in secure session/token | Navigation-dependent role switching, inconsistent UX | S3 |
| GAP-SEC-004 | Auth guard on admin routes | /admin and sub-routes accessible without auth | ProtectedRoute component requiring authentication | Unauthorized admin access (BUG-ADM-001) | S3 |
| GAP-SEC-005 | Session management | No session tokens, cookies, JWT | Server-issued session with expiry, refresh | No real session state or expiry | S4 |
| GAP-SEC-006 | Server-side input validation | Only client-side regex validation | Server-side sanitization and validation on all inputs | Vulnerable to crafted requests bypassing client | S4 |
| GAP-SEC-007 | Rate limiting / account lockout | No rate limiting on OTP attempts | Rate limiting after N failed attempts | Brute force OTP bypass possible | S4 |
| GAP-SEC-008 | OTP brute force protection | Any non-zero code accepted | Time-limited, attempt-limited OTP verification | OTP guessing attack vector | S4 |
| GAP-SEC-009 | Privacy controls (real) | No real PII storage/transmission | GDPR/CCI-compliant privacy controls | Privacy compliance not addressed | S5 |
| GAP-SEC-010 | Audit logging | No audit log infrastructure | Event-driven audit log for auth/access events | No traceability for security events | S5 |
| GAP-SEC-011 | Password-based auth | OTP-only authentication | Password + OTP (MFA) supported | Limited authentication methods | S3 |
| GAP-SEC-012 | HTTPS enforcement | HTTP only (dev mode) | HTTPS-only with HSTS | No transport layer security | S3 |
