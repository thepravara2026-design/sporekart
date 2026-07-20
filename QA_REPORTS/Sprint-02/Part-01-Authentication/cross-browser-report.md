# SporeKart QA Sprint 2 — Cross-Browser Compatibility Report: Authentication

**Date:** 2026-07-17  
**Projects Tested:** 5 (chromium, firefox, webkit, mobile-chrome, mobile-safari)

---

## Browser Support Matrix

| Auth Feature | Chromium | Firefox | WebKit | Mobile Chrome | Mobile Safari |
|---|---|---|---|---|---|
| Phone/Email channel switching | ✅ | ❌ | ✅ | ✅ | ✅ |
| Empty field validation | ✅ | ❌ | ✅ | ✅ | ✅ |
| Invalid format validation | ✅ | ❌ | ✅ | ✅ | ✅ |
| Terms agreement gate | ✅ | ❌ | ✅ | ✅ | ✅ |
| Login → OTP redirect | ✅ | ❌ | ✅ | ✅ | ✅ |
| OTP 000000 rejection | ✅ | ❌ | ✅(flaky) | ✅ | ✅(flaky) |
| OTP success → session | ✅ | ❌ | ✅ | ✅ | ✅(flaky) |
| Resend OTP cooldown | ✅ | ❌ | ✅ | ✅ | ✅ |
| Register → OTP | ✅ | ❌ | ✅ | ✅ | ✅ |
| Forgot password | ✅ | ❌ | ✅ | ✅ | ✅ |
| Social sign-in buttons | ✅ | ❌ | ✅ | ✅ | ✅ |

## Detailed Results

### Chromium (120.0.6099.71) — 10/10 (100%)
All auth validation tests pass without issue. Fast execution (avg 4.2s per test).

### Firefox (latest) — 0/11 (0%)
**Complete failure.** All 11 auth tests time out at 30 seconds, with retries also timing out. Root cause is suspected to be a mock API interception issue in Gecko. Tests hang at page interaction before mock responses are received.

### WebKit (Safari 18) — 10/11 (90.9%)
10/11 pass. OTP fail (000000) test flaky on first run — passes on retry. OTP input `.fill()` occasionally times out.

### Mobile Chrome (Pixel 5) — 11/11 (100%)
All auth tests pass. Slightly slower than desktop Chromium (avg 7.5s vs 4.2s) due to device emulation overhead.

### Mobile Safari (iPhone 13) — 9/11 (81.8%)
9/11 pass, 2 flaky. Both flaky tests relate to OTP input filling — the `.fill()` action on individual OTP digit inputs occasionally times out. Both pass on retry.

## Cross-Browser Bugs

| ID | Browser | Issue | Severity |
|----|---------|-------|----------|
| BUG-AUTH-001 | Firefox | All auth tests time out | Critical |
| BUG-AUTH-006 | Mobile Safari | OTP input fill flaky | Medium |
| BUG-AUTH-007 | WebKit | OTP input fill flaky | Medium |

## Recommendation

1. **Firefox:** Investigate `page.route()` compatibility with Gecko. The mock API handler may need a Firefox-specific approach (e.g., using `page.route('**/api/**')` with explicit response headers for CORS).
2. **OTP Input:** Consider wrapping OTP input actions with a `fillOrType` helper that falls back to `page.type()` when `.fill()` fails on WebKit and iOS.
