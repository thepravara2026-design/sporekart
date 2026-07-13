# Style Governance — SporeKart Enterprise AI Platform
## Phase 5 / Sprint 19 (Part 1A): Enterprise Product Experience Foundation

> **Status:** Documentation Only. Defines how we keep the experience consistent
> across web and future mobile through tokens, naming, contribution rules,
> ownership, and automated checks.

---

## 1. Design Tokens

All visual decisions are expressed as tokens — not hardcoded values. Tokens are
the contract between design and code, and between web and native.

### Color
- `color.bg.*` — surfaces (base, raised, sunken, overlay).
- `color.text.*` — primary, secondary, disabled, on-accent.
- `color.brand.*` — primary, hover, active.
- `color.semantic.*` — success, warning, error, info (all AA-contrast).
- `color.border.*` — default, strong, focus.

### Typography
- `type.family.*` — UI, content, mono (data/IDs).
- `type.scale.*` — modular steps (e.g., -1 … 8).
- `type.weight.*` — regular, medium, semibold (restrained).
- `type.lineheight.*` — body, heading, dense.

### Spacing
- `space.*` — base 4px scale (1–16+ steps).
- Used for padding, gaps, margins — never arbitrary pixels.

### Radius
- `radius.*` — none, sm, md, lg, pill. Consistent across components.

### Elevation
- `elevation.*` — subtle shadow/overlay steps; restrained, not decorative.

### Z-Index
- `z.*` — established layers (base, sticky, overlay, modal, toast) to prevent
  stacking chaos.

Tokens are versioned with the design system (`component-freeze-policy.md`).

---

## 2. Naming Conventions

- **Components:** PascalCase, descriptive, category-prefixed where useful
  (e.g., `ProductCard`, `PrimaryButton`).
- **Tokens:** dot.notation, lowercase, grouped by category (above).
- **Routes:** kebab-case, noun-based (`/order-tracking`, not `/trackOrderNow`).
- **Files:** component folder + `index.tsx`, co-located styles/tests.
- **CSS classes:** scoped/BEM or CSS Modules; no global leaks.

---

## 3. Contribution Rules

- Reuse frozen components before creating new ones.
- New components require a proposal and full gate approval.
- All changes go through PR with design-system review.
- Tokens are edited only by the Design System team; consumers reference tokens,
  never raw values.
- Documentation (brief, tokens, usage) is updated with every change.

---

## 4. Ownership

- The **Design System Team** owns tokens, frozen components, and this governance.
- Design owns philosophy/principles adherence; Eng owns implementation fidelity;
  QA owns accessibility and regression.
- Disputes escalate to CPO + CXO.

---

## 5. Audit and CI Checks

Automated checks enforce governance on every PR:

- **Lint** — code style, naming, no raw token values (must use token var).
- **a11y** — automated accessibility (contrast, roles, labels, focus).
- **Token audit** — detect hardcoded colors/spacing/radii.
- **Component audit** — flag unapproved components or duplicated patterns.
- **Visual regression** — optional snapshot tests against frozen components.
- **Performance budget** — LCP/TTI/bundle checks fail the build on breach.

CI failing any check blocks merge until resolved or explicitly waived by the
Design System team with recorded reason.

---

## 6. Cross-Platform Consistency

- Web (React), and future Android/iOS/React Native, share the **same tokens**
  and the **same API layer** (`/api/v1/**`).
- Native platforms map tokens to platform equivalents; behavior and hierarchy
  are preserved, not reinvented.
- Governance applies equally to all surfaces; a token change propagates everywhere.
