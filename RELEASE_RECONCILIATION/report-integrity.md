# Report Integrity

**Date:** 2026-07-18
**Scope:** CI configuration, report directories, overwrite risk, internal consistency

---

## 1. CI Configuration Validation

| Workflow | File | Purpose | Status |
|---------|------|---------|--------|
| Build | `.github/workflows/build.yml` | Compile + typecheck + build | ✅ Present |
| Regression | `.github/workflows/playwright-regression.yml` | Cross-browser E2E | ⚠️ See §2 |

---

## 2. Report Directory Overwrite Risk

The `playwright-regression.yml` workflow hardcodes:

```yaml
env:
  PLAYWRIGHT_REPORT_DIR: Regression/Sprint-B/RC2
```

and is named **"Playwright Regression — Sprint B RC2"**. This means:
- Every regression run (regardless of actual sprint) writes to
  `QA_REPORTS/Regression/Sprint-B/RC2/…`.
- If Regression Sprint C or later runs reuse this workflow without changing the env
  var, **new results overwrite Sprint B RC2 artifacts** — violating the brief's
  *"No Sprint report should overwrite another sprint."*

**Severity:** Medium (CI hygiene). **Action for Gate D:** parameterize
`PLAYWRIGHT_REPORT_DIR` per run (workflow input or matrix) so each regression
produces a distinct directory.

---

## 3. Playwright Config Report Paths

`shared-testing/playwright.config.ts` uses:
```ts
const reportDir = process.env.PLAYWRIGHT_REPORT_DIR || 'Sprint-01/Playwright';
reporter: [['html', { outputFolder: `../QA_REPORTS/${reportDir}/html-report` }], ...]
```
When `PLAYWRIGHT_REPORT_DIR` is unset, results land in `Sprint-01/Playwright` —
again a default that could collide if not overridden. **Action:** ensure CI always
sets the env var (it does for regression runs).

---

## 4. Internal Report Consistency

| Check | Result |
|-------|--------|
| No contradictory exec summaries (after reconciliation) | ✅ QA Sprint 4 flagged historical; active set agrees |
| No duplicated bug IDs | ✅ (see bug-register-reconciliation.md) |
| No inconsistent severity for same ID | ✅ IDs consistent; only status was stale |
| No missing deliverables (Sprint D) | ✅ 11/11 required present |
| No missing deliverables (Reconciliation) | ✅ 12/12 present |

---

## 5. Verdict

⚠️ Report integrity is **good at the artifact level** but has **one CI configuration
defect**: the regression workflow hardcodes a Sprint B RC2 report directory, risking
overwrites. This must be parameterized before the next regression run (Regression
Sprint C / QA Sprint 5). Not a code defect; low effort to fix.
