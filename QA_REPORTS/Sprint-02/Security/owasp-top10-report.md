# OWASP Top 10 Assessment Report — QA Sprint 2 Part 10

**Release:** v1.0.0-rc1 | **Date:** 2026-07-17

---

## Overall OWASP Compliance: ❌ FAIL (25/100)

| Rank | Category | Score | Risk | Status |
|------|----------|-------|------|--------|
| A01 | Broken Access Control | 10/100 | 🔴 CRITICAL | ❌ FAIL |
| A02 | Cryptographic Failures | 40/100 | 🟡 MEDIUM | ⚠️ WARNING |
| A03 | Injection | 85/100 | 🟢 LOW | ✅ PASS |
| A04 | Insecure Design | 15/100 | 🔴 CRITICAL | ❌ FAIL |
| A05 | Security Misconfiguration | 20/100 | 🟠 HIGH | ❌ FAIL |
| A06 | Vulnerable Components | 70/100 | 🟢 LOW | ⚠️ WARNING |
| A07 | Authentication Failures | 5/100 | 🔴 CRITICAL | ❌ FAIL |
| A08 | Software Integrity | 80/100 | 🟢 LOW | ✅ PASS |
| A09 | Logging Failures | 30/100 | 🟡 MEDIUM | ❌ FAIL |
| A10 | SSRF | 90/100 | 🟢 LOW | ✅ N/A |

---

## A01: Broken Access Control — 🔴 CRITICAL (10/100)

### Routes

| Finding | Status | Risk |
|---------|--------|------|
| Route guard components exist | ❌ NONE FOUND | 🔴 CRITICAL |
| Protected routes without guards | 256 of 256 protected routes | 🔴 CRITICAL |
| Role-based UI filter bypassable | ✅ Yes — canView() is client-side only | 🔴 CRITICAL |
| Default role is administrator | ✅ All users start as admin | 🔴 CRITICAL |
| Privilege escalation via URL | ✅ Direct URL access works for any route | 🔴 CRITICAL |

**Evidence:** Part 4 Route Protection report documented 337 routes total, 256 protected by role config, 0 with actual guard components.

### API Endpoints

| Finding | Status | Risk |
|---------|--------|------|
| Endpoints with authentication | 27 of 155 (17.4%) | 🔴 CRITICAL |
| Endpoints without authentication | 128 of 155 (82.6%) | 🔴 CRITICAL |
| Ownership verification | ❌ NONE | 🔴 CRITICAL |

---

## A02: Cryptographic Failures — 🟡 MEDIUM (40/100)

| Finding | Status | Risk |
|---------|--------|------|
| HTTPS enforcement | ❌ Not configured | 🟡 MEDIUM |
| Password hashing | ❌ Not applicable (mock auth) | 🟢 LOW |
| Token encryption | ❌ Not implemented | 🟡 MEDIUM |
| OTP in plaintext | ✅ Mock, but pattern is plaintext | 🟡 MEDIUM |
| TLS configuration | ❌ Not configured | 🟡 MEDIUM |

---

## A03: Injection — 🟢 LOW (85/100)

| Finding | Status | Risk |
|---------|--------|------|
| SQL injection vectors | ✅ None — no SQL in frontend | 🟢 LOW |
| NoSQL injection vectors | ✅ None — no NoSQL in frontend | 🟢 LOW |
| XSS via dangerouslySetInnerHTML | ✅ Not used anywhere | 🟢 LOW |
| XSS via innerHTML | ✅ Not used anywhere | 🟢 LOW |
| eval() usage | ✅ None found | 🟢 LOW |
| Template injection | ✅ None — no template engine | 🟢 LOW |
| React JSX auto-escaping | ✅ Used throughout | 🟢 LOW |

---

## A04: Insecure Design — 🔴 CRITICAL (15/100)

| Finding | Status | Risk |
|---------|--------|------|
| Security-by-design | ❌ Not observed | 🔴 CRITICAL |
| Auth is entirely mock | ❌ No real authentication | 🔴 CRITICAL |
| Session management | ❌ Not implemented | 🔴 CRITICAL |
| Rate limiting | ❌ Not implemented | 🟠 HIGH |
| CSRF protection | ❌ Not implemented | 🟠 HIGH |
| Input validation server-side | ❌ Not implemented | 🟠 HIGH |
| Principle of least privilege | ❌ Not followed (all roles start as admin) | 🔴 CRITICAL |

