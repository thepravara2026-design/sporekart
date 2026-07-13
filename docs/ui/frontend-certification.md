# Frontend Certification — SporeKart Enterprise Web Application

## 1. Purpose

Define the certification criteria that every frontend sprint must meet before code can be released to production. This is the **Definition of Done** for frontend work.

---

## 2. Certification Levels

| Level | Scope | Required For |
|-------|-------|--------------|
| **Component** | Individual component | Every component in `@sporekart/ui` |
| **Page** | Complete page/route | Every page in the application |
| **Sprint** | All work in sprint | Sprint release |
| **Release** | Full release candidate | Production deployment |

---

## 3. Component Certification Checklist

Every component in `@sporekart/ui` must pass **all** checks before publication.

### 3.1 Code Quality

| Check | Tool | Threshold |
|-------|------|-----------|
| TypeScript | `tsc --noEmit --strict` | 0 errors |
| ESLint | `eslint` + custom rules | 0 errors / 0 warnings |
| Token Compliance | Custom Stylelint rule | 0 hardcoded values |
| Props Interface | Exported + JSDoc | 100% documented |
| Default Props | All non-required have defaults | 100% |

### 3.2 Testing

| Test Type | Tool | Coverage Threshold |
|-----------|------|-------------------|
| Unit | Vitest | ≥ 80% lines / branches / functions |
| A11y Automated | axe-core (JSDOM) | 0 violations |
| Visual Regression | Chromatic | 0 pixel diffs (all variants) |
| Interaction | Testing Library | All user flows covered |

### 3.3 Accessibility (Mandatory)

| Check | Method | Pass Criteria |
|-------|--------|---------------|
| axe-core (auto) | Vitest + axe-core | 0 violations (AA) |
| Keyboard Navigation | Manual test script | All interactions reachable; focus visible |
| Screen Reader (NVDA) | Manual test | Name + Role + State announced |
| Screen Reader (VoiceOver) | Manual test | Name + Role + State announced |
| Focus Management | Manual + automated | Logical order; trapped in modals |
| Color Contrast | CCA + axe | 4.5:1 text; 3:1 UI; focus ring 3:1 |
| Reduced Motion | CSS + manual | Animations disabled; static skeletons |
| Zoom 200% | Browser zoom | No horizontal scroll; no overlap |

### 3.4 Visual & UX

| Check | Method | Pass Criteria |
|-------|--------|---------------|
| Visual Regression | Chromatic | 0 pixel diffs (all variants, all breakpoints) |
| Design Token Usage | Token audit | 100% semantic tokens; 0 hardcoded |
| Responsive | Browser dev tools | All 6 breakpoints render correctly |
| Dark Mode | Theme toggle | All tokens resolve correctly |
| High Contrast | `forced-colors: active` | System colors used; no custom colors |

### 3.5 Performance

| Metric | Budget | Measurement |
|--------|--------|-------------|
| Component JS (gz) | ≤ 5 KB | Vite bundle analyzer |
| Component CSS (gz) | ≤ 2 KB | Vite bundle analyzer |
| Render Time | ≤ 16ms | React Profiler (dev) |
| Re-renders | Minimal | React Profiler (why-did-you-render) |

### 3.6 Documentation

| Artifact | Required |
|----------|----------|
| Props Interface (JSDoc) | ✅ |
| Storybook Stories (all variants) | ✅ |
| Usage Examples | ✅ |
| A11y Notes | ✅ |
| Migration Notes (if changed) | ✅ |

### 3.7 Component Certification Record

`/docs/ui/certification/components/[ComponentName].md`

```markdown
# Component Certification: [ComponentName]

**Version:** [X.Y.Z]
**Date:** [YYYY-MM-DD]
**Design System Version:** [X.Y.Z]

## Code Quality
- TypeScript: ✅ 0 errors
- ESLint: ✅ 0 errors / 0 warnings
- Token Compliance: ✅ 0 hardcoded values
- Props Interface: ✅ Documented

## Testing
- Unit Coverage: [X]% (≥80%)
- A11y Auto: ✅ 0 violations
- Visual Regression: ✅ 0 diffs
- Interaction Tests: ✅

## Accessibility
| Test | Tester | Date | Status |
|------|--------|------|--------|
| Keyboard | [Name] | YYYY-MM-DD | ✅ |
| NVDA | [Name] | YYYY-MM-DD | ✅ |
| VoiceOver | [Name] | YYYY-MM-DD | ✅ |
| Contrast | [Name] | YYYY-MM-DD | ✅ |
| Reduced Motion | [Name] | YYYY-MM-DD | ✅ |

## Visual
- Chromatic: ✅ 0 diffs
- Responsive: ✅ 6 breakpoints
- Dark Mode: ✅
- High Contrast: ✅

## Performance
- JS Bundle: [X] KB (≤5 KB)
- CSS Bundle: [X] KB (≤2 KB)
- Render: [X] ms (≤16ms)

## Documentation
- Props Doc: ✅
- Stories: ✅
- Examples: ✅
- A11y Notes: ✅

## Sign-off
**Component Owner:** [Name] — ✅
**Principal DS Architect:** [Name] — ✅
**Principal A11y Architect:** [Name] — ✅
**Date:** YYYY-MM-DD
```

