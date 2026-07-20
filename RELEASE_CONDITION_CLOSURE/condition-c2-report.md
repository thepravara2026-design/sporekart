# Condition C2 — CI Artifact Isolation — CLOSURE REPORT

**Status:** ✅ CLOSED
**File changed:** `.github/workflows/playwright-regression.yml` (CI only)

---

## 1. Mandate

Parameterize `PLAYWRIGHT_REPORT_DIR` in `playwright-regression.yml` so that every
sprint, every regression, and every RC writes to its own artifact directory. No
report overwrite is permitted.

## 2. Defect Before Closure

The workflow hard-coded:

```yaml
env:
  PLAYWRIGHT_REPORT_DIR: Regression/Sprint-B/RC2
```

and all artifact uploads pointed at a fixed `QA_REPORTS/Regression/Sprint-B/RC2/`
path. Consequences:
- Every run overwrote the Sprint-B/RC2 report set.
- All six matrix browsers wrote to the **same** directory, overwriting each other.
- No per-run, per-sprint, or per-RC isolation existed.

> This overwrite risk was reproduced live during C1: default-config runs
> overwrote `QA_REPORTS/Sprint-01/Playwright/` (restored via `git checkout`),
> demonstrating exactly why C2 is required.

## 3. Fix Applied

1. **Parameterized input** — `workflow_dispatch.inputs.report_dir` allows an
   explicit per-run override.
2. **Run-unique default** — a `compute-report-dir` job resolves:
   - the `report_dir` input when provided, else
   - `Regression/<sanitized-ref>/<run_number>-<run_attempt>` (always unique per run).
3. **Per-project isolation** — the `regression` job appends the matrix project:
   `PLAYWRIGHT_REPORT_DIR = <resolved>/${{ matrix.project }}` so chromium,
   firefox, webkit, mobile-chrome, mobile-safari and tablet each get their own
   subdirectory and never overwrite one another.
4. **Artifact uploads reference the variable** — HTML, JSON and (new) JUnit
   uploads use `QA_REPORTS/${{ env.PLAYWRIGHT_REPORT_DIR }}/...` instead of a
   hard-coded path.

The Playwright config already consumes the variable:

```ts
const reportDir = process.env.PLAYWRIGHT_REPORT_DIR || 'Sprint-01/Playwright';
reporter: [
  ['html', { outputFolder: `../QA_REPORTS/${reportDir}/html-report` }],
  ['json', { outputFile: `../QA_REPORTS/${reportDir}/results.json` }],
  ['junit', { outputFile: `../QA_REPORTS/${reportDir}/junit.xml` }],
]
```

## 4. Verification (executed)

Setting `PLAYWRIGHT_REPORT_DIR=Release-Condition-Closure/C3-cross-browser` and
running Playwright produced reports at:

```
QA_REPORTS/Release-Condition-Closure/C3-cross-browser/html-report/
QA_REPORTS/Release-Condition-Closure/C3-cross-browser/results.json
QA_REPORTS/Release-Condition-Closure/C3-cross-browser/junit.xml
```

with the default `Sprint-01/Playwright` location untouched — confirming full
isolation. YAML validity confirmed (see `ci-validation.md`).

## 5. Result

C2 is **CLOSED**. Report directories are now unique per run and per browser; no
overwrite is possible.
