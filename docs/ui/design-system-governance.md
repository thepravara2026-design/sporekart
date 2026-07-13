# Design System Governance — SporeKart Enterprise Web Application

## 1. Governance Model

### 1.1 Governance Structure

| Role | Responsibility | Authority |
|------|----------------|-----------|
| **Design System Steward** (Principal DS Architect) | Token/component integrity; versioning; migration | Final say on token/component changes |
| **Design System Council** (CDO, Principal DS Arch, Principal Frontend Arch, Principal A11y Arch, Principal Perf Eng) | Policy, DCRs, audits, strategy | Approve DCRs; set priorities |
| **Component Owners** (per component) | Component health; docs; tests; migrations | Implement changes; review PRs |
| **Platform Teams** (Web, Android, iOS) | Consumption; feedback; platform-specific impl | Report issues; request features |

### 1.2 Decision Rights

| Decision | Owner | Consulted | Informed |
|----------|-------|-----------|----------|
| New token | Principal DS Arch | Component Owners | All teams |
| Token value change | Principal DS Arch | Component Owners, Perf Eng | All teams |
| New component | Principal DS Arch | CDO, Principal Frontend Arch | All teams |
| Component API change | Component Owner | Principal DS Arch, A11y Arch | Consumers |
| Component deprecation | Principal DS Arch | Component Owner | All teams |
| Visual language change | CDO | Principal Visual Designer | All teams |
| Performance budget change | Principal Perf Eng | Principal Frontend Arch | CDO |

---

## 2. Token Governance

### 2.1 Token Lifecycle

| Stage | Process | Gate |
|-------|---------|------|
| **Propose** | DCR submitted | DCR Review |
| **Design** | Token spec + Figma + code | Visual Review |
| **Implement** | Add to JSON + TS types + CSS vars | Code Review |
| **Validate** | Token lint + contrast + Figma sync | CI Gates |
| **Release** | Version bump + changelog | Release Gate |
| **Deprecate** | DCR + migration plan | Deprecation Gate |
| **Remove** | After 12 months | Removal Gate |

### 2.2 Token Change Rules

| Change Type | Authority | Migration |
|-------------|-----------|-----------|
| New primitive scale step | Principal DS Arch | None (additive) |
| New semantic alias | Principal DS Arch | None (additive) |
| Semantic alias value change | Principal DS Arch + CDO | 6-month migration |
| Primitive scale value change | Principal DS Arch + CDO | 6-month migration |
| Token rename | Principal DS Arch + CDO | 12-month migration (alias old name) |
| Token removal | Principal DS Arch + CDO | 12-month deprecation |

---

## 3. Component Governance

### 3.1 Component Lifecycle

| Stage | Criteria | Owner |
|-------|----------|-------|
| **Experimental** | Prototype; behind flag; no docs | Component Owner |
| **Beta** | Implemented; tests; docs; Storybook; a11y certified | Component Owner |
| **Stable** | 2 sprints in production; 0 critical bugs; frozen API | Component Owner |
| **Frozen** | Part of frozen system; changes require DCR | Component Owner + DS Arch |
| **Deprecated** | DCR approved; migration guide published | Component Owner + DS Arch |
| **Removed** | 12 months after deprecation | DS Arch |

### 3.2 Component API Freeze

**Once Stable, these are frozen without DCR:**
- Prop names, types, defaults
- Slot names, types
- Event names, payloads
- CSS class structure
- CSS custom property API
- Keyboard behavior
- ARIA attributes
- Focus management
- Visual appearance (tokens only)

### 3.3 Component Health Metrics (Quarterly Review)

| Metric | Target | Measurement |
|--------|--------|-------------|
| **Bundle Size** | ≤ budget | Bundle analyzer |
| **Test Coverage** | ≥ 80% | Vitest coverage |
| **A11y Score** | 0 violations | axe-core |
| **Visual Regressions** | 0 | Chromatic |
| **Usage** | Tracked | Bundle analyzer + code search |
| **Deprecation Age** | ≤ 12 months | Registry timestamp |

---

## 4. Icon Governance

### 4.1 Icon Set Management

| Category | Governance |
|----------|------------|
| **Required Set (55)** | Frozen; changes = DCR |
| **Extended Set** | Additive; monthly review |
| **Custom/Brand** | CDO approval only |

### 4.2 Icon Standards (Frozen)

| Property | Value |
|----------|-------|
| Grid | 24×24px |
| Stroke Width | 2px (1.5px at 16px) |
| Corner Radius | 2px |
| Caps/Joins | Round |
| Alignment | Optical center |
| Filled Variant | Same shapes; fill = currentColor |
| Color | `currentColor` (inherit) |
| Sizes | 16, 20, 24, 28, 32, 40, 48px (tokens) |

### 4.3 Icon Addition Process

1. **Request** → Component Owner
2. **Design** → Principal Visual Designer (matches style)
3. **Review** → Principal DS Arch (token compliance)
3. **Implement** → SVG optimized; added to sprite
4. **Release** → Minor version bump

---

## 5. Illustration Governance

### 5.1 Illustration Categories (Frozen)

| Category | Style | Sizes | Accent Color |
|----------|-------|-------|--------------|
| **Empty States** | Line art + 1 accent | xs, sm, md, lg, xl | Primary / Neutral |
| **Success** | Checkmark + context | sm, md, lg | Success |
| **Error/Warning** | Alert triangle + context | sm, md, lg | Danger / Warning |
| **Onboarding** | Character + context | lg, xl | Primary + Secondary |
| **Agriculture** | Line art (seed, leaf, soil) | md, lg | Primary |
| **AI/Intelligence** | Sparkles + nodes | sm, md, lg | Primary |
| **Governance** | Shield + document | sm, md, lg | Primary |

### 5.2 Illustration Standards (Frozen)

| Property | Value |
|----------|-------|
| Style | Minimal line art (2px stroke) |
| Corner Radius | 4px |
| Perspective | Flat / Isometric 2:1 |
| Accent | Single semantic color |
| Background | Transparent or `--color-surface` |
| Sizes | xs(64), sm(80), md(120), lg(160), xl(240) |

---

## 6. Theme Governance

### 6.1 Theme Hierarchy

| Theme | Status | Implementation |
|-------|--------|----------------|
| **Light** | **Implemented** | Default; all tokens defined |
| **Dark** | **Specified** | Token overrides defined; not implemented |
| **High Contrast** | **Architecture** | CSS `forced-colors` media query only |
| **Brand (Partner)** | **Architecture** | Token override file per partner |

### 6.2 Theme Switching

| Mechanism | Implementation |
|-----------|----------------|
| **Data Attribute** | `<html data-theme="dark">` |
| **System Preference** | `@media (prefers-color-scheme: dark)` |
| **User Preference** | `localStorage` + `matchMedia` listener |
| **Persistence** | `localStorage.setItem('theme', value)` |
| **Sync** | `storage` event listener across tabs |

### 6.3 Dark Theme Token Overrides (Specified)

| Token | Light | Dark |
|-------|-------|------|
| `color-background` | `neutral-100` | `neutral-950` |
| `color-surface` | `white` | `neutral-900` |
| `color-surface-raised` | `white` | `neutral-800` |
| `color-border` | `neutral-200` | `neutral-700` |
| `color-text-primary` | `neutral-900` | `neutral-50` |
| `color-text-secondary` | `neutral-600` | `neutral-400` |
| `color-text-disabled` | `neutral-400` | `neutral-600` |
| `color-primary` | `green-600` | `green-400` |
| `color-primary-hover` | `green-700` | `green-300` |
| `color-focus-ring` | `green-600` | `green-400` |
| `color-skeleton-base` | `neutral-200` | `neutral-800` |
| `color-skeleton-highlight` | `neutral-100` | `neutral-700` |
| `elevation-1` | `rgba(29,43,34,0.08)` | `rgba(0,0,0,0.4)` |
| `elevation-2` | `rgba(29,43,34,0.12)` | `rgba(0,0,0,0.5)` |

---

## 7. Release & Versioning

### 7.1 Versioning Policy (Semantic)

| Version | Trigger | Example |
|---------|---------|---------|
| **Major (X.0.0)** | Breaking token/component change; new design language | 1.0.0 → 2.0.0 |
| **Minor (X.Y.0)** | New component; new token; new variant; new icon | 1.0.0 → 1.1.0 |
| **Patch (X.Y.Z)** | Bug fix; doc update; internal refactor; non-visual | 1.0.0 → 1.0.1 |

### 7.2 Package Versioning

| Package | Policy |
|---------|--------|
| `@sporekart/tokens` | Independent; components declare peer range |
| `@sporekart/ui` | Tracks tokens; minor on token minor; major on token major |
| `@sporekart/icons` | Independent; aligns with token major |

### 7.3 Release Cadence

| Channel | Frequency | Approval |
|---------|-----------|----------|
| **Tokens** | As needed (with components) | CDO + Principal DS Arch |
| **Components** | Monthly (scheduled) + hotfix | CDO + Principal Frontend Arch |
| **Full System** | Quarterly (sprint-aligned) | CDO + CPO |

---

## 8. Cross-Platform Consistency

### 8.1 Shared Foundation

| Asset | Web | Android | iOS |
|-------|-----|---------|-----|
| **Tokens** | CSS vars + TS | XML + Compose | SwiftUI + Figma Tokens |
| **Components** | React | Compose | SwiftUI |
| **Icons** | SVG sprite | VectorDrawable | SF Symbols / Custom |
| **Illustrations** | SVG | VectorDrawable | PDF/SVG |
| **Typography** | System + Inter | System + Inter | System + Inter |

### 8.2 Platform-Specific Adaptations (Allowed)

| Area | Web | Android | iOS |
|------|-----|---------|-----|
| **Navigation** | Sidebar + Top bar | Bottom nav + Top bar | Tab bar + Nav bar |
| **Drawer** | Left sidebar | Bottom sheet | Side sheet |
| **Toast** | Top-right stack | Bottom snackbar | Top banner |
| **Date Picker** | Calendar popover | Material DatePicker | UIDatePicker |
| **Select** | Combobox popover | Material Menu | UIMenu / Popover |
| **Toast Duration** | 4s (success), 8s (warning) | 3.5s | 3s |

**Rule:** IA, tokens, components, patterns identical; only *presentation* adapts.

---

## 9. Documentation Governance

### 9.1 Required Documentation Per Asset

| Asset | Required Docs | Location |
|-------|---------------|----------|
| **Token** | Description, example, a11y note, primitive ref | `/docs/ui/design-system/tokens/` |
| **Component** | Props, variants, states, a11y, usage, preview | `/docs/ui/design-system/components/` |
| **Pattern** | Problem, solution, variants, a11y, code | `/docs/ui/design-system/patterns/` |
| **Icon** | Name, usage, sizes, filled/outline | `/docs/ui/design-system/icons/` |
| **Illustration** | Category, sizes, accent, usage | `/docs/ui/design-system/illustrations/` |

### 9.2 Documentation Update Rules

| Trigger | Required Update | Deadline |
|---------|-----------------|----------|
| New token | Token ref + semantic alias doc | Same PR |
| Token value change | Migration guide + token ref | Same release |
| New component | Full component doc | Before Gate 2 |
| Component API change | Prop/variant doc + migration note | Same release |
| Component deprecation | Migration guide + deprecation notice | Announcement day |
| New icon | Icon ref + usage example | Same PR |
| New illustration | Illustration guide + usage | Same PR |

---

## 9. Deprecation & Migration

### 9.1 Deprecation Timeline (Standard)

| Phase | Duration | Actions |
|-------|----------|---------|
| **Announce** | Day 0 | Docs notice; console warning; migration guide |
| **Support** | Months 0–6 | Bug fixes; codemods; migration help |
| **Deprecate** | Months 6–12 | No fixes; console error; migration mandatory |
| **Remove** | Month 12 | Code deleted; imports fail; docs archived |

### 9.2 Emergency Deprecation (Security/A11y)

| Phase | Duration |
|-------|----------|
| Announce | Immediate |
| Migrate | 2 weeks |
| Remove | 2 weeks |

### 9.3 Migration Support (Required)

| Support | Requirement |
|---------|-------------|
| **Codemod** | ≥80% automated migration |
| **Migration Guide** | Step-by-step for remaining 20% |
| **Support Channel** | Slack + office hours (weekly) |
| **Dashboard** | Deprecated usage by team |
| **SLA** | 100% teams migrated by Month 5 |

---

## 10. Audit & Compliance

### 10.1 Quarterly Design Audit

| Area | Method | Owner |
|------|--------|-------|
| Token Compliance | Code + Figma diff | Principal DS Arch |
| Component Usage | Bundle analyzer + search | Principal DS Arch |
| Visual Consistency | Chromatic review | Principal Visual Designer |
| Accessibility | axe + manual | Principal A11y Arch |
| Performance | Lighthouse CI + RUM | Principal Perf Eng |
| Documentation | Coverage scan | Docs Architect |

### 10.2 Annual External Audit

- External design system audit firm
- Full heuristic evaluation
- WCAG 2.2 AA audit
- Competitive benchmark
- Report to CPO/CTO/CDO

---

## 10. Version History

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | Sprint 19 Part 1E | Enterprise Design System Team | Initial governance |

---

**Authority:** Design System Council (CDO, Principal DS Arch, Principal Frontend Arch, Principal A11y Arch, Principal Perf Eng)
**Review Cycle:** Semi-annual (aligned with major version)
**Effective:** Sprint 19 Part 1E certification