---

## A05: Security Misconfiguration — 🟠 HIGH (20/100)

| Finding | Status | Risk |
|---------|--------|------|
| Content-Security-Policy | ❌ NOT CONFIGURED | 🟠 HIGH |
| X-Frame-Options | ❌ NOT CONFIGURED | 🟠 HIGH |
| X-Content-Type-Options | ❌ NOT CONFIGURED | 🟡 MEDIUM |
| Strict-Transport-Security | ❌ NOT CONFIGURED | 🟠 HIGH |
| Referrer-Policy | ❌ NOT CONFIGURED | 🟡 MEDIUM |
| Permissions-Policy | ❌ NOT CONFIGURED | 🟡 MEDIUM |
| X-XSS-Protection | ❌ NOT CONFIGURED | 🟡 MEDIUM |
| Cache-Control | ❌ NOT CONFIGURED | 🟡 MEDIUM |
| CORS headers | ❌ NOT CONFIGURED | 🟠 HIGH |
| Debug pages in production | ✅ Demo/preview routes accessible | 🟡 MEDIUM |

---

## A06: Vulnerable Components — 🟢 LOW (70/100)

| Finding | Status | Risk |
|---------|--------|------|
| Runtime dependencies | 3 (react, react-dom, react-router-dom) | 🟢 LOW |
| Known CVEs in deps | Not audited — manual check shows no critical CVEs | 🟡 MEDIUM |
| Outdated packages | ✅ All deps at latest stable versions | 🟢 LOW |
| npm audit performed | ❌ Not executed | 🟡 MEDIUM |
| Unused dependencies | ✅ None identified | 🟢 LOW |

---

## A07: Authentication Failures — 🔴 CRITICAL (5/100)

| Finding | Status | Risk |
|---------|--------|------|
| Real authentication flow | ❌ NOT IMPLEMENTED — mock only | 🔴 CRITICAL |
| Authentication required | ❌ Default role = administrator, no auth check | 🔴 CRITICAL |
| MFA/2FA | ❌ Not implemented | 🟠 HIGH |
| Biometric auth | ❌ Not implemented | 🟡 MEDIUM |
| Account lockout | ❌ Not implemented | 🟠 HIGH |
| Password policies | ❌ Not implemented | 🟠 HIGH |
| Session management | ❌ Not implemented | 🔴 CRITICAL |
| OTP validation | ❌ Mock — any code except 000000 works | 🔴 CRITICAL |

---

## A08: Software Integrity Failures — 🟢 LOW (80/100)

| Finding | Status | Risk |
|---------|--------|------|
| Subresource Integrity | ❌ Not configured | 🟢 LOW |
| Dependencies from official source | ✅ All from npm | 🟢 LOW |
| CI/CD pipeline integrity | ❌ Not analyzed | 🟢 LOW |

---

## A09: Logging Failures — 🟡 MEDIUM (30/100)

| Finding | Status | Risk |
|---------|--------|------|
| Centralized logging | ❌ Not implemented | 🟡 MEDIUM |
| Security event logging | ❌ Not implemented | 🟠 HIGH |
| Audit trail | ❌ Not implemented | 🟠 HIGH |
| Console.log in production | ⚠️ Present in multiple components | 🟡 MEDIUM |

---

## A10: SSRF — 🟢 LOW (90/100)

**Not applicable.** No server-side URL fetching implemented.

---

## OWASP Risk Heat Map

```
                     Impact
              LOW    MEDIUM   HIGH   CRITICAL
      HIGH    ┌──────┬────────┬──────┬────────┐
              │      │   A09  │ A05  │  A01   │
  Likelihood  │      │        │      │  A04   │
              │      │        │      │  A07   │
      MEDIUM  ├──────┼────────┼──────┼────────┤
              │ A03  │   A02  │      │        │
              │ A06  │        │      │        │
              │ A08  │        │      │        │
              │ A10  │        │      │        │
      LOW     └──────┴────────┴──────┴────────┘
```

---

*End of OWASP Top 10 Assessment*
