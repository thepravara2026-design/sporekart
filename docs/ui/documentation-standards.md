# Documentation Standards — SporeKart Enterprise Web Application

## 1. Documentation Requirements Per Sprint

Every frontend sprint **MUST** update the following documentation directories:

```
/docs/
├── ui/                          # Design system documentation
│   ├── design-briefs/           # Per-page design briefs
│   ├── component-docs/          # Component documentation
│   ├── approval-logs/           # Gate approval records
│   ├── review-notes/            # Sprint review notes
│   └── migration/               # Migration guides
├── design-system/               # Design system reference
│   ├── tokens/                  # Token reference
│   ├── components/              # Component reference
│   ├── patterns/                # Pattern library
│   └── icons/                   # Icon reference
├── ddr/                         # Design Decision Records
│   ├── dcr-log.md               # DCR log
│   ├── exception-log.md         # Exception log
│   └── deprecation-log.md       # Deprecation log
├── approval-log/                # Per-page approval tracking
├── audits/                      # Quarterly audit reports
└── reviews/                     # Sprint review summaries
```

---

## 2. Design Brief (Required Per Page)

Every new or modified page **must** have a design brief using the template from Part 1A.

### 2.1 Design Brief Template

```markdown
# Design Brief: [Page Name]

## 1. Business Context
- **Sprint:** [Sprint Number]
- **Workspace:** [Workspace Name]
- **Page Route:** [e.g., /orders/:id]
- **Primary User:** [Persona from Part 1A]
- **Business Goal:** [One sentence]

## 2. User Goals
| Persona | Goal | Success Criteria |
|---------|------|------------------|
| [Persona] | [What they want to do] | [Measurable outcome] |

## 3. Information Architecture
- **Workspace:** [Workspace name from Part 1B]
- **Parent Page:** [Parent route]
- **Breadcrumb:** [Home › Workspace › Section › Page]
- **Child Pages:** [List if any]

## 4. Content Requirements
| Section | Content Type | Source | Required? |
|---------|--------------|--------|-----------|
| Header | [Title + actions] | [API/Static] | Yes |
| Content | [Description] | [API/Static] | Yes |

## 5. Interaction Requirements
| Trigger | Action | Feedback | Navigation |
|---------|--------|----------|------------|
| [Click] | [Action] | [Toast/Modal] | [Route] |

## 5. States
| State | Description | Visual |
|-------|-------------|--------|
| Loading | [Skeleton/Spinner] | [Ref] |
| Empty | [Illustration + CTA] | [Ref] |
| Error | [Inline + Toast] | [Ref] |
| Success | [Toast + redirect] | [Ref] |

## 6. Responsive Behavior
| Breakpoint | Layout Change | Navigation |
|------------|---------------|------------|
| xs (<640) | [Stack] | [Drawer] |
| md (768) | [Grid 2-col] | [Icon Rail] |
| xl (1280) | [Grid 3-col] | [Fixed Sidebar] |

## 6. Accessibility Requirements
- [ ] Semantic HTML landmarks
- [ ] Heading hierarchy (H1→H2→H3)
- [ ] Focus order matches visual
- [ ] Color not sole indicator
- [ ] 200% zoom no horizontal scroll

## 7. Performance Budget
| Metric | Target |
|--------|--------|
| LCP | ≤ 2.5s |
| Route chunk | ≤ [size] KB |
| Initial JS | ≤ [size] KB |

## 8. Dependencies
- **Tokens:** [List new/changed tokens]
- **Components:** [List new/changed components]
- **API:** [Endpoints required]
- **Design System Version:** [vX.Y.Z]

## 9. Approval Tracking
| Gate | Reviewer | Status | Date | Comments |
|------|----------|--------|------|----------|
| 1 UX | | | | |
| 2 Visual | | | | |
| 3 Impl | | | | |
| 4 A11y | | | | |
| 5 Perf | | | | |
| 6 Final | | | | |

---

## 3. Component Documentation (Required Per Component)

Every component in `@sporekart/ui` must have documentation in `/docs/ui/component-docs/[ComponentName].md`

### 3.1 Component Documentation Template

```markdown
# [ComponentName]

## Overview
[One-sentence description of purpose]

## When to Use
- [Use case 1]
- [Use case 2]

## When NOT to Use
- [Anti-pattern 1]
- [Anti-pattern 2]

## API Reference

### Props
| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| [prop] | [Type] | [default] | [Yes/No] | [Description] |

### Slots
| Slot | Description |
|------|-------------|
| [slot] | [Description] |

### Events
| Event | Payload | Description |
|-------|---------|-------------|
| [event] | [Type] | [Description] |

### Sub-components
| Sub-component | Description |
|---------------|-------------|
| [SubComponent] | [Description] |

## Variants
| Variant | Description | Use Case |
|---------|-------------|----------|
| [variant] | [Description] | [When to use] |

## States
| State | Visual | Description |
|-------|--------|-------------|
| Default | [Screenshot] | Normal state |
| Hover | [Screenshot] | Mouse over |
| Focus | [Screenshot] | Keyboard focus |
| Active | [Screenshot] | Pressed |
| Disabled | [Screenshot] | Not interactive |
| Loading | [Screenshot] | Async in progress |
| Error | [Screenshot] | Validation error |

## Accessibility
- **ARIA Roles:** [roles used]
- **Keyboard:** [keyboard interactions]
- **Screen Reader:** [announcements]
- **Focus Management:** [behavior]

## Responsive Behavior
| Breakpoint | Behavior |
|------------|----------|
| xs | [Stack/full-width] |
| md | [Inline/2-col] |
| xl | [Full layout] |

## Theming
- **Tokens Used:** [List token paths]
- **Dark Mode:** [Supported/Not yet]
- **High Contrast:** [Supported/Not yet]

## Examples

### Basic Usage
```tsx
import { ComponentName } from '@sporekart/ui';

<ComponentName prop="value" />
```

### With Variants
```tsx
<ComponentName variant="primary" size="lg" />
```

## Related Components
- [RelatedComponent1] — [Why related]
- [RelatedComponent2] — [Why related]

## Changelog
| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | YYYY-MM-DD | Initial release |

## Approval History
| Gate | Reviewer | Status | Date |
|------|----------|--------|------|
| 1 UX | | | |
| 2 Visual | | | |
| 3 Impl | | | |
| 4 A11y | | | |
| 5 Perf | | | |
| 6 Final | | | |
```

---

## 4. Design Decision Records (DDR)

Every significant design decision must be recorded in `/docs/ddr/DDR-XXX.md`

### 4.1 DDR Template

```markdown
# DDR-[NUMBER]: [Title]

## Status
[Proposed / Accepted / Superseded / Deprecated]

## Date
[YYYY-MM-DD]

## Decision Makers
- [Role]: [Name]

## Problem
[What problem does this decision solve?]

## Context
[Background information, constraints, alternatives considered]

## Decision
[What was decided? Be specific.]

## Alternatives Considered
| Alternative | Pros | Cons | Why Rejected |
|-------------|------|------|--------------|

## Consequences
### Positive
- [Benefit 1]
- [Benefit 2]

### Negative
- [Trade-off 1]
- [Trade-off 2]

### Risks
- [Risk 1]
- [Mitigation]

## Implementation
- [Ticket/Component references]
- [Migration if applicable]

## Related Decisions
- DDR-XXX: [Related decision]

## Metadata
- **Sprint:** [Sprint Number]
- **Version:** [Design System Version]
- **Status:** [Active/Superseded]
```

### 4.2 Required DDRs (Sprint 19 Baseline)

| DDR | Title | Status |
|-----|-------|--------|
| DDR-001 | Layout Strategy (App Shell + Sidebar + Content) | Accepted |
| DDR-002 | Navigation Strategy (Sidebar + Command Palette) | Accepted |
| DDR-003 | Typography System (System Stack + Modular Scale) | Accepted |
| DDR-004 | Color System (Green Primary + Semantic Aliases) | Accepted |
| DDR-005 | Responsive Strategy (6 Breakpoints + Drawer/Rail) | Accepted |
| DDR-006 | Accessibility Standard (WCAG 2.2 AA) | Accepted |
| DDR-007 | Design Token Architecture (Primitive→Semantic→Component) | Accepted |
| DDR-008 | Component Freeze Policy (Sprint 19 Part 1E) | Accepted |

---

## 4. Approval Logs

Every page/component approval tracked in `/docs/ui/approval-logs/[page-or-component].md`

### 4.1 Approval Log Template

```markdown
# Approval Log: [Page/Component Name]

## Overview
- **Type:** [Page / Component / Pattern]
- **Route/Path:** [if page]
- **Component:** [if component]
- **Sprint:** [Sprint Number]

## Gate Approvals

| Gate | Reviewer | Role | Status | Date | Comments |
|------|----------|------|--------|------|----------|
| 1: UX | [Name] | Principal UX Architect | [✅/🔄/❌] | YYYY-MM-DD | |
| 2: Visual | [Name] | CDO / Principal DS Architect | | | |
| 3: Implementation | [Name] | Principal Frontend Architect | | | |
| 4: Accessibility | [Name] | Principal A11y Architect | | | |
| 5: Performance | [Name] | Principal Perf Engineer | | | |
| 6: Final | [Name] | Release Manager | | | |

## Artifacts
- Design Brief: [link]
- Figma: [link]
- Preview URL: [link]
- PR: [link]
- DDR: [link if new decision]

## Final Status
- **Overall:** ✅ APPROVED / ❌ BLOCKED
- **Version:** [Design System Version]
- **Preview URL:** [URL]
```

---

## 4. Migration Guides

Every breaking change requires a migration guide at `/docs/ui/migration/vX.Y.Z.md`

### 4.1 Migration Guide Template

```markdown
# Migration Guide: vX.Y.Z

## Overview
[What changed and why]

## Breaking Changes

### Tokens
| Old Token | New Token | Action |
|-----------|-----------|--------|
| `color.primary` | `color.primary.default` | Update imports |

### Components
| Component | Change | Migration |
|-----------|--------|-----------|
| `Button` | `size` prop renamed to `scale` | `size="lg"` → `scale="lg"` |

### Patterns
| Pattern | Change | Migration |
|---------|--------|-----------|
| Layout | Container max-width changed | Update page wrapper |

## Automated Migration (Codemod)

```bash
npx @sporekart/codemod v1.2.0-to-v1.3.0
```

### What the Codemod Handles
- [ ] Token renames
- [ ] Prop renames
- [ ] Import path changes
- [ ] Deprecated component removal

### What Requires Manual Update
- [ ] Custom component compositions
- [ ] Complex prop transformations
- [ ] CSS-in-JS custom styles

## Manual Migration Steps

### 1. Update Imports
```tsx
// Before
import { Button } from '@sporekart/ui/button';

// After
import { Button } from '@sporekart/ui';
```

### 2. Update Props
```tsx
// Before
<Button size="lg" variant="primary" />

// After
<Button scale="lg" variant="primary" />
```

### 3. Update Styles
```css
/* Before */
.custom-button {
  padding: 16px;
}

/* After */
.custom-button {
  padding: var(--spacing-component-gap);
}
```

## Testing Checklist
- [ ] All existing tests pass
- [ ] Visual regression tests pass (Chromatic)
- [ ] Accessibility tests pass (axe)
- [ ] Performance budgets met
- [ ] No console errors/warnings

## Rollback Plan
If critical issues found:
1. Revert to previous version: `npm install @sporekart/ui@X.Y.Z`
2. Re-deploy preview
3. File regression issue

## Support
- **Slack:** #design-system-migration
- **Office Hours:** Tuesdays 10–11 AM
- **Docs:** [Migration FAQ](link)
- **Issues:** GitHub Issues with label `migration-vX.Y.Z`
```

---

## 5. Quarterly Audit Reports

Located at `/docs/audits/[YYYY-QN].md`

### 5.1 Audit Report Template

```markdown
# Design System Audit: [YYYY-QN]

## Executive Summary
[Overall health score, top 3 risks, top 3 wins]

## Audit Areas

### 1. Token Compliance
- **Scan Date:** [Date]
- **Tool:** Stylelint + custom rule
- **Results:** [X violations; Y files]
- **Top Violations:** [List]
- **Action Items:** [List]

### 2. Component Usage
- **Total Components:** [Count]
- **Orphaned Components:** [List]
- **Duplicate Components:** [List]
- **Usage Heatmap:** [Link to report]

### 3. Visual Consistency
- **Tool:** Chromatic
- **Baseline:** [Date]
- **Drifts Detected:** [Count]
- **False Positives:** [Count]
- **Action Items:** [List]

