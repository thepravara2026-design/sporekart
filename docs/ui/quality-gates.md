# Quality Gates — SporeKart Enterprise Web Application

## 1. Gate Overview

Every frontend implementation must pass **7 mandatory gates** before production deployment. Gates are sequential; no gate may be skipped.

| Gate | Name | Owner | Type | Automation |
|------|------|-------|------|------------|
| **G1** | Lint & Type Safety | CI | Automated | ✅ |
| **G2** | Unit & Integration Tests | CI | Automated | ✅ |
| **G3** | Accessibility Automated | CI | Automated | ✅ |
| **G4** | Build & Bundle Analysis | CI | Automated | ✅ |
| **G5** | Lighthouse CI (Perf + A11y) | CI | Automated | ✅ |
| **G6** | Manual Review Gates (1–5) | Reviewers | Manual | ❌ |
| **G7** | Final Release Approval | Release Manager | Manual | ❌ |

**All gates must pass.** No exceptions without documented DER.

---

## 2. Automated Gate Specifications

### G1: Lint & Type Safety

| Tool | Config | Threshold |
|------|--------|-----------|
| ESLint | `.eslintrc.json` + `eslint-plugin-jsx-a11y` | 0 errors |
| Stylelint | `.stylelintrc.json` + token lint | 0 errors |
| TypeScript | `tsc --noEmit --strict` | 0 errors |
| Token Lint | Custom rule: no hardcoded values | 0 violations |

**Failure:** PR blocked; must fix before merge.

---

### G2: Unit & Integration Tests

| Metric | Threshold |
|--------|-----------|
| **Unit Test Coverage** | ≥ 80% statements / branches / functions / lines |
| **Integration Tests** | All critical user flows covered |
| **Flaky Tests** | 0 tolerated |
| **Test Duration** | < 5 minutes total |

**Tools:** Vitest + React Testing Library + MSW

---

### G3: Accessibility Automated

| Tool | Config | Threshold |
|------|--------|-----------|
| axe-core | WCAG 2.2 AA | 0 violations |
| eslint-plugin-jsx-a11y | Recommended | 0 errors |
| Storybook a11y addon | Per component | 0 violations |

**Note:** Automated catches ~30% of issues. Gate 4 (Manual) catches the rest.

---

### G4: Build & Bundle Analysis

| Check | Tool | Threshold |
|-------|------|-----------|
| **Production Build** | `vite build --mode production` | Success |
| **JS Bundle (gz)** | Vite bundle analyzer | ≤ 170 KB |
| **CSS Bundle (gz)** | Vite bundle analyzer | ≤ 35 KB |
| **Fonts (gz)** | Network analysis | ≤ 50 KB |
| **Total Initial (gz)** | Vite + Network | ≤ 350 KB |
| **Requests** | Network analysis | < 40 |
| **Duplicate Dependencies** | `npm ls` / bundle analyzer | 0 |
| **Unused Exports** | `vite-plugin-inspect` | 0 |

---

### G5: Lighthouse CI

| Category | Threshold | URL Tested |
|----------|-----------|------------|
| **Performance** | ≥ 95 | Home, Product, Dashboard, Checkout |
| **Accessibility** | = 100 | All pages |
| **Best Practices** | ≥ 95 | All pages |
| **SEO** | ≥ 95 | Public pages |
| **PWA** | ≥ 90 | If applicable |

**Environment:** Mobile 4G throttling (RTT 150ms, 1.6 Mbps down)

**Budget:** LCP ≤ 2.5s, INP ≤ 200ms, CLS ≤ 0.1

---

## 3. Manual Review Gates (G6)

Executed after all automated gates pass.

| Sub-Gate | Name | Owner | Duration | Artifacts |
|----------|------|-------|----------|-----------|
| **G6.1** | UX Review | Principal UX Architect | 2 days | Wireframes, flows, IA mapping |
| **G6.2** | Visual Design | CDO + Design System Arch | 2 days | Hi-fi mockups (6 breakpoints) |
| **G6.3** | Implementation | Principal Frontend Architect | 2 days | PR, preview, bundle report |
| **G6.4** | Accessibility | Principal A11y Architect | 3 days | Axe report, keyboard video, SR log |
| **G6.5** | Performance | Principal Perf Engineer | 1 day | Lighthouse CI, bundle analyzer |

