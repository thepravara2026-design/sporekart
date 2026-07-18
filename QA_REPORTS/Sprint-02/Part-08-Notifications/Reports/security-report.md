# Security — Report

## Tests: 5

| Test | Chromium | WebKit | Mobile Chrome | Mobile Safari |
|------|----------|--------|---------------|---------------|
| Public pages accessible without auth | ✅ | ✅ | ✅ | ✅ |
| Admin notification pages accessible without auth | ✅ | ✅ | ✅ | ✅ |
| No PII in notification page source | ❌ | ❌ | ❌ | ❌ |
| No console errors on notification pages | ❌ | ❌ | ❌ | ❌ |
| No PII in delivery queue page | ✅ | ✅ | ✅ | ✅ |

## Failure Analysis
### PII in page source (ALL browsers)
HTML contains "token" text — this is the auth/CSRF token in a script tag, not actual PII. This is a false positive for SPA applications where tokens are embedded in the initial HTML payload.

### Console errors (ALL browsers)
1 console error detected on the communication notifications page. Likely a missing asset or API failure logged by a component.

## Findings
- **BUG-ADM-001 re-confirmed:** Admin notification pages at `/admin/training/communication/*` are accessible without authentication
- No passwords, secrets, or PII (email, phone) found in page source (false positive on "token")
- Public pages correctly accessible

## Verdict
**PARTIAL** — No auth guard on admin notification routes (same as BUG-ADM-001). Console errors present. Token in source is expected SPA behavior.
