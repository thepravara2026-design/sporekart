# Sprint 19 Part 1D Review Notes
## Phase 5 / Sprint 19 (Part 1D): Enterprise Design Language & Brand Guidelines

> **Status:** Reviewer guide. Sprint stops for review after showcase page is live.

---

## 1. What to Review

### Documentation (12 files)
| File | Review Focus |
|------|--------------|
| `design-language.md` | Visual tone, shape language, depth philosophy — does it feel "premium enterprise"? |
| `brand-guidelines.md` | Voice/tone, logo usage, co-branding — any conflicts with agri brand? |
| `color-system.md` | Green scale, semantic aliases, contrast validation — any gaps? |
| `typography.md` | Scale, responsive clamp, mono for data — readable on low-end phones? |
| `spacing-system.md` | 4px base, semantic tokens, responsive clamp — any missing scales? |
| `design-tokens.md` | Architecture, naming, theme chain, build pipeline — dev-ready? |
| `elevation-system.md` | 5 levels, when to use each, dark theme adjustments — sufficient? |
| `iconography.md` | Stroke, filled/outline, sizes, a11y — consistent with radius? |
| `illustration-guidelines.md` | Style, categories, sizes, a11y — matches empty states from 1C? |
| `theme-foundation.md` | Light/dark/HC/brand chain, switching, fallbacks — complete? |
| `token-naming-convention.md` | CTI-inspired, linting rules, deprecation — enforceable? |
| `design-review-notes.md` | This file — open questions captured? |

### Prototype Showcase (`/design-showcase`)
| Section | Test |
|---------|------|
| Color Palette | All semantic tokens rendered, contrast badges visible |
| Typography | Scale responsive, mono for data, line heights correct |
| Spacing | Grid overlay, primitive + semantic, component padding |
| Elevation | 5 levels visible, hover states, dark toggle |
| Radius | All component radii, interactive |
| Icons | Sizes, stroke, filled/outline toggle |
| Illustrations | Empty state, success, error, feature placeholders |
| Token Inspector | Search/filter, copy CSS/TS, theme toggle |
| Responsive Preview | Breakpoint toggle, grid overlay |

---

## 2. Open Questions for Part 1E (Component Library)

### 2.1 Token Priorities
1. **Which first?** Spacing/sizing → Color → Typography → Radius/Elevation → Animation?
2. **JSON format** — Style Dictionary vs Token Studio vs custom?
2. **Build output** — CSS vars + TS types + SCSS map + Figma sync?

### 2.2 Component Freeze Scope (Part 1E)
| Component | Priority | Dependencies |
|-----------|----------|--------------|
| Button | P0 | Color, radius, elevation, typography, icon |
| Input/Textarea | P0 | Color, radius, spacing, typography, elevation |
| Select/Combobox | P0 | Input, elevation, icon, animation |
| Table | P0 | Typography, spacing, elevation, radius |
| Card | P0 | Radius, elevation, spacing, illustration |
| Modal/Dialog | P0 | Elevation, radius, animation, focus trap |
| Toast | P0 | Color, elevation, animation, icon |
| Tooltip | P1 | Elevation, radius, animation |
| Tabs | P1 | Color, radius, typography, animation |
| Accordion | P1 | Radius, spacing, animation, icon |
| Avatar | P1 | Radius, sizing, color, illustration fallback |
| Badge | P1 | Radius, color, typography |
| Dropdown/Menu | P2 | Elevation, radius, animation, keyboard |
| Date Picker | P2 | Combobox, elevation, calendar, a11y |
| File Upload | P2 | Input, drag-drop, elevation, progress |
| Pagination | P2 | Button, spacing, typography |
| Progress | P2 | Color, radius, animation |
| Skeleton | P2 | Color, animation, spacing |

**Question:** Freeze all P0+P1 in Part 1E, or stagger?

### 2.3 Dark Mode Timing
- Part 1E (with components) or Part 1F (separate)?
- If Part 1E: need dark values for every component token

### 2.4 Animation Library
- CSS-only (current) or Framer Motion (bundle + a11y work)?
- If CSS: define `animation.duration.*` + `animation.easing.*` tokens now

### 2.5 Icon Set
- Build custom (SVG sprite, tree-shakable) or use `lucide-react` / `tabler-icons`?
- Custom = brand control; library = speed + a11y tested

### 2.6 Date Picker Strategy
- Custom ARIA calendar (full control, ~3KB) or `react-day-picker` (12KB, battle-tested)?

### 2.7 Table Virtualization
- `tanstack-virtual` (headless, 3KB) or `react-virtuoso` (batteries included, 8KB)?

### 2.8 Toast System
- Custom portal + stack (3KB) or `sonner` / `react-hot-toast` (5-8KB)?

### 2.9 Form Library
- `react-hook-form` + `zod` (standard) — confirm?

### 2.10 Error Boundary UI
- Global error boundary with "Something went wrong" + retry — design now?

### 2.11 Print Styles
- `@media print` tokens needed? (hide chrome, break tables, show URLs)

### 2.12 RTL Readiness
- Logical properties only (`margin-inline`, `padding-inline`) — enforce in lint?

### 2.13 Font Subsetting
- Devanagari subset for Hindi/Marathi — include in Part 1E font bundle?

### 2.14 CSP / Nonce Strategy
- Vite generates inline styles for CSS vars — need `style-src 'nonce-{nonce}'`?

---

## 3. Known Limitations (This Sprint)

- No component library — showcase uses raw CSS vars
- Dark theme values defined but not implemented in prototype
- High contrast theme CSS-only (no React context)
- Brand theme architecture only (no partner tokens)
- No Figma sync pipeline yet
- No visual regression testing (Chromatic) configured
- Animation tokens foundation only (no library)

---

## 4. Review Gate

| Reviewer | Role | Decision | Date |
|----------|------|----------|------|
| __________ | Chief Design Officer | ☐ Approve ☐ Changes | ______ |
| __________ | Principal Brand Designer | ☐ Approve ☐ Changes | ______ |
| __________ | Principal Design System Architect | ☐ Approve ☐ Changes | ______ |
| __________ | Principal Frontend Architect | ☐ Approve ☐ Changes | ______ |
| __________ | Principal Accessibility Architect | ☐ Approve ☐ Changes | ______ |

**All 5 must Approve** before Part 1E starts.

---

## 5. Next Steps (Part 1E)

Upon approval:
1. Initialize `@sporekart/tokens` package with Style Dictionary
2. Generate CSS vars, TS types, SCSS map
3. Build component library (Storybook + tests + a11y)
4. Freeze P0+P1 components per table above
5. Add visual regression (Chromatic)
6. Migrate prototype to use token package + component library