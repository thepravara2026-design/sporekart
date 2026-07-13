# Frontend Style-Guide Migration — Plan & Audit

> **Directive:** Enterprise UI Modernization — migrate the entire `web-app` frontend visual
> system to the canonical Style Guide (design tokens + `design-language.md`), token-only,
> non-destructive, regression-safe.
> **Source of truth:** `frontend/web-app/src/tokens/*.json` + `docs/ui/design-language.md`
> (per user: "use existing docs").
> **Scope:** whole `web-app` frontend (the active app under `frontend/web-app`).

---

## 0. TL;DR — What the migration actually is

The styling system is **already token-designed**. The problem is **not** that components
hardcode values arbitrarily — it is that the token system is **not wired into the running
app** and the Design System (DS) components **crash on render** due to a systemic React
anti-pattern. The migration is therefore:

1. **Wire the canonical token layer into the app** (currently dead/unimported).
2. **Fix the systemic `style`-prop string crash** in the DS so token-driven components render.
3. **Reconcile the two conflicting `global.css` files** and the active surfaces
   (navigation prototype + Sprint 21 public site) to consume tokens.
4. **Audit + remediate the rest of the DS** (≈350 files) and confirm the 10 sibling
   frontend apps.

This is a large but well-defined job. It is being executed in safe, reviewable increments.

---

## 1. Canonical Style Source (what "compliant" means)

| Source | Location | Status |
|--------|----------|--------|
| Color tokens | `src/tokens/color.json` | ✅ complete (green/neutral/semantic/data-viz) |
| Typography | `src/tokens/typography.json` | ✅ complete (fontFamily/size/weight/lineHeight/letterSpacing) |
| Radius | `src/tokens/radius.json` | ✅ complete (4→9999 scale + component mappings) |
| Spacing | `src/tokens/spacing.json` | ✅ complete (4px base scale) |
| Elevation | `src/tokens/elevation.json` | ✅ complete (shadow 1–4 + surface levels) |
| Border | `src/tokens/border.json` | ✅ complete (width/style/color) |
| Animation / opacity / sizing / z-index / breakpoints | `src/tokens/*.json` | ✅ present |
| Design language / brand rules | `docs/ui/design-language.md` | ✅ principles (4px grid, semantic color only, 8/12px radii, elevation 1–2, tabular nums, skeletons not spinners) |
| **Compiled token CSS variables** | `src/design-system/styles/global.css` | ✅ complete — defines `--color-*`, `--radius-*`, `--space-*`, `--text-*`, `--weight-*`, `--shadow-*`, etc. (marked "generated from design tokens") |
| **Runtime token context** | `src/design-system/context/token-context.tsx` | ✅ `useToken(path)` / `getToken` available |

**Conclusion:** the token system is the single source of truth and is complete. No new
token values are required.

---

## 2. Current State — Defects Found

### D2.1 (CRITICAL) — Canonical token layer is not imported
- `src/main.tsx` imports **only** `./styles/global.css` (the *navigation-prototype* palette:
  `--sk-bg`, `--sk-accent`, `--sk-focus:#1b5e9c`, danger `#c0392b`, etc.).
- `src/design-system/styles/global.css` (the real token variables) is **never imported**.
- **Effect:** every DS component's `var(--color-bg-primary-default)`, `var(--radius-card)`,
  `var(--space-4)`, etc. resolves to nothing → DS components are unstyled even if they render.

### D2.2 (CRITICAL) — DS components crash: CSS-text string passed to React `style` prop
- DS components build styles as **multi-line CSS-string template literals** and pass them as
  `style={style as React.CSSProperties}` (e.g. `Button.tsx:154`, `Card.tsx:122`,
  `FeatureCard.tsx`, `Checkbox.tsx`, `Input.tsx`, `Password.tsx`, `OtpInput.tsx`,
  `RadioGroup.tsx`, `Link.tsx`, `FloatingActionButton.tsx`, `ButtonGroup.tsx`, …).
- React 18 rejects a **string** for `style` → throws
  `The 'style' prop expects a mapping from style properties to values, not a string.`
- Some components also embed pseudo-selectors (`&:hover`) and `cssText +=` focus hacks inside
  the string — invalid in inline styles even if the string were accepted.
- **Effect:** any page importing a DS `Button`/`Card`/`FeatureCard` (incl. the Sprint 21
  homepage) **renders blank / crashes**. This is the root cause of the reported blank homepage.
- The DS was never exercised in a real render before Sprint 21 imported it → the bug was latent.

### D2.3 (HIGH) — Two conflicting `global.css` files
- `src/styles/global.css` (prototype, `--sk-*` vars, layout classes `.sk-shell`/`.sk-sidebar`/etc.)
- `src/design-system/styles/global.css` (canonical token vars, `body`/`:root` rules)
- Both define `:root`/`body`. Importing both risks conflicting `body` background/font rules.
- The prototype defines a *drifted* palette (e.g. `--sk-focus:#1b5e9c` not in token set;
  danger `#c0392b` vs token `#C62828`).

