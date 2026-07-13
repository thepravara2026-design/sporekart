# Design Freeze Policy — SporeKart Enterprise Web Application

## 1. Purpose

Define what "frozen" means for design assets, the process for changes, and the migration requirements. This policy ensures stability while allowing controlled evolution.

---

## 2. Frozen Assets

### 2.1 Design Tokens (Frozen at Sprint 19 Part 1D Certification)

| Category | Frozen Assets | Change Authority |
|----------|---------------|------------------|
| **Color Primitives** | All scale steps (50–950) for green, neutral, success, warning, danger, info | CDO + Principal DS Architect |
| **Color Semantics** | All semantic aliases (primary, surface, text, border, focus, etc.) | CDO + Principal DS Architect |
| **Typography** | Font families, scale (display → OTP), weights, line heights, tracking | CDO + Principal DS Architect |
| **Spacing** | Base unit (4px), scale (0–16), semantic tokens (page-padding, component-gap, etc.) | Principal DS Architect |
| **Radius** | Scale (none–full), component mappings (btn, input, card, modal, etc.) | Principal DS Architect |
| **Elevation** | Shadow levels (0–4), surface levels (0–4), backdrops | Principal DS Architect |
| **Z-Index** | Layer tokens (base–max) | Principal DS Architect |
| **Breakpoints** | xs, sm, md, lg, xl, 2xl values | Principal DS Architect |
| **Animation** | Durations (instant–slower), easings (standard, decelerate, etc.) | Principal Visual Designer |
| **Borders** | Widths (thin, thick), styles, colors | Principal DS Architect |
| **Opacity** | Scale (0–100), semantic (disabled, overlay, etc.) | Principal DS Architect |
| **Sizing** | Icon, illustration, container, component heights | Principal DS Architect |

### 2.2 Components (Frozen at Certification)

| Layer | Components | Frozen Date |
|-------|------------|-------------|
| **Primitives** | Box, Flex, Grid, Text, Heading, Spacer, Divider, VisuallyHidden | Sprint 19 Part 1E |
| **Core Interactive** | Button, IconButton, Link, Input, Textarea, Select, Checkbox, Radio, Switch, Label | Sprint 19 Part 1E |
| **Composite** | Card, Modal, Drawer, Popover, Tooltip, Dropdown, Tabs, Accordion, Table, DataGrid, Pagination, Breadcrumbs, Stepper, Avatar, Badge, Tag, Chip, Progress, Skeleton | Sprint 19 Part 1E |
| **Layout/Nav** | Header, Sidebar, Footer, NavLink, NavList, Breadcrumb, Pagination, Tabs, Stepper | Sprint 19 Part 1E |
| **Feedback** | Toast, ToastStack, Banner, Alert, Dialog, ConfirmDialog, EmptyState, LoadingOverlay | Sprint 19 Part 1E |
| **Icons** | All 55 required icons (outline + filled) | Sprint 19 Part 1E |
| **Illustrations** | All 7 categories × 5 sizes | Sprint 19 Part 1E |

---

## 3. What "Frozen" Means

### 3.1 Prohibited Without DCR

| Asset Type | Prohibited Changes |
|------------|-------------------|
| **Tokens** | Add/remove/rename primitive scales; change semantic alias values; change token naming |
| **Components** | Add/remove/rename props; change default props; change slot structure; change visual appearance; change keyboard behavior; change focus management; change ARIA attributes; remove variants/states; change default variant |
| **Icons** | Add/remove icons from required set; change stroke weight; change viewBox; change corner radius |
| **Illustrations** | Change style (stroke/fill); change accent color usage; change composition rules; change size tokens |
| **Components** | Remove component; rename component; change export structure; change sub-component structure |
| **Patterns** | Change layout grid; change container widths; change sidebar/header heights; change navigation patterns |

### 3.2 Allowed Without DCR (Maintenance)

| Asset Type | Allowed Changes |
|------------|-----------------|
| **Tokens** | Add new primitive scale step (e.g., green-50) if no semantic alias changes; add new semantic alias pointing to existing primitive |
| **Components** | Bug fixes (visual, behavioral, a11y); internal refactoring (no API change); test improvements; documentation updates; Storybook story additions; performance optimizations (no visual change) |
| **Icons** | Add new icons to *extended* set (not required 55); optimize SVG paths (no visual change) |
| **Illustrations** | Add new illustrations to *extended* library; fix SVG rendering bugs |
| **Tokens** | Documentation fixes; example updates |

---

## 4. Design Change Request (DCR) Process

### 4.1 When a DCR Is Required

| Change Type | DCR Required? |
|-------------|---------------|
| New design token (semantic) | Yes |
| New design token (primitive) | Yes |
| Token value change | Yes |
| Token rename | Yes |
| New component | Yes |
| Component prop API change | Yes |
| Component visual change | Yes |
| Component behavior change | Yes |
| Component removal | Yes |
| Component deprecation | Yes |
| New icon (required set) | Yes |
| New illustration category | Yes |
| Layout pattern change | Yes |
| Navigation pattern change | Yes |

### 4.2 DCR Template

```markdown
# DCR-[YYYYMMDD-XXX]: [Title]

## Summary
[One sentence]

## Type
[Token / Component / Pattern / Icon / Illustration]

## Affected Assets
- Tokens: [list]
- Components: [list]
- Patterns: [list]

## Justification
[Business/UX/Technical reason]

## Proposed Change
[Detailed description with before/after mockups]

## Impact Analysis
| Dimension | Impact | Migration Effort |
|-----------|--------|------------------|
| Components using this | [count] | [auto/manual/dev-days] |
| Pages affected | [count] | [dev-days] |
| Figma files | [count] | [design-days] |
| Documentation | [pages] | [dev-days] |
| Android/iOS | [yes/no] | [effort] |

## Migration Plan
1. [Step 1: Codemod for X]
2. [Step 2: Manual updates for Y]
3. [Step 3: Testing]
4. [Step 4: Deploy]

## Timeline
- Announcement: [Date]
- Migration Start: [Date]
- Migration Complete: [Date]
- Removal: [Date]

## Approvals
| Role | Name | Status | Date |
|------|------|--------|------|
| CDO | | | |
| Principal DS Architect | | | |
| Principal Frontend Architect | | | |
| CTO (if breaking) | | | |
```

### 4.3 DCR Lifecycle

```
DRAFT → IMPACT_ANALYSIS → REVIEW → APPROVED → ANNOUNCED → MIGRATING → DEPRECATED → REMOVED
```

| Stage | Duration | Exit Criteria |
|-------|----------|---------------|
| **Draft** | 1–3 days | DCR complete with impact analysis |
| **Impact Analysis** | 2–5 days | All affected teams consulted; migration plan complete |
| **Review** | 2–5 days | All approvers reviewed; questions resolved |
| **Approved** | Day 0 | All approvals recorded |
| **Announced** | Day 0 | Added to deprecation log; migration guide published |
| **Migrating** | 6 months | All affected code migrated; deprecated usage = 0 |
| **Deprecated** | 6 months | Console warnings; no new usage; migration support |
| **Removed** | 12 months | Code deleted; imports fail; docs updated |

---

## 5. Design Deprecation Policy

### 5.1 Standard Deprecation (12 Months)

| Phase | Duration | Requirements |
|-------|----------|--------------|
| **Announcement** | Day 0 | Deprecation notice in docs, console warning, migration guide |
| **Supported** | Months 0–6 | Bug fixes only; migration assistance; codemods available |
| **Deprecated** | Months 6–12 | No bug fixes; console error; migration mandatory |
| **Removed** | Month 12 | Code deleted; imports fail; docs archived |

### 5.2 Emergency Deprecation (Security/Accessibility)

| Phase | Duration |
|-------|----------|
| Announcement | Immediate |
| Migration Window | 2 weeks |
| Removal | 2 weeks |

**Authority:** CDO + CTO joint decision

---

## 6. Design Versioning

### 6.1 Semantic Versioning for Design System

| Version | Trigger |
|---------|---------|
| **Major (X.0.0)** | Breaking token/component change; new design language; removed assets |
| **Minor (X.Y.0)** | New non-breaking tokens; new components; new variants; new icons/illustrations |
| **Patch (X.Y.Z)** | Bug fixes; doc updates; internal refactors; non-visual improvements |

### 6.2 Version Synchronization

| Package | Version Policy |
|---------|----------------|
| `@sporekart/tokens` | Independent; components declare peerDependency range |
| `@sporekart/ui` | Tracks tokens; minor bump on token minor; major on token major |
| `@sporekart/icons` | Independent; aligns with token major |

### 6.3 Release Schedule

