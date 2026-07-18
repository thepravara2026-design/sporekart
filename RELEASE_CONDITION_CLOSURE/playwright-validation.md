# Playwright Validation

**Config:** `shared-testing/playwright.config.ts`
**Playwright:** 1.61.1 · **Node:** v24.16.0 (local) / v20 (CI)
**Status:** ✅ PASS

---

## 1. Configuration Review

| Setting | Value | Assessment |
|---------|-------|------------|
| `testDir` | `./tests` | ✅ |
| `reportDir` source | `process.env.PLAYWRIGHT_REPORT_DIR || 'Sprint-01/Playwright'` | ✅ consumes C2 variable |
| Reporters | html + json + junit + list, all under `../QA_REPORTS/${reportDir}/...` | ✅ isolation-aware |
| `baseURL` | `process.env.BASE_URL || 'http://localhost:5173'` | ✅ overridable |
| `forbidOnly` | `!!process.env.CI` | ✅ |
| `retries` | `CI ? 2 : 1` | ✅ |
| `workers` | `CI ? 1 : undefined` | ✅ deterministic in CI |
| trace/video/screenshot | on | ✅ full evidence |
| Projects | chromium, firefox, webkit, mobile-chrome, mobile-safari, tablet | ✅ full matrix |

## 2. Spec Reconciliation Verification (C1)

App built (`vite build`) and served on `http://localhost:4173`.

### Cross-browser run (WIP-inclusive build)
```
QA_REPORTS/Release-Condition-Closure/C3-cross-browser/
chromium + webkit + mobile-chrome + mobile-safari + tablet
=> 125 passed
```

### Baseline re-verification (production WIP stashed)
```
chromium + tablet, 3 reconciled specs
=> 50 passed
```

Confirms the reconciled specs are correct against the committed baseline
independent of stashed prior-sprint WIP.

## 3. Smoke Validation
```
--project=chromium smoke.spec.ts  => 6 passed
```

## 4. Report-Generation Validation

Running with `PLAYWRIGHT_REPORT_DIR=Release-Condition-Closure/C3-cross-browser`
produced:
```
QA_REPORTS/Release-Condition-Closure/C3-cross-browser/html-report/
QA_REPORTS/Release-Condition-Closure/C3-cross-browser/results.json
QA_REPORTS/Release-Condition-Closure/C3-cross-browser/junit.xml
```
The default `Sprint-01/Playwright` location was **not** touched → isolation
confirmed.

## 5. Browser Matrix Status

| Project | Engine | Local | CI |
|---------|--------|-------|----|
| chromium | Chromium | ✅ PASS | ✅ supported |
| firefox | Gecko | ⚠️ cannot launch on Windows host | ✅ via `--with-deps` on ubuntu |
| webkit | WebKit | ✅ PASS | ✅ supported |
| mobile-chrome | Chromium (Pixel 5) | ✅ PASS | ✅ supported |
| mobile-safari | WebKit (iPhone 13) | ✅ PASS | ✅ supported |
| tablet | WebKit (iPad gen 7) | ✅ PASS | ✅ supported |

## 6. Result

Playwright configuration and the reconciled suites are **validated**. 125/125 pass
across five local engines; report generation and artifact isolation confirmed;
Firefox qualification delegated to CI.