---

## 4. Page Certification Checklist

Every page/route must pass all checks before merge to `main`.

### 4.1 Pre-Implementation (Design Phase)

| Artifact | Required |
|----------|----------|
| Design Brief | ✅ |
| Wireframes (all 6 breakpoints) | ✅ |
| High-Fidelity Mockups (all 6 breakpoints) | ✅ |
| Token Usage Report | ✅ |
| Component Inventory | ✅ |
| Accessibility Annotation | ✅ |

### 4.2 Implementation Phase

| Check | Tool | Threshold |
|-------|------|-----------|
| TypeScript | `tsc` | 0 errors |
| Lint | ESLint | 0 errors |
| Unit Tests | Vitest | ≥ 80% coverage |
| A11y Auto | axe-core | 0 violations |
| Build | Vite | Success |
| Bundle Size | Vite | Within budget |

### 4.3 Post-Deploy (Preview Environment)

| Check | Tool | Threshold |
|-------|------|-----------|
| Lighthouse Performance | Lighthouse CI | ≥ 95 |
| Lighthouse Accessibility | Lighthouse CI | 100 |
| Lighthouse Best Practices | Lighthouse CI | ≥ 90 |
| Lighthouse SEO | Lighthouse CI | ≥ 90 |
| axe-core (live) | Playwright + axe | 0 violations |
| Keyboard Navigation | Manual | All tasks completable |
| NVDA | Manual | All content announced |
| VoiceOver | Manual | All content announced |
| Zoom 200% | Browser | No horizontal scroll |
| Reduced Motion | OS setting | Animations disabled |

### 4.4 Gate Approvals Required

| Gate | Approver | Status |
|------|----------|--------|
| 1: UX | Principal UX Architect | ✅ |
| 2: Visual | CDO + Principal DS Architect | ✅ |
| 3: Implementation | Principal Frontend Architect | ✅ |
| 4: Accessibility | Principal A11y Architect | ✅ |
| 5: Performance | Principal Performance Engineer | ✅ |
| 6: Final | Release Manager | ✅ |

### 4.4 Page Certification Record

`/docs/ui/certification/pages/[route].md`

```markdown
# Page Certification: [Page Name] ([route])

**Sprint:** [Number]
**Route:** [/path/:param]
**Design System Version:** [X.Y.Z]

## Gate Approvals
| Gate | Reviewer | Role | Status | Date | Comments |
|------|----------|------|--------|------|----------|
| 1 UX | | Principal UX Architect | | | |
| 2 Visual | | CDO / Principal DS Arch | | | |
| 3 Impl | | Principal Frontend Arch | | | |
| 4 A11y | | Principal A11y Arch | | | |
| 5 Perf | | Principal Perf Eng | | | |
| 6 Final | | Release Manager | | | |

## Automated Checks
| Check | Tool | Result |
|-------|------|--------|
| TypeScript | tsc | ✅ |
| Lint | ESLint | ✅ |
| Tests | Vitest | ✅ [X]% |
| A11y Auto | axe-core | ✅ 0 violations |
| Build | Vite | ✅ |
| Bundle | Analyzer | ✅ within budget |
| Lighthouse Perf | CI | ✅ ≥95 |
| Lighthouse A11y | CI | ✅ 100 |

## Manual Testing
| Test | Tester | Date | Status |
|------|--------|------|--------|
| Keyboard | | | ✅ |
| NVDA | | | ✅ |
| VoiceOver | | | ✅ |
| Zoom 200% | | | ✅ |
| Reduced Motion | | | ✅ |

## Performance (RUM / Lighthouse)
| Metric | Value | Target |
|--------|-------|--------|
| LCP | [X]s | ≤ 2.5s |
| INP | [X]ms | ≤ 200ms |
| CLS | [X] | ≤ 0.1 |

## Final Status
**Overall:** ✅ APPROVED / ❌ BLOCKED
**Version:** [Design System Version]
**Preview URL:** [URL]
```

---

## 5. Sprint Certification

A sprint is **certified** when:

| Requirement | Status |
|-------------|--------|
| All new/modified components certified | ✅ |
| All new/modified pages certified | ✅ |
| All 6 gates passed for all work | ✅ |
| Zero accessibility regressions | ✅ |
| Zero performance regressions | ✅ |
| Documentation updated (components, pages, DDRs, migration guides) | ✅ |
| Design system version bumped (if applicable) | ✅ |
| Release notes drafted | ✅ |
| Stakeholder demo completed | ✅ |

### 5.1 Sprint Certification Record

`/docs/reviews/sprint-XX.md`