### D2.4 (MEDIUM) — Sprint 21 public site uses drifted prototype vars
- Homepage sections + the `ui.tsx` shims I added use `var(--sk-surface)`, `var(--sk-accent)`,
  etc. (prototype palette), not the canonical `--color-*` tokens.
- This is itself a compliance gap (works, but not token-canonical).

### D2.5 (MEDIUM) — Sibling frontend apps
- `frontend/` contains 10 other apps (admin-control-plane, admin-dashboard, buyer-app,
  approval-dashboard, automation-dashboard, compliance-dashboard, governance-dashboard,
  registry-center, risk-dashboard, shared-ui). Their styling state is **unaudited**; they may
  or may not share this DS. Mass edits there are deferred pending audit + explicit go-ahead.

---

## 3. Remediation Strategy (phased, regression-safe)

### Phase A — Foundation (this increment)
- [ ] Import `src/design-system/styles/global.css` in `main.tsx` (canonical tokens become live).
- [ ] Reconcile the two `global.css` files: keep prototype *layout* classes
      (`.sk-shell`, `.sk-sidebar`, …) but remove the duplicated/derived color vars; ensure
      `body` resolves to canonical tokens (no conflict).
- [ ] Add a shared **scoped-style helper** (`useScopedStyle`) that turns a CSS-string +
      `&`-pseudo rules into a real `<style>` block keyed by a unique class — the single,
      reusable fix for D2.2.
- [ ] Repair the DS components actually used by the live app: `Button`, `Card`, `FeatureCard`
      (use the helper; preserve tokens, focus ring, hover).
- [ ] Repoint Sprint 21 homepage sections from the `ui.tsx` shims to the fixed DS; delete shims.
- [ ] `tsc --noEmit` + `npm run build` + headless console/visual check (no errors, homepage
      renders with tokens).

### Phase B — DS systemic remediation (requires go-ahead)
- [ ] Apply the same `useScopedStyle` fix across the remaining DS components that use the
      string-`style` pattern (`Checkbox`, `Input`, `Password`, `OtpInput`, `RadioGroup`,
      `Link`, `ButtonGroup`, `FloatingActionButton`, composite cards, navigation, feedback, …).
- [ ] Verify DS Playground (`/design-system` or equivalent) renders every component.
- [ ] Per-component visual regression screenshots (desktop/tablet/mobile).

### Phase C — Surface & sibling audit (requires go-ahead)
- [ ] Audit + align the navigation prototype surfaces to canonical tokens.
- [ ] Audit the 10 sibling frontend apps; remediate only those that share this DS, with
      explicit per-app confirmation.

---

## 4. Quality Gates (per directive)
- ✅ Style-guide compliance (tokens only, no hardcoded `#hex`/arbitrary px)
- ✅ Typography / color / gradient / radius / elevation consistency
- ✅ Responsive (desktop/laptop/tablet/mobile), no overflow / CLS
- ✅ Accessibility (WCAG 2.2 AA, keyboard, focus-visible, reduced-motion, ARIA)
- ✅ Performance (no CLS, no unused CSS, token-only)
- ✅ **No functionality changed, no routes/logic touched**
- ✅ No console errors

---

## 5. Out of scope (strictly prohibited per directive)
Business logic, auth/OTP, marketplace/checkout/training/admin flows, Supabase/API, routing
structure, React state/hooks/context, services, RBAC, validation, permissions, existing
sprint flow. Only visual presentation changes.

---

## 6. Status
- Sprint 21 Parts 1–3: committed & pushed (`sporetest`, `cfe3d16`).
- Sprint 21 Part 4 (inner pages): built, uncommitted, awaiting review.
- Style migration:
  - **Phase A (web-app foundation): DONE** — token layer wired, `Button`/`Card`/`FeatureCard`
    crash fixed via `useScopedStyle`, homepage un-blanked, `Link` fixed, `ui.tsx` shim removed.
  - **Shared infra: DONE** — `frontend/tokens/global.css` (canonical, single source); fixed
    `:root` close + added `--color-primary`. `web-app` repointed to it.
  - **Sibling apps: 7 of 7 done** — `registry-center`, `admin-dashboard`, `approval-dashboard`,
    `automation-dashboard`, `compliance-dashboard`, `governance-dashboard`, `risk-dashboard`
    all rebranded to SporeKart (light theme, dark green sidebars, green accents), verified,
    builds clean. `buyer-app`/`shared-ui` are empty stubs (left as-is).
  - See `docs/sibling-app-style-audit.md` for per-app state + sequence.
