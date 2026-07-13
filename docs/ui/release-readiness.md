# Release Readiness — SporeKart Enterprise Web Application

## 1. Release Criteria

Every release to production must satisfy **all** criteria below.

---

## 2. Mandatory Gates (All Must Pass)

| Gate | Criteria | Verification |
|------|----------|--------------|
| **G1: Code Quality** | TypeScript strict: 0 errors; ESLint: 0 errors; Tests: ≥80% coverage | CI Pipeline |
| **G2: Design Review** | All 6 gates passed for all changed pages/components | Approval Log |
| **G3: Accessibility** | WCAG 2.2 AA certified; 0 axe violations; keyboard/screen reader tested | A11y Cert |
| **G4: Performance** | LCP ≤2.5s, INP ≤200ms, CLS ≤0.1; Bundle ≤ budgets | Lighthouse CI + Bundle Analyzer |
| **G5: Security** | 0 high/critical vulns; CSP headers; no secrets in bundle | Snyk + CSP Check |
| **G6: Compatibility** | Chrome/FF/Safari/Edge (last 2); iOS/Android (last 2) | BrowserStack |
| **G7: Monitoring** | Sentry + RUM + Lighthouse CI alerts configured | Dashboard |
| **G8: Rollback** | Rollback tested in staging; <5 min RTO | Runbook |

---

## 3. Pre-Release Checklist

### 3.1 Code & Build

- [ ] `npm run typecheck` — 0 errors
- [ ] `npm run lint` — 0 errors
- [ ] `npm run test` — ≥80% coverage; 0 failures
- [ ] `npm run build` — Production build succeeds
- [ ] `npm run bundle:analyze` — All route chunks within budget
- [ ] `npm run tokens:build` — Tokens published; types generated

### 3.2 Design & UX

- [ ] All changed pages/components have approved Design Brief
- [ ] Gate 1 (UX) passed for all changes
- [ ] Gate 2 (Visual) passed for all changes
- [ ] Gate 3 (Implementation) passed for all changes
- [ ] Gate 4 (A11y) passed for all changes
- [ ] Gate 5 (Performance) passed for all changes
- [ ] Gate 6 (Final) signed by Release Manager
- [ ] Design system version bumped per semver
- [ ] Figma files updated; tokens synced

### 3.3 Accessibility

- [ ] Automated axe: 0 violations (AA)
- [ ] Keyboard navigation: 100% tasks completable
- [ ] NVDA + Chrome: All components tested
- [ ] VoiceOver + Safari: All components tested
- [ ] Focus management: Route changes, modals, drawers
- [ ] Color contrast: 4.5:1 text; 3:1 UI
- [ ] Zoom 200%: No horizontal scroll; no overlap
- [ ] Reduced motion: All animations disabled
- [ ] ARIA: Valid roles, properties, states

### 3.4 Performance

- [ ] Lighthouse CI: Performance ≥95, A11y 100, BP ≥95, SEO ≥90
- [ ] Core Web Vitals (Mobile 4G): LCP ≤2.5s, INP ≤200ms, CLS ≤0.1
- [ ] Bundle sizes: JS ≤170KB gz; CSS ≤35KB gz; Fonts ≤50KB gz
- [ ] Route chunks: Within per-route budget
- [ ] Font loading: `font-display: swap`; preload critical
- [ ] Images: WebP/AVIF; blurhash; lazy load; proper sizes
- [ ] Caching: Headers correct; SW precache works

### 3.5 Security

- [ ] `npm audit` — 0 high/critical
- [ ] Snyk scan — 0 high/critical
- [ ] CSP headers: `script-src 'self' 'nonce-...'`
- [ ] No secrets in bundle (checked via `trufflehog`)
- [ ] Dependencies pinned; no floating versions
- [ ] CSP report-only tested in staging

### 3.6 Compatibility

- [ ] Chrome (latest 2) — All features work
- [ ] Firefox (latest 2) — All features work
- [ ] Safari (latest 2) — All features work
- [ ] Edge (latest 2) — All features work
- [ ] iOS Safari (latest 2) — Touch, gestures, PWA
- [ ] Android Chrome (latest 2) — Touch, gestures, PWA
- [ ] RTL: Hebrew/Arabic layouts (if applicable)
- [ ] Zoom 200%: No horizontal scroll; content readable

### 3.7 Monitoring & Observability

- [ ] Sentry: Error tracking + performance
- [ ] RUM: web-vitals (LCP, INP, CLS, TTFB)
- [ ] Lighthouse CI: Alerts on regression
- [ ] Bundle size: Alert on >10% increase
- [ ] API latency: p99 < 1.5s alert
- [ ] Error rate: >0.1% alert
- [ ] Uptime: 99.9% SLA

### 3.8 Rollback & Recovery

- [ ] Rollback tested in staging (<5 min)
- [ ] Database migrations: Reversible; tested
- [ ] Feature flags: Kill switches for new features
- [ ] Database backup: Verified <24h old
- [ ] Runbook: Updated; accessible; tested

### 3.8 Documentation

- [ ] Changelog updated (conventional commits)
- [ ] Migration guide (if breaking)
- [ ] Component docs updated
- [ ] API docs updated (if API changed)
- [ ] Runbook updated
- [ ] Architecture decision records updated

### 3.9 Stakeholder Sign-off

| Role | Name | Approved | Date |
|------|------|----------|------|
| Product Owner | | ☐ | |
| Engineering Lead | | ☐ | |
| QA Lead | | ☐ | |
| Security | | ☐ | |
| Release Manager | | ☐ | |

---

## 4. Release Process

### 4.1 Release Branching

```
main
  └── release/vX.Y.Z  ← Release branch cut from main
        └── hotfix/*  ← Only critical fixes
```

### 4.2 Release Steps

1. **Cut Release Branch**
   ```bash
   git checkout main
   git pull
   git checkout -b release/v1.2.0
   npm version minor  # or patch/major
   git push origin release/v1.2.0
   ```

2. **CI/CD Pipeline Runs**
   - All quality gates
   - Deploys to **staging** environment

3. **Staging Validation**
   - Smoke tests pass
   - Stakeholder review on staging
   - Performance baseline captured

3. **Production Deploy**
   - Merge `release/v1.2.0` → `main` (fast-forward)
   - Tag: `git tag v1.2.0`
   - CI/CD deploys to production
   - Post-deploy smoke tests

4. **Post-Deploy**
   - Monitor RUM dashboards (30 min)
   - Verify error rate < baseline
   - Verify performance at baseline
   - Notify stakeholders

### 4.3 Hotfix Process

1. `git checkout main`
2. `git checkout -b hotfix/v1.2.1`
3. Fix + test
4. `npm version patch`
5. `git push origin hotfix/v1.2.1`
6. CI runs → deploys staging → production
7. Backport to `release/v1.2.0` if exists

---

## 5. Release Communication

### 5.1 Internal

| Channel | Audience | Timing |
|---------|----------|--------|
| Slack `#releases` | All engineering | At deploy |
| Email | Stakeholders (PM, Design, Support) | At deploy |
| Changelog | Public `/changelog` | At deploy |

### 5.2 Release Notes Template

```markdown
# v1.2.0 — [Release Name]

## Highlights
- [Major feature/improvement]

## New Features
- [Feature] — [Description]

## Improvements
- [Improvement] — [Impact]

## Bug Fixes
- [Fix] — [Ticket]

## Breaking Changes
- [Change] — [Migration Guide Link]

## Performance
- LCP improved by X%
- Bundle reduced by Y KB

## Accessibility
- [A11y improvement]

## Security
- [Security fix/upgrade]

## Migration Guide
[vX.Y.Z Migration Guide](link)

## Full Changelog
[GitHub Compare](link)
```

### 5.3 External (if user-facing)

- In-app banner / toast
- Email to users (if major)
- Help center article updated

---

## 6. Post-Release Monitoring

| Timeframe | Action | Owner |
|-----------|--------|-------|
| **0–30 min** | Monitor error rate, latency, CWV | Release Manager |
| **30 min–2 hr** | Verify feature adoption; check logs | Engineering Lead |
| **2–24 hr** | Review error trends; performance trends | Engineering Lead |
| **24–72 hr** | Post-release retrospective | Scrum Master |
| **1 week** | Full metrics review; update baselines | Performance Engineer |

### 6.1 Rollback Triggers

| Condition | Action |
|-----------|--------|
| Error rate > 2x baseline (5 min) | Immediate rollback |
| LCP > 4s (p75, 10 min) | Rollback + investigate |
| Critical security vuln | Immediate rollback + patch |
| Data corruption | Immediate rollback + DB restore |

---

## 7. Version History

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | Sprint 19 Part 1E | Enterprise Release Engineering | Initial release readiness |

---

**Authority:** Enterprise Release Manager  
**Review Cycle:** Per release  
**Effective:** Sprint 19 Part 1E certification