### 4. Accessibility
- **Tool:** axe-core + manual (NVDA/VoiceOver)
- **Pages Audited:** [Count]
- **Violations:** [Count by severity]
- **Regression:** [Yes/No]

### 5. Performance
- **LCP (p75):** [Value] (Target: ≤2.5s)
- **INP (p75):** [Value] (Target: ≤200ms)
- **CLS (p75):** [Value] (Target: ≤0.1)
- **Bundle Size:** [Value] KB (Target: ≤170KB gz)
- **Lighthouse Score:** [Value]/100

### 6. Documentation
- **Component Docs Coverage:** [X/Y components]
- **Design Briefs:** [X/Y pages]
- **Migration Guides:** [Count]
- **DDRs:** [Count new this quarter]

## Top 5 Risks
1. [Risk] — [Impact] — [Mitigation]
2. [Risk] — [Impact] — [Mitigation]
3. [Risk] — [Impact] — [Mitigation]
4. [Risk] — [Impact] — [Mitigation]
5. [Risk] — [Impact] — [Mitigation]

## Debt Paydown Plan
| Debt Item | Owner | Target Sprint | Status |
|-----------|-------|---------------|--------|

## Approvals
| Role | Name | Status | Date |
|------|------|--------|------|
| CDO | | | |
| Principal DS Architect | | | |
| Principal Frontend Architect | | | |
```

---

## 5. Review Summaries

Per sprint review at `/docs/reviews/sprint-XX.md`

### 5.1 Review Summary Template

```markdown
# Sprint [XX] Review Summary

## Sprint Goal
[What was the sprint goal?]

## Completed
| Item | Type | Status |
|------|------|--------|
| [Page/Component] | [Page/Component/Pattern] | ✅ Done |

## Gates Summary
| Gate | Items Reviewed | Passed | Blocked | Avg Duration |
|------|----------------|--------|---------|--------------|
| 1 UX | [Count] | [Count] | [Count] | [Days] |
| 2 Visual | [Count] | [Count] | [Count] | [Days] |
| 3 Impl | [Count] | [Count] | [Count] | [Days] |
| 4 A11y | [Count] | [Count] | [Count] | [Days] |
| 5 Perf | [Count] | [Count] | [Count] | [Days] |
| 6 Final | [Count] | [Count] | [Count] | [Days] |

## Metrics
| Metric | Value | Target |
|--------|-------|--------|
| Gate 1 Pass Rate (1st try) | [%] | ≥ 80% |
| Avg Gate Duration | [Days] | ≤ SLA |
| Re-review Rate | [%] | ≤ 20% |
| A11y Regressions | [Count] | 0 |
| Perf Regressions | [Count] | 0 |

## Stakeholder Feedback
| Source | Feedback | Action |
|--------|----------|--------|

## Retrospective
### What Worked
- [Item]

### What Didn't
- [Item]

### Action Items
| Action | Owner | Due |
|--------|-------|-----|

## Approval
| Role | Name | Status | Date |
|------|------|--------|------|
| Scrum Master | | | |
| Product Owner | | | |
```

---

## 6. File Naming Conventions

| Document Type | Pattern | Example |
|---------------|---------|---------|
| Design Brief | `design-brief-[kebab-case-page].md` | `design-brief-order-detail.md` |
| Component Doc | `component-docs/[ComponentName].md` | `component-docs/Button.md` |
| DDR | `ddr/DDR-[001-999].md` | `ddr/DDR-001.md` |
| DCR | `ddr/DCR-YYYYMMDD-XXX.md` | `ddr/DCR-20260713-001.md` |
| DER | `ddr/DER-YYYYMMDD-XXX.md` | `ddr/DER-20260713-001.md` |
| Approval Log | `approval-logs/[kebab-case-name].md` | `approval-logs/order-detail.md` |
| Migration Guide | `migration/vX.Y.Z.md` | `migration/v1.2.0.md` |
| Audit Report | `audits/YYYY-QN.md` | `audits/2026-Q3.md` |
| Review Summary | `reviews/sprint-XX.md` | `reviews/sprint-19.md` |

---

## 7. Version History

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | Sprint 19 Part 1E | Enterprise Design Language Team | Initial standards |

---

**Authority:** Documentation Architect  
**Review Cycle:** Quarterly  
**Effective:** Sprint 19 Part 1E