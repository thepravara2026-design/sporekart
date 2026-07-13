# Approval Log Template

Use this template for every page/component review. Save as `/docs/ui/approval-logs/[kebab-case-name].md`

---

# Approval Log: [Page/Component Name]

## Overview
- **Type:** [Page / Component / Pattern / Pattern Instance]
- **Route/Path:** [e.g., `/orders/:id` or `Button`]
- **Sprint:** [Sprint Number]
- **Design System Version:** [X.Y.Z]

## Gate Approvals

| Gate | Reviewer | Role | Status | Date | Comments |
|------|----------|------|--------|------|----------|
| 1: UX Review | [Name] | Principal UX Architect | [✅ Approved / 🔄 Changes Required / ❌ Rejected] | YYYY-MM-DD | |
| 2: Visual Design | [Name] | CDO / Principal DS Architect | [✅/🔄/❌] | YYYY-MM-DD | |
| 3: Implementation | [Name] | Principal Frontend Architect | [✅/🔄/❌] | YYYY-MM-DD | |
| 4: Accessibility | [Name] | Principal A11y Architect | [✅/🔄/❌] | YYYY-MM-DD | |
| 5: Performance | [Name] | Principal Performance Engineer | [✅/🔄/❌] | YYYY-MM-DD | |
| 6: Final Approval | [Name] | Release Manager | [✅/🔄/❌] | YYYY-MM-DD | |

## Artifacts
| Artifact | Link/Location | Version |
|----------|---------------|---------|
| Design Brief | [Figma/Confluence link] | v[X.Y] |
| Figma Frames | [Figma link] | v[X.Y] |
| Preview URL | [Netlify/Vercel/Preview URL] | [Commit SHA] |
| PR | [GitHub/GitLab PR link] | [PR #] |
| DDR (if new decision) | [DDR link] | [DDR-XXX] |

## Gate Details

### Gate 1: UX Review
- [ ] IA Compliance
- [ ] Task Flow Efficiency
- [ ] Persona Goal Coverage
- [ ] Edge Cases Identified
- [ ] Future-Proofing

### Gate 2: Visual Design
- [ ] Token Compliance (0 hardcoded values)
- [ ] Brand Compliance
- [ ] Visual Hierarchy
- [ ] Responsive (6 breakpoints)
- [ ] Dark Mode Ready
- [ ] Animation Specs

### Gate 3: Implementation
- [ ] TypeScript Strict: 0 errors
- [ ] ESLint: 0 errors
- [ ] Unit Tests: ≥80% coverage
- [ ] A11y Auto: 0 violations
- [ ] Visual Regression: 0 diffs
- [ ] Bundle Size: Within budget
- [ ] Token Usage: 100% semantic

### Gate 4: Accessibility
- [ ] axe-core: 0 violations
- [ ] Keyboard: 100% reachable
- [ ] NVDA + Chrome: Tested
- [ ] VoiceOver + Safari: Tested
- [ ] Focus Management: Correct
- [ ] Contrast: 4.5:1 text / 3:1 UI
- [ ] Zoom 200%: No horizontal scroll
- [ ] Reduced Motion: Respected

### Gate 5: Performance
- [ ] Lighthouse Perf ≥ 95
- [ ] LCP ≤ 2.5s
- [ ] INP ≤ 200ms
- [ ] CLS ≤ 0.1
- [ ] Bundle within budget

### Gate 6: Final
- [ ] All Gates 1-5 Passed
- [ ] Documentation Complete
- [ ] Preview URL Accessible
- [ ] Stakeholder Sign-off
- [ ] Rollback Plan Documented

## Change Log
| Date | Gate | Reviewer | Change |
|------|------|----------|--------|
| YYYY-MM-DD | 1 | [Name] | Initial submission |
| YYYY-MM-DD | 2 | [Name] | Visual feedback incorporated |

## Final Status
**Overall:** [✅ APPROVED / ❌ BLOCKED]
**Version:** [Design System Version]
**Preview URL:** [URL]
**Approved By:** [Release Manager Name]
**Date:** YYYY-MM-DD