# Release Observations — RC1 Certification

**Date:** 2026-07-20

---

## Classification

Per release mandate, observations may ONLY include:
- ✅ Documentation improvements
- ✅ CI improvements
- ✅ Future enhancements
- ✅ Technical debt

No production defects are permitted in observations.

---

## 1. Documentation Improvements

| # | Observation | Priority |
|---|-------------|----------|
| DOC-01 | Consolidate all prior QA/bug-fix/regression reports into a single release evidence repository with cross-references | Medium |
| DOC-02 | Add architecture decision records (ADRs) for auth provider choice, payment gateway selection, and state management | Low |
| DOC-03 | Document the release rollback procedure (see rollback-checklist.md) | High |
| DOC-04 | Add API documentation for the 17 microservices referenced in .env.mock | Medium |
| DOC-05 | Generate OpenAPI/Swagger specs for all backend services | Medium |
| DOC-06 | Add a production readiness checklist to docs/ that auditors can verify independently | High |

## 2. CI Improvements

| # | Observation | Priority |
|---|-------------|----------|
| CI-01 | Add frontend build to `build.yml` (currently only builds identity-service) | High |
| CI-02 | Add Lighthouse CI to the build workflow for performance regression detection | Medium |
| CI-03 | Add bundle-size checks (e.g., `bundlesize` or `size-limit`) to prevent chunk bloat | Medium |
| CI-04 | Add TypeScript strict-mode check as a separate CI job | Medium |
| CI-05 | Add dependency vulnerability scanning (Dependabot or `npm audit` in CI) | High |
| CI-06 | Add secret scanning (e.g., GitGuardian or `truffleHog`) to prevent credential leaks | High |
| CI-07 | Configure Playwright to run on every PR, not just release branches | Medium |
| CI-08 | Add test result flakiness detection and alerting | Low |

## 3. Future Enhancements

| # | Observation | Priority |
|---|-------------|----------|
| ENH-01 | Implement product detail pages (`/products/:id`) — currently the only missing customer-facing feature of significance | High |
| ENH-02 | Add real-time notification system (WebSocket/SSE) for order and training events | Medium |
| ENH-03 | Implement PWA offline support with service worker caching strategies | Low |
| ENH-04 | Add internationalization (i18n) framework for multi-language support | Low |
| ENH-05 | Implement dark mode with CSS custom property toggling | Low |
| ENH-06 | Add comprehensive unit test suite (Jest + React Testing Library) | High |
| ENH-07 | Add end-to-end encryption for sensitive user data | Medium |

## 4. Technical Debt

| # | Observation | Priority |
|---|-------------|----------|
| TD-01 | Enable TypeScript strict mode (`strict: true` in tsconfig.json) and resolve all resulting errors | High |
| TD-02 | Replace inline CSS styles in ErrorBoundary.tsx with design system tokens | Low |
| TD-03 | Centralize all mock data into a single mock-data service with TypeScript type safety | Medium |
| TD-04 | Standardize API error response shapes across all service mocks | Low |
| TD-05 | Remove duplicate route definitions — App.tsx has both direct `<Route>` elements and `getAllPages()`-driven routes | Medium |
| TD-06 | Consolidate the 200+ lazy imports in App.tsx into organized route-group files | Low |
| TD-07 | Remove "Navigation Prototype" branding strings from all production-facing locations | High |
| TD-08 | Add `prettier-plugin-organize-imports` to keep import statements sorted | Low |

## 5. Process Observations

| # | Observation | Priority |
|---|-------------|----------|
| PROC-01 | The release audit process should define "production-ready" criteria earlier in the development lifecycle | High |
| PROC-02 | Each sprint should include a "production hardening" checklist item beyond feature work | Medium |
| PROC-03 | Bug registers should cross-reference the current commit hash so they cannot become stale | Medium |
| PROC-04 | Consider adopting trunk-based development with short-lived feature branches instead of long-running sprint branches | Low |
