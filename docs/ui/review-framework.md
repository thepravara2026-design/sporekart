# Review Framework — SporeKart Enterprise Web Application

## 1. Overview

Every page, component, and workflow must pass through **6 mandatory review gates** before production. No gate may be skipped. Each gate has explicit criteria, required artifacts, and designated approvers.

---

## 2. The Six Gates

```
Business Requirement
         ↓
    Gate 1: UX Review
         ↓
    Gate 2: Visual Design Review
         ↓
    Gate 3: Implementation Review
         ↓
    Gate 4: Accessibility Review
         ↓
    Gate 5: Performance Review
         ↓
    Gate 6: Final Approval
         ↓
   Production Ready
```

---

## 3. Gate Details

### Gate 1: UX Review (Principal UX Architect)

**When:** Wireframes / low-fidelity complete  
**Duration:** 1–2 business days  
**Blocking:** No visual design or implementation starts without Gate 1 pass

| Criterion | Verification Method | Pass Threshold |
|-----------|---------------------|----------------|
| **IA Compliance** | Page maps to approved workspace; breadcrumb strategy correct | 100% |
| **Task Flow Efficiency** | Primary task ≤ 3 clicks from workspace root | ≤ 3 clicks |
| **Persona Coverage** | Every target persona has a clear path to their goals | 100% personas |
| **Error/Empty States** | All states identified: empty, error, loading, permission | 100% states |
| **Future-Proofing** | New features slot into IA without restructure | Yes |
| **Journey Alignment** | Maps to approved user journeys (Part 1A) | 100% journeys |

**Artifacts Required:**
- Wireframes (Figma frames at desktop + mobile)
- Task flow diagrams
- IA mapping spreadsheet
- Persona-goal mapping

**Output:** ✅ Approved / 🔄 Changes Required (specific, actionable)

---

### Gate 2: Visual Design Review (CDO + Principal Design System Architect)

**When:** High-fidelity mockups complete at all 6 breakpoints  
**Duration:** 1–2 business days  
**Blocking:** No implementation starts without Gate 2 pass

| Criterion | Verification Method | Pass Threshold |
|-----------|---------------------|----------------|
| **Token Usage** | Figma → Token mapping audit (plugin) | 100% semantic tokens |
| **Brand Compliance** | Logo, color, typography, iconography audit | 100% |
| **Visual Hierarchy** | Primary action visually dominant; weight = importance | 1 primary / view |
| **Layout Compliance** | 4px grid; max reading width 720px; no CLS | 100% |
| **Responsive** | 6 breakpoint frames + adaptation specs | All 6 |
| **Dark Mode Ready** | Token structure supports dark (no hardcoded light values) | Token audit |
| **Animation** | Duration 150–300ms; ease-out; respects reduced-motion | 100% |
| **Illustrations** | Match illustration guidelines (style, accent, size) | 100% |

**Artifacts Required:**
- Figma file: High-fidelity frames at xs, sm, md, lg, xl, 2xl
- Token usage report (auto-generated)
- Animation specs (duration, easing, triggers)
- Illustration specifications

**Output:** ✅ Approved / 🔄 Changes Required

---

### Gate 3: Implementation Review (Principal Frontend Architect)

**When:** Code complete, preview deployed, PR open  
**Duration:** 1–2 business days  
**Blocking:** No merge to main without Gate 3 pass

| Criterion | Verification Method | Pass Threshold |
|-----------|---------------------|----------------|
| **Token Compliance** | `stylelint` custom rule: no hardcoded values | 0 violations |
| **Component Reuse** | Code search for duplicate components | 0 duplicates |
| **Type Safety** | `tsc --noEmit --strict` | 0 errors; no `any` |
| **Architecture** | Lazy routes, Suspense boundaries, code splitting | Per route budget |
| **State Management** | Server state (TanStack Query), client state (Context/Zustand) | Consistent |
| **Bundle Size** | Vite bundle analyzer per route | ≤ budget per route |
| **Error Boundaries** | Every route wrapped; graceful degradation | 100% routes |
| **SEO/Meta/ | Title, description, OG, structured data | 100% pages |

