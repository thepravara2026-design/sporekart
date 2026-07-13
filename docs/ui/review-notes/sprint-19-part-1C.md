# Sprint 19 Part 1C Review Notes
## Phase 5 / Sprint 19 (Part 1C): UX Standards & Accessibility Foundation

> **Status:** Reviewer guide. Sprint stops for review after prototype extension.

---

## 1. What to Review

### Documentation (11 files)
| File | Review Focus |
|------|--------------|
| `ux-standards.md` | 11 principles actionable? Conflict resolution clear? Screen checklist complete? |
| `responsive-strategy.md` | Breakpoints correct? Table/card/form adaptations right? Touch targets 48px? |
| `accessibility-standards.md` | WCAG 2.2 AA complete? ARIA patterns correct? Focus mgmt covers all cases? |
| `keyboard-navigation.md` | Shortcuts conflict-free? Component patterns complete? Focus restore verified? |
| `loading-experience.md` | Skeleton specs match final layouts? No "Loading..." text? Offline covered? |
| `error-handling-guidelines.md` | Every HTTP code covered? Recovery paths clear? Microcopy tone right? |
| `empty-state-guidelines.md` | All 30+ states covered? Explain-Guide-Act formula followed? Variants defined? |
| `form-experience.md` | Validation timing right? Auto-save/draft recovery specified? OTP/address/checkout patterns complete? |
| `microcopy-guidelines.md` | Voice consistent? Terminology locked? Forbidden patterns enforced? |
| `performance-budgets.md` | CWV targets realistic? Budgets enforceable in CI? Monitoring defined? |
| `accessibility-guidelines.md` | Quick-reference accurate? CI gates correct? |

### Prototype Extensions (`frontend/web-app/`)
| Route | Test |
|-------|------|
| `/demo/responsive` | Viewport toggle works; drawer/rail/table-flip visible |
| `/demo/keyboard` | Focus order visualizer; component patterns work; shortcuts active |
| `/demo/loading` | All skeleton variants; action loaders; offline banner; retry flows |
| `/demo/errors` | 404, 403, 401, 500, network, timeout, validation, conflict, rate-limit |
| `/demo/empty` | 30+ states searchable; variants (first-time, filtered, permission, error) |
| `/demo/forms` | All field types; async validation; OTP; address; checkout steps; auto-save |
| `/demo/microcopy` | All patterns rendered; copy-paste ready; tone consistent |
| `/` + nav | Role switcher still works; command palette `Cmd+K`; breadcrumbs |

---

## 2. Open Questions for Part 1D

1. **Design Tokens Priority:** Which token set first — spacing/sizing, color, typography, or all together?
2. **Component Freeze Scope:** Freeze all primitives (Button, Input, Select, Table, Card, Modal, Toast, Tooltip, Avatar, Badge, Tabs, Accordion) in Part 1D, or stagger?
3. **Dark Mode:** Deferred to Part 1E? Confirm.
4. **Animation Library:** CSS-only or Framer Motion? (Impacts bundle)
5. **Icon Set strict CSP:** Hash-based `script-src` for inline styles? Need nonce strategy for Vite.
7. **Date Picker:** Build custom (ARIA calendar) or use `react-day-picker` (bundle + a11y work)?
8. **Combobox:** Custom (full ARIA) or `downshift`/`react-aria`? Bundle vs control.
9. **Table Virtualization:** `react-virtual` / `tanstack-virtual`? Required for 200+ rows.
10. **Form Library:** `react-hook-form` + `zod` (standard) — confirm.
11. **Toast System:** Custom (portal + stack) or `react-hot-toast` / `sonner`?
12. **Error Boundary UI:** Global error boundary with "Something went wrong" + retry — design now or later?
13. **Offline Indicator:** Header badge vs banner — finalize placement.
14. **Analytics Consent:** Cookie banner / consent management — Part 1D or later?
15. **RTL Readiness:** Logical properties only? Confirm no `left/right` in prototype CSS.

---

## 3. Known Limitations (This Sprint)

- No design tokens — prototype uses CSS custom properties directly
- No component library — demo components are inline/one-off
- No real auth — role switcher is review-only
- No API — all data mocked, mutations simulated
- No i18n — English only; microcopy gallery placeholder for HI
- No bundle analysis in CI — needs `vite-bundle-analyzer` config
- No E2E tests — Playwright config needed for Part 1D

---

## 4. Review Gate

| Reviewer | Role | Decision | Date |
|----------|------|----------|------|
| __________ | Chief Experience Officer | ☐ Approve ☐ Changes | ______ |
| __________ | Principal Accessibility Architect | ☐ Approve ☐ Changes | ______ |
| __________ | Principal Frontend Architect | ☐ Approve ☐ Changes | ______ |
| __________ | Principal Product Designer | ☐ Approve ☐ Changes | ______ |

**All 4 must Approve** before Part 1D starts.

---

## 5. Next Steps (Part 1D)

Upon approval:
1. Create design token system (spacing, color, typography, radius, shadow, motion)
2. Build frozen component library (Storybook + tests + a11y)
3. Migrate prototype to use token + component library
4. Add CI: bundle budget, a11y, visual regression (Chromatic)
5. Document component API + usage guidelines