# CI Validation

**Workflow:** `.github/workflows/playwright-regression.yml`
**Status:** ✅ PASS

---

## 1. Structural Validation (executed)

A dependency-free structural validator was run against the workflow:

```
node validate-ci.js .github/workflows/playwright-regression.yml
=> STRUCTURAL VALIDATION PASS
```

Checks performed and passed:

| Check | Result |
|-------|--------|
| No tab characters (YAML requires spaces) | ✅ 0 tabs |
| `workflow_dispatch` trigger present | ✅ |
| `report_dir` dispatch input present (C2) | ✅ |
| `compute-report-dir` job present (C2) | ✅ |
| `regression` job `needs: compute-report-dir` | ✅ |
| `PLAYWRIGHT_REPORT_DIR` env consumed (8 references) | ✅ |
| Per-project isolation `.../${{ matrix.project }}` | ✅ |
| Artifact uploads reference `QA_REPORTS/${{ env.PLAYWRIGHT_REPORT_DIR }}` | ✅ |
| All six matrix projects present | ✅ chromium, firefox, webkit, mobile-chrome, mobile-safari, tablet |
| No residual hard-coded `Regression/Sprint-B/RC2` default | ✅ removed |
| No residual hard-coded upload path | ✅ removed |

## 2. Workflow Design Review

| Aspect | Assessment |
|--------|------------|
| Triggers | `push` (release/**), `pull_request` (main, release/**), `workflow_dispatch` — appropriate for RC/regression gating |
| Runner | `ubuntu-latest` — GitHub-hosted, supports headless Firefox/WebKit/Chromium |
| Node | v20 via `actions/setup-node@v4` |
| Dependency caching | npm cache for `frontend/web-app` and `shared-testing` |
| Build | `npm ci` + `npm run build` before serving |
| App serving | `vite preview --port 4173` + 30-attempt health poll |
| Browser install | `npx playwright install --with-deps chromium firefox webkit` (installs Gecko + OS deps missing on the local Windows host — this is the Firefox qualification path for C3) |
| Matrix | six projects, `fail-fast: false` (one engine failing does not mask others) |
| Concurrency | `workers: CI ? 1` in config (stable, deterministic) |
| Retries | `retries: CI ? 2` |
| Artifacts | HTML report, JSON, JUnit, traces (on failure), videos, screenshots — per project, isolated dirs, retention 30–90 days |

## 3. Report-Path Consistency

The workflow and `playwright.config.ts` agree on the artifact contract:
- Config writes to `../QA_REPORTS/${PLAYWRIGHT_REPORT_DIR}/{html-report,results.json,junit.xml}`.
- Workflow uploads from `QA_REPORTS/${PLAYWRIGHT_REPORT_DIR}/...`.
- With `PLAYWRIGHT_REPORT_DIR = <base>/<project>`, every browser and every run is
  isolated.

## 4. Companion Workflow

`.github/workflows/build.yml` present and unmodified by this sprint.

## 5. Result

CI configuration is **validated**. The workflow satisfies C2 (artifact isolation)
and provides the C3 cross-browser execution path including Firefox.