**Artifacts Required:**
- GitHub PR with preview URL
- Bundle analyzer output (HTML)
- TypeScript strict check log
- Lighthouse CI report (performance only)

**Output:** ✅ Approved / 🔄 Changes Required

---

### Gate 4: Accessibility Review (Principal Accessibility Architect)

**When:** Preview deployed, implementation complete  
**Duration:** 2–3 business days  
**Blocking:** Zero tolerance for AA failures; no production without Gate 4 pass

| Criterion | Verification Method | Pass Threshold |
|-----------|---------------------|----------------|
| **Automated** | axe-core (WCAG 2.2 AA) | 0 violations |
| **Keyboard** | Manual tab test; all interactive reachable; focus visible | 100% |
| **Screen Reader** | NVDA (Win) + JAWS (Win) + VoiceOver (Mac/iOS) | All content announced |
| **Focus Management** | Route change → `#main`; modal trap; restore on close | 100% |
| **Color Contrast** | Text 4.5:1; UI 3:1; focus ring 3:1 | 100% |
| **Zoom 200%** | No horizontal scroll; no content loss; no overlap | 100% |
| **Reduced Motion** | `prefers-reduced-motion`: animations disabled | 100% |
| **ARIA** | Valid roles, states, properties; no redundant ARIA | 100% |

**Artifacts Required:**
- axe-core HTML report
- Keyboard test video (5 min max)
- Screen reader test log (NVDA + VoiceOver)
- Zoom 200% screenshots (desktop + mobile)
- Reduced motion verification
- ARIA audit spreadsheet

**Output:** ✅ Approved / 🔄 Changes Required (zero tolerance for AA)

---

### Gate 5: Performance Review (Principal Performance Engineer)

**When:** Production build deployed to preview  
**Duration:** 1 business day  
**Blocking:** Budget violations block release

| Metric | Target | Measurement |
|--------|--------|-------------|
| **LCP** | ≤ 2.5s | Lighthouse (Mobile 4G throttling) |
| **INP** | ≤ 200ms | Lighthouse + RUM |
| **CLS** | ≤ 0.1 | Lighthouse |
| **TTFB** | ≤ 800ms | Server timing |
| **JS Bundle (gz)** | ≤ 170 KB | Vite bundle analyzer |
| **CSS Bundle (gz)** | ≤ 35 KB | Vite bundle analyzer |
| **Fonts (gz)** | ≤ 50 KB | Network tab |
| **Requests** | < 40 initial | Network tab |
| **Lighthouse Perf** | ≥ 95 | Lighthouse CI |

**Artifacts Required:**
- Lighthouse CI report (HTML + JSON)
- Bundle analyzer output (HTML)
- RUM dashboard screenshot (if live)
- Core Web Vitals RUM data (if available)

**Output:** ✅ Approved / 🔄 Changes Required

---

### Gate 6: Final Approval (Enterprise Release Manager)

**When:** All Gates 1–5 passed, documentation complete  
**Duration:** 0.5 business day  
**Blocking:** Final production gate

| Checklist Item | Verification |
|----------------|--------------|
| All Gates 1–5 passed | Gate status = Approved |
| Documentation complete | Component docs, page docs, DDR updated |
| Preview live | Preview URL accessible to stakeholders |
| Approval log complete | All 6 gates signed in approval log |
| Dependencies resolved | No blocking tickets |
| Rollback plan | Documented + tested |
| Stakeholder sign-off | PM + CDO + Engineering Lead |

**Artifacts Required:**
- Gate approval log (all 6 gates)
- Deploy preview URL
- Documentation links
- Rollback plan document
- Stakeholder sign-off (recorded)

**Output:** 🚀 **APPROVED FOR PRODUCTION** / 🔄 Changes Required

---

## 4. Gate Bypass Policy

