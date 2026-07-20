# CI Workflow Documentation — Playwright Regression (Sprint B RC2)

**File:** `.github/workflows/playwright-regression.yml`

## 1. Workflow Triggers

| Trigger | Event | Target Branches |
|---------|-------|-----------------|
| Push | code push | `release/**` |
| Pull Request | opened, synchronize | `main`, `release/**` |
| Manual | `workflow_dispatch` | any branch (via GitHub UI or `gh`) |

## 2. Pipeline Overview

Single job (`regression`) runs in a 6-way matrix, one entry per Playwright project.
All 6 projects execute in parallel on independent Ubuntu runners.

| Matrix Entry | Playwright Project | Device |
|-------------|-------------------|--------|
| `chromium` | chromium | Desktop Chrome |
| `firefox` | firefox | Desktop Firefox |
| `webkit` | webkit | Desktop Safari |
| `mobile-chrome` | mobile-chrome | Pixel 5 |
| `mobile-safari` | mobile-safari | iPhone 13 |
| `tablet` | tablet | iPad (gen 7) |

## 3. Step Sequence (per matrix entry)

1. **Checkout** — `actions/checkout@v4`
2. **Setup Node** — `actions/setup-node@v4` (Node 20)
3. **Cache npm** — frontend/web-app node_modules (keyed by package-lock.json hash)
4. **Cache npm** — shared-testing node_modules (keyed by package-lock.json hash)
5. **Install frontend deps** — `npm ci` in `frontend/web-app`
6. **Build application** — `npm run build` (TypeScript + Vite)
7. **Start application** — `npx vite preview --port 4173` (daemonized)
8. **Health check** — curl loop (max 30 attempts, 2s interval, expects HTTP 200)
9. **Install test deps** — `npm ci` in `shared-testing`
10. **Install Playwright browsers** — `npx playwright install --with-deps chromium firefox webkit`
11. **Execute tests** — `npx playwright test --project=${{ matrix.project }}`
12. **Upload artifacts** — HTML report, JSON results, traces (on failure), videos, screenshots

## 4. Required Secrets

No secrets are required for the basic workflow. If the application under test needs
authentication credentials or API keys, add them as GitHub Actions secrets and pass
them as environment variables to the test step:

```yaml
- name: Run Playwright tests
  run: npx playwright test --project=${{ matrix.project }}
  env:
    TEST_USER: ${{ secrets.TEST_USER }}
    TEST_PASSWORD: ${{ secrets.TEST_PASSWORD }}
```

## 5. Runtime Requirements

- **Runner:** `ubuntu-latest` (GitHub-hosted)
- **Node:** 20.x
- **Browsers:** Chromium, Firefox, WebKit (system deps installed via `--with-deps`)
- **App Server:** Vite preview on port 4173 (built from `frontend/web-app`)
- **Playwright version:** ^1.61.1 (as specified in `shared-testing/package.json`)

## 6. Artifact Retention

| Artifact | Upload Condition | Path | Retention |
|----------|-----------------|------|-----------|
| HTML report | always | `QA_REPORTS/Regression/Sprint-B/RC2/html-report/` | 90 days |
| JSON results | always | `QA_REPORTS/Regression/Sprint-B/RC2/results.json` | 90 days |
| Test traces | on failure | `shared-testing/test-results/` | 30 days |
| Test videos | always | `shared-testing/test-results/**/*.webm` | 30 days |
| Screenshots | always | `shared-testing/test-results/**/*.png` | 30 days |

**Note:** Generated evidence is uploaded as CI artifacts — NEVER committed into the repository.
The `.gitignore` excludes `shared-testing/test-results/` and `shared-testing/playwright-report/`.

## 7. Report Output Structure

```
QA_REPORTS/
  Regression/
    Sprint-B/
      RC2/
        html-report/        ← Playwright HTML report (per project)
        results.json        ← JSON test results (per project)
        junit.xml           ← JUnit XML (per project)
```

The output path is controlled by `PLAYWRIGHT_REPORT_DIR=Regression/Sprint-B/RC2`
(set in `env` at the workflow top level). To change the target directory for a future
sprint, update this single variable.

## 8. Execution Instructions

### Via GitHub UI
1. Navigate to **Actions** → **Playwright Regression — Sprint B RC2**
2. Click **Run workflow** → select branch → **Run**

### Via CLI (gh)
```bash
gh workflow run playwright-regression.yml --ref bugfix/sprint-b-high-priority
```

### Via PR
Open a pull request targeting `main` or `release/*` and the workflow triggers automatically.

## 9. Local Execution (for comparison)

```bash
# Start the app
cd frontend/web-app
npm run build
npx vite preview --port 4173 &

# Run tests (all projects)
cd shared-testing
npm run test:ci

# Run a single project
npm run test:chromium

# Run with custom report path
PLAYWRIGHT_REPORT_DIR=Regression/Sprint-B/RC2 npx playwright test
```

---
*End of CI Workflow Documentation.*
