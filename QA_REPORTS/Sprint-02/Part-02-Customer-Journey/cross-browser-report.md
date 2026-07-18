# SporeKart QA Sprint 2 — Cross-Browser Report (Customer Journey)

**Date:** 2026-07-17

---

## Browser Matrix

| Test | Chromium | Firefox | WebKit | Mobile Chrome | Mobile Safari |
|------|----------|---------|--------|---------------|---------------|
| Homepage all viewports | ✅ | ❌ | ❌(flaky) | ⚠️(partial) | ⚠️(partial) |
| Nav bar viewport adapt | ✅ | ❌ | ✅ | ⚠️(partial) | ⚠️(partial) |
| Products page viewports | ✅ | ❌ | ✅ | ⚠️(partial) | ⚠️(partial) |
| Key pages all viewports | ✅(flaky) | ❌ | ❌(timeout) | ⚠️(partial) | ⚠️(partial) |
| Responsive images | ✅ | ❌ | ✅ | ⚠️(partial) | ⚠️(partial) |
| No horizontal scroll | ✅ | ❌ | ✅ | ⚠️(partial) | ⚠️(partial) |

**Legend:** ✅ Passed | ❌ Failed | ⚠️ Partial (test not completed)

## Viewport Coverage

| Viewport | Width | Height | Tested |
|----------|-------|--------|--------|
| Desktop | 1440 | 900 | ✅ |
| Laptop | 1024 | 768 | ✅ |
| Tablet | 768 | 1024 | ✅ |
| Mobile | 375 | 667 | ✅ |

## Findings

### Chromium
- All viewports render correctly
- No horizontal scroll on any page
- Responsive images load correctly
- Navigation bar adapts properly

### Firefox
- **Complete failure** — same root cause as Part 1 (mock API interception issue in Gecko)
- All 6 cross-browser tests time out at 30s
- Critical blocker

### WebKit
- Mixed results: homepage viewports failed, other tests passed
- Key pages across all viewports timed out (40s)
- Responsive images and no-horizontal-scroll passed

### Mobile
- Tests partially executed (timeout on batch)
- Chrome and Safari mobile projects initialized successfully

## Bugs

| ID | Issue | Browser | Severity |
|----|-------|---------|----------|
| BUG-CB-001 | All tests fail on Firefox | Firefox | Critical |
| BUG-CB-002 | Key pages timeout on WebKit viewport iteration | WebKit | High |
| BUG-CB-003 | Homepage viewport test fails on WebKit | WebKit | Medium |

## Recommendation

Same as Part 1: Investigate Firefox `page.route()` compatibility. For WebKit viewport tests, reduce iteration count or increase timeout.