**All G6.x must pass.** Any failure returns to implementation.

---

### G6.1: UX Review Checklist

| Criterion | Pass If |
|-----------|---------|
| IA Compliance | Page maps to approved workspace; breadcrumbs correct |
| Task Efficiency | Primary task ≤ 3 clicks from workspace root |
| User Goals | All persona goals have visible path |
| Edge States | Empty, error, loading, permission identified |
| Future-Proof | New features fit IA without restructure |

---

### G6.2: Visual Design Review Checklist

| Criterion | Pass If |
|-----------|---------|
| Token Compliance | 0 hardcoded colors/spacing/typography |
| Brand Compliance | Logo, colors, typography, icons match guidelines |
| Visual Hierarchy | 1 primary action/view; weight = importance |
| Layout | 4px grid; max reading width 720px; CLS < 0.1 |
| Responsive | 6 breakpoints mocked; drawer/rail/table adapt |
| Dark Mode Ready | No hardcoded light values; semantic tokens |

---

### G6.3: Implementation Review Checklist

| Criterion | Pass If |
|-----------|---------|
| Token Compliance | `no-hardcoded-values` lint passes |
| Component Reuse | Existing components used; no duplicates |
| Type Safety | Strict TS; no `any`; proper component types |
| Architecture | Lazy routes; Suspense; code splitting correct |
| State Management | Server (TanStack Query) + Client (Context) correct |
| Bundle Size | Route chunk ≤ budget; no duplicate deps |
| Error Boundaries | Every route wrapped; graceful degradation |
| SEO / Meta | Title, description, OG, structured data |

---

### G6.4: Accessibility Review Checklist

| Criterion | Method | Pass Threshold |
|-----------|--------|----------------|
| Automated | axe-core (WCAG 2.2 AA) | 0 violations |
| Keyboard | Manual tab test | 100% reachable; focus visible |
| Screen Reader | NVDA + JAWS + VoiceOver | All content announced |
| Focus Mgmt | Route → `#main`; modal trap; restore | 100% |
| Contrast | Text 4.5:1; UI 3:1; focus 3:1 | 100% |
| Zoom 200% | No horizontal scroll; no loss; no overlap | 100% |
| Reduced Motion | `prefers-reduced-motion` respected | 100% |
| ARIA | Valid roles/states; no redundant ARIA | 100% |

---

### G6.5: Performance Review Checklist

| Metric | Target | Tool |
|--------|--------|------|
| LCP | ≤ 2.5s | Lighthouse (Mobile 4G) |
| INP | ≤ 200ms | Lighthouse + RUM |
| CLS | ≤ 0.1 | Lighthouse |
| TTFB | ≤ 800ms | Server timing |
| JS Bundle (gz) | ≤ 170 KB | Bundle analyzer |
| CSS Bundle (gz) | ≤ 35 KB | Bundle analyzer |
| Fonts (gz) | ≤ 50 KB | Network tab |
| Lighthouse Perf | ≥ 95 | Lighthouse CI |

---

## 4. Final Release Gate (G7)

| Check | Verification |
|-------|--------------|
| All Gates 1–5 Passed | Automated status checks |
| All Gates 6.1–6.5 Passed | Manual approvals recorded |
| Preview Live | URL accessible to stakeholders |
| Documentation Complete | Component docs, DDRs, approval log |
| Dependencies Resolved | No blocking tickets |
| Rollback Plan | Documented + tested |
| Stakeholder Sign-off | PM + CDO + Eng Lead |

---

## 5. CI/CD Pipeline Configuration

