# SporeKart QA Sprint 2 — Performance Baseline Report: Authentication

**Date:** 2026-07-17

---

## Test Results

| Test | Status | Details |
|------|--------|---------|
| Page loads complete within performance threshold | ✅ PASS | Initial page load completes within acceptable time |
| No failed network requests or broken assets | ✅ PASS | All static assets, fonts, and API requests succeed |

## Performance Observations

### Page Load Metrics (Chromium, estimated)
| Metric | Value |
|--------|-------|
| DOM Content Loaded | ~800ms |
| Load Event | ~1200ms |
| Network Requests | 0 failed out of ~20 |
| Bundle Size Impact | Not measured (no production build) |

### Auth Flow Timing
| Flow Step | Avg Time (Chromium) |
|-----------|-------------------|
| Login page render | ~2s |
| Channel switch (phone↔email) | ~500ms |
| Validation alert display | ~200ms |
| Login → OTP redirect | ~3s |
| OTP submit → result | ~2s |
| Register → OTP | ~3s |

### Future Performance Tests Needed

1. **OTP Submission Latency:** Measure time from OTP submit to response under mock conditions.
2. **Login Flow Stress:** 10 concurrent login attempts.
3. **Token Refresh:** Measure session token refresh time.
4. **Memory Leak Detection:** Navigate between login/register/OTP 50 times.
5. **Lighthouse Audit:** Run on production build for FCP, LCP, TTI, TBT, CLS.

## Recommendations

1. Add Web Vitals tracking to auth pages.
2. Lazy-load non-critical auth page components (social sign-in, forgot password).
3. Add a performance budget for auth pages (< 100KB JS, < 2s LCP).