**No gate bypass is permitted.** Exceptional circumstances:

| Scenario | Process | Approval |
|----------|---------|----------|
| **Critical security fix** | Gate 1–4 compressed to 4h; Gate 5 waived; Gate 6 by CTO | CTO + CDO |
| **Critical accessibility regression** | Gate 4 expedited (2h); other gates compressed | Principal A11y Architect + CDO |
| **Production outage fix** | Gate 5 waived; others compressed | CTO + CDO |

**All bypasses:** Documented in DDR; retrospective within 5 business days.

---

## 4. Preview Requirements

Every review must use a **live preview route**.

| Preview Route | Format | Required For |
|---------------|--------|--------------|
| `/design-system/preview/[component]` | Component isolation | Gate 2, 3 |
| `/preview/[page-path]` | Full page in context | Gates 2–6 |
| `/preview/flows/[journey]` | Multi-page flow | Gate 1, 4 |
| `/preview/responsive` | Viewport toggle | Gate 2 |

**Preview Requirements:**
- **URL:** Unique, shareable, stable
- **State:** Resets on load (or query param for state)
- **Annotations:** Gate status badge visible
- **Annotations:** Reviewer comments overlay (Figma-like)
- **Version:** Git SHA displayed in footer

---

## 5. Review Tracking

### Approval Log (Per Sprint)

| Component/Page | Gate | Reviewer | Status | Date | Comments |
|----------------|------|----------|--------|------|----------|
| Button Primary | Gate 1 | UX Architect | ✅ | 2026-07-10 | — |
| Button Primary | Gate 2 | CDO | ✅ | 2026-07-11 | Use primary token |
| Button Primary | Gate 3 | Frontend Arch | ✅ | 2026-07-12 | — |
| Button Primary | Gate 4 | A11y Architect | ✅ | 2026-07-12 | Focus ring OK |
| Button Primary | Gate 5 | Perf Engineer | ✅ | 2026-07-13 | 12KB gz |
| Button Primary | Gate 6 | Release Manager | ✅ | 2026-07-13 | Ready |

**Location:** `/docs/ui/approval-log/[sprint]/[component].md`

---

## 6. Review SLA

| Gate | Max Duration | Escalation |
|------|--------------|------------|
| Gate 1 | 2 business days | UX Architect → CDO |
| Gate 2 | 2 business days | CDO |
| Gate 3 | 2 business days | Frontend Architect → CTO |
| Gate 4 | 3 business days | A11y Architect → CDO |
| Gate 5 | 1 business day | Perf Engineer → CTO |
| Gate 6 | 0.5 business day | Release Manager → CPO |

**Total Max:** 10.5 business days (2.5 weeks) per component/page

---

## 6. Review Artifacts Checklist

### Per Component
- [ ] DDR (Design Decision Record)
- [ ] Figma component (all variants, states, breakpoints)
- [ ] Component docs (Props, Variants, States, A11y, Usage)
- [ ] Storybook stories (all variants + a11y addon)
- [ ] Unit tests (≥ 80% coverage)
- [ ] Visual regression baseline (Chromatic)
- [ ] a11y test (axe + manual)

### Per Page
- [ ] Wireframes (all breakpoints)
- [ ] High-fidelity mockups (all breakpoints)
- [ ] Page DDR
- [ ] Page docs (Purpose, IA, Variants, A11y, Preview)
- [ ] E2E test (critical path)
- [ ] Lighthouse CI baseline

---

## 7. Review Retrospective

**Frequency:** Per sprint  
**Duration:** 30 minutes  
**Participants:** Sprint team + Reviewers

**Agenda:**
1. Gate duration actuals vs SLA
2. Common rejection reasons
3. Process improvements
4. Tool improvements
5. Action items for next sprint

**Output:** Action items with owners + due dates

---

## 8. Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | Sprint 19 Part 1E | Initial framework |

---

**Authority:** Chief Design Officer  
**Review Cycle:** Per sprint (process); Annual (framework)