```yaml
# .github/workflows/quality-gates.yml
name: Quality Gates
on:
  pull_request:
    branches: [main, preview/*]
    types: [opened, synchronize, reopened]

jobs:
  g1-lint-typecheck:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with: { node-version: '20', cache: 'npm' }
      - run: npm ci
      - run: npm run lint
      - run: npm run typecheck
      - run: npm run lint:tokens

  g2-tests:
    runs-on: ubuntu-latest
    needs: g1-lint-typecheck
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with: { node-version: '20', cache: 'npm' }
      - run: npm ci
      - run: npm run test:coverage

  g3-a11y-auto:
    runs-on: ubuntu-latest
    needs: g1-lint-typecheck
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with: { node-version: '20', cache: 'npm' }
      - run: npm ci
      - run: npm run test:a11y

  g4-build-bundle:
    runs-on: ubuntu-latest
    needs: [g1-lint-typecheck, g2-tests, g3-a11y-auto]
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with: { node-version: '20', cache: 'npm' }
      - run: npm ci
      - run: npm run build
      - run: npm run bundle:analyze
      - uses: actions/upload-artifact@v4
        with:
          name: bundle-report
          path: dist/bundle-report.html

  g5-lighthouse:
    runs-on: ubuntu-latest
    needs: g4-build-bundle
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with: { node-version: '20', cache: 'npm' }
      - run: npm ci
      - run: npm run build
      - uses: treosh/lighthouse-ci-action@v11
        with:
          urls: |
            https://preview-${{ github.run_id }}.sporekart.dev/
            https://preview-${{ github.run_id }}.sporekart.dev/products
            https://preview-${{ github.run_id }}.sporekart.dev/dashboard
          budgetPath: .github/lighthouse/budget.json
          uploadArtifacts: true

  gate-status:
    runs-on: ubuntu-latest
    needs: [g1-lint-typecheck, g2-tests, g3-a11y-auto, g4-build-bundle, g5-lighthouse]
    if: always()
    steps:
      - name: Check all gates
        run: |
          for job in g1-lint-typecheck g2-tests g3-a11y-auto g4-build-bundle g5-lighthouse; do
            if [ "${{ needs.$job.result }}" != "success" ]; then
              echo "::error::Gate $job failed"
              exit 1
            fi
          done
          echo "All automated gates passed"
```

---

## 6. Branch Protection Rules

### `main` Branch
- Require PR review (2 approvals)
- Require status checks: **All 7 gates**
- Require linear history
- No force push
- No deletion

### `preview/*` Branches
- Require PR review (1 approval)
- Require status checks: G1–G5
- Auto-deploy on merge

### `hotfix/*` Branches
- Require PR review (1 approval)
- Require status checks: G1–G4 (G5 optional with CTO approval)
- Auto-deploy to staging

---

## 6. Metrics & Reporting

### Per-PR Dashboard
| Gate | Status | Duration | Details |
|------|--------|----------|---------|
| G1 Lint/Type | ✅/❌ | 45s | [Link] |
| G2 Tests | ✅/❌ | 2m | [Link] |
| G3 A11y Auto | ✅/❌ | 30s | [Link] |
| G4 Build/Bundle | ✅/❌ | 90s | [Link] |
| G5 Lighthouse | ✅/❌ | 2m | [Link] |
| G6.1 UX | ✅/⏳/❌ | 2d | [Log] |
| G6.2 Visual | ✅/⏳/❌ | 2d | [Log] |
| G6.3 Impl | ✅/⏳/❌ | 2d | [Link] |
| G6.4 A11y | ✅/❌ | 3d | [Log] |
| G6.5 Perf | ✅/❌ | 1d | [Link] |
| G6 Final | ✅/⏳/❌ | 0.5d | [Log] |

---

## 7. Failure Handling

| Gate | Failure Response |
|------|------------------|
| G1–G5 | PR blocked; fix required; re-run |
| G6.1–G6.5 | Return to implementation; re-review |
| **Any Gate** | No merge to `main` until all pass |

**Escalation:** Failed gate > SLA → auto-escalation per review framework.

---

## 6. Version

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | Sprint 19 Part 1E | Initial quality gates |

---

**Authority:** Principal Frontend Architect + Principal Performance Engineer  
**Review Cycle:** Quarterly (automated); Per sprint (manual)