| Channel | Cadence | Approval |
|---------|---------|----------|
| **Tokens** | As needed (with component releases) | CDO + Principal DS Architect |
| **Components** | Monthly (scheduled) + hotfix | CDO + Principal Frontend Architect |
| **Full System** | Quarterly (aligned to sprint) | CDO + CPO |

---

## 7. Migration Support

### 7.1 Required for Every Breaking Change

| Support | Requirement |
|---------|-------------|
| **Codemod** | Automated migration for ≥80% of cases |
| **Migration Guide** | Step-by-step manual for remaining cases |
| **Timeline** | 6 months supported; 12 months total |
| **Support Channel** | Dedicated Slack channel; office hours weekly |
| **Progress Tracking** | Dashboard: deprecated usage by team |

### 7.2 Migration Dashboard (Required)

| Metric | Target |
|--------|--------|
| Deprecated Usage | 0 by Month 6 |
| Codemod Coverage | ≥ 80% of occurrences |
| Team Migration | 100% teams migrated by Month 5 |
| Support Tickets | < 5/month after Month 3 |

---

## 8. Exception Process

### 8.1 Design Exception Request (DER)

For rare cases where standards cannot be met:

```markdown
# DER-[YYYYMMDD-XXX]: [Title]

## Exception Requested
[Which standard/token/component]

## Justification
[Why standard cannot be met]

## Impact
[Scope, duration, affected teams]

## Mitigation
[How to minimize deviation]

## Time-Box
[Max 1 sprint; with paydown plan]

## Approvals
| Role | Name | Status | Date |
|------|------|--------|------|
| CDO | | | |
| Principal DS Architect | | | |
| Principal Frontend Architect | | | |
```

### 8.2 DER Rules

| Rule | Detail |
|------|--------|
| **Max Duration** | 1 sprint (2 weeks) |
| **Paydown Required** | Technical debt ticket created with sprint target |
| **No A11y Exceptions** | Never allowed for accessibility |
| **No Security Exceptions** | Never allowed for security |
| **No Token Hardcoding** | Never allowed |

---

## 8. Design System Release Process

### 8.1 Release Checklist

| Step | Owner | Verification |
|------|-------|--------------|
| 1. Version bump (semver) | Release Manager | `npm version` |
| 2. Changelog generated | Release Manager | `conventional-changelog` |
| 3. Tokens published | Release Manager | `npm publish @sporekart/tokens` |
| 4. Components published | Release Manager | `npm publish @sporekart/ui` |
| 5. Icons published | Release Manager | `npm publish @sporekart/icons` |
| 6. Storybook deployed | Release Manager | Chromatic deploy |
| 7. Docs deployed | Release Manager | GitHub Pages / Vercel |
| 8. Migration guide published | Docs Architect | `/migration/vX.Y.Z.md` |
| 9. Slack announcement | Release Manager | `#design-system-releases` |

### 8.2 Hotfix Process

| Step | Timeline |
|------|----------|
| 1. Identify critical bug | Immediately |
| 2. Create hotfix branch from tag | < 1 hour |
| 3. Fix + test | < 4 hours |
| 4. Patch version bump | Immediately |
| 5. Publish patch | < 1 hour |
| 5. Notify consumers | < 1 hour |

---

## 8. Compliance & Enforcement

### 8.1 Automated Enforcement

| Check | Tool | When |
|-------|------|------|
| Token usage (no hardcoded values) | Stylelint custom rule | CI + pre-commit |
| Component API compliance | Custom ESLint rule | CI + pre-commit |
| Deprecated usage | Custom ESLint rule | CI + pre-commit |
| Token drift (code vs Figma) | Tokens Studio diff | Weekly CI |
| Component drift (code vs Figma) | Chromatic | Per PR |

### 8.2 Non-Compliance Consequences

| Violation | First | Repeated |
|-----------|-------|----------|
| Hardcoded value | PR blocked | Formal warning + debt ticket |
| DCR bypass | Sprint blocked | Escalation to CPO/CTO |
| Gate bypass | Release blocked | Escalation to CPO/CTO |
| Accessibility regression | Immediate hotfix | A11y freeze on team |
| Performance regression | Sprint blocked | Perf freeze on team |

---

## 8. Version History

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | Sprint 19 Part 1E | Enterprise Design Language Team | Initial policy |

---

**Authority:** Chief Design Officer + Principal Design System Architect  
**Review Cycle:** Semi-annual (aligned with major version)  
**Effective:** Upon Sprint 19 certification approval