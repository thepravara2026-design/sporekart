# Bug Register — Part 10: Performance

## Performance BUGs

| Bug ID | Module | Category | Severity | Priority | Browser | Description |
|--------|--------|----------|----------|----------|---------|-------------|
| BUG-PERF-001 | Responsive | Viewport | P3 | P3 | Mobile Safari | Desktop viewport test flaky on mobile-safari — timeout on first attempt, pass on retry. Likely emulator resource constraint. |

## Previously Confirmed BUGs
| Bug ID | Status | Notes |
|--------|--------|-------|
| BUG-001 | ACCEPTED | Firefox mock API interception failure — not tested in Part 10 |
| BUG-SEC-001 | CONFIRMED | Admin routes unprotected — performance unaffected |
| BUG-ADM-001 | CONFIRMED | No auth guard on /admin routes — performance unaffected |

## Summary
- **New bugs:** 1 (P3, flaky)
- **Confirmed existing:** 3