```markdown
# Sprint XX Certification

## Goal
[Sprint goal]

## Completed Work
| Item | Type | Gates Passed |
|------|------|--------------|
| [Item] | [Component/Page/Pattern] | 1-6 |

## Gate Summary
| Gate | Items Reviewed | Passed | Blocked | Avg Duration |
|------|----------------|--------|---------|--------------|
| 1 UX | [N] | [N] | [N] | [Days] |
| 2 Visual | [N] | [N] | [N] | [Days] |
| 3 Impl | [N] | [N] | [N] | [Days] |
| 4 A11y | [N] | [N] | [N] | [Days] |
| 5 Perf | [N] | [N] | [N] | [Days] |
| 6 Final | [N] | [N] | [N] | [Days] |

## Metrics
| Metric | Value | Target |
|--------|-------|--------|
| Gate 1 Pass Rate (1st try) | [%] | ≥ 80% |
| Avg Gate Duration | [Days] | ≤ SLA |
| Re-review Rate | [%] | ≤ 20% |
| A11y Regressions | [Count] | 0 |
| Perf Regressions | [Count] | 0 |

## Retrospective
### Worked Well
- [Item]

### Needs Improvement
- [Item]

## Action Items
| Action | Owner | Due |
|--------|-------|-----|

## Certification
**Sprint Certified:** ✅ YES / ❌ NO
**Design System Version:** [X.Y.Z]
**Next Sprint Goal:** [Goal]
```

---

## 5. Release Certification

A release candidate is **certified for production** when:

| Requirement | Verification |
|-------------|--------------|
| Sprint certified | ✅ |
| All hotfixes certified | ✅ |
| Rollback tested | ✅ |
| Monitoring alerts configured | ✅ |
| Rollback plan documented | ✅ |
| Stakeholder sign-off | ✅ |

### 5.1 Release Certification Record

`/docs/releases/vX.Y.Z.md`

```markdown
# Release v[X.Y.Z] Certification

## Components
| Component | Version | Status |
|-----------|---------|--------|
| @sporekart/tokens | [X.Y.Z] | ✅ |
| @sporekart/ui | [X.Y.Z] | ✅ |
| @sporekart/icons | [X.Y.Z] | ✅ |

## Sprint Certification
| Sprint | Certified | DS Version |
|--------|-----------|------------|
| [XX] | ✅ | [X.Y.Z] |

## Quality Gates
| Gate | Status |
|------|--------|
| All Gates 1-6 Passed | ✅ |
| Zero A11y Regressions | ✅ |
| Zero Perf Regressions | ✅ |
| Zero Critical Bugs | ✅ |

## Release Plan
- **Date:** [Date]
- **Rollout:** [Canary / Full]
- **Rollback Plan:** [Link]
- **Monitoring:** [Dashboard link]

## Approvals
| Role | Name | Status | Date |
|------|------|--------|------|
| Release Manager | | ✅ | |
| CDO | | ✅ | |
| CPO | | ✅ | |
| CTO | | ✅ | |
```

---

## 6. Certification Automation

### 6.1 CI Pipeline Gates

```yaml
# .github/workflows/certification.yml
jobs:
  component-cert:
    runs-on: ubuntu-latest
    steps:
      - checkout
      - setup-node
      - run: npm ci
      - run: npm run typecheck
      - run: npm run lint
      - run: npm run test:unit -- --coverage
      - run: npm run test:a11y
      - run: npm run test:visual
      - run: npm run bundle:analyze
      - run: npm run doc:generate

  page-cert:
    needs: component-cert
    runs-on: ubuntu-latest
    steps:
      - checkout
      - deploy-preview
      - run: lighthouse-ci
      - run: playwright-a11y
      - run: playwright-keyboard
      - run: playwright-zoom

  sprint-cert:
    needs: [component-cert, page-cert]
    if: github.ref == 'refs/heads/main'
    runs-on: ubuntu-latest
    steps:
      - gate-1-ux-approval
      - gate-2-visual-approval
      - gate-3-impl-approval
      - gate-4-a11y-approval
      - gate-5-perf-approval
      - gate-6-final-approval
```

---

## 7. Certification Failure Handling

| Failure Type | Response |
|--------------|----------|
| **Automated (CI)** | PR blocked; fix required; re-run |
| **Gate 1-3 (Design/Impl)** | Return to implementation; re-review |
| **Gate 4 (A11y)** | **Immediate block**; P0 fix; no merge until 0 violations |
| **Gate 5 (Perf)** | Investigate; optimize or scope reduction; re-review |
| **Gate 6 (Final)** | Release blocked; all prior gates re-verified |

**No exceptions.** No "ship with known issues" for certification gates.

---

## 8. Recertification

| Trigger | Recertification Scope |
|---------|----------------------|
| Design System Major Version | All components + pages |
| Design System Minor Version | Affected components + pages |
| Browser Engine Update | Smoke test (critical paths) |
| Accessibility Standard Update | Full A11y recertification |
| Performance Budget Change | Affected pages + components |

---

## 7. Version History

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | Sprint 19 Part 1E | Enterprise Quality Team | Initial certification framework |

---

**Authority:** Principal Frontend Architect + Principal Accessibility Architect + Principal Performance Engineer  
**Review Cycle:** Per sprint (sprint certification); Per release (release certification)  
**Effective:** Sprint 19 Part 1E certification