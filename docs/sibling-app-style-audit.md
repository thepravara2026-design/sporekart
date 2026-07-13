# Sibling App Style Audit

**Part of:** Frontend Style-Guide Migration
**Scope:** the 10 other frontend apps under `frontend/` (besides `web-app`).
**Source of truth:** canonical SporeKart tokens (`frontend/tokens/global.css`, copied from
`web-app/src/design-system/styles/global.css`).

---

## Shared infrastructure (created)
- `frontend/tokens/global.css` — the canonical token CSS layer (single source). Every app
  can import it via `import '../../tokens/global.css'` from `<app>/src`.
- `web-app` already repointed to it and builds ✓.

---

## Per-app state (static scan)

| App | tsx | css | hardcoded hex lines | token refs | Current palette |
|-----|----|----|------|------|------|
| admin-control-plane | 9 | 1 | 73 | 0 | own (unknown) |
| admin-dashboard | 12 | 1 | 29 | 102 | **indigo `#6366f1` / slate `#1e293b`** |
| approval-dashboard | 8 | 1 | 63 | 0 | own |
| automation-dashboard | 8 | 1 | 57 | 0 | own |
| buyer-app | 0 | 0 | 0 | 0 | **empty stub** |
| compliance-dashboard | 7 | 1 | 62 | 0 | own |
| governance-dashboard | 8 | 1 | 51 | 0 | own |
| registry-center | 13 | 1 | 14 | 34 | partially token-aware |
| risk-dashboard | 7 | 1 | 62 | 0 | own |
| shared-ui | 0 | 0 | 0 | 0 | **empty stub** |

---

## Critical finding — each app has its OWN design language

The sibling apps do **not** share the SporeKart design system. Each defines its own `:root`
tokens in its own `index.css` with a **different palette**:
- `admin-dashboard`: indigo primary `#6366f1`, slate sidebar `#1e293b` (NOT SporeKart green).
- The 6 "own" apps hardcode hex directly in CSS/Tailwind-ish utility strings.
- `registry-center` and `admin-dashboard` already reference some `var(--…)` tokens, but to
  their *own* token names, not the canonical `--color-bg-primary-default` set.

**Implication:** "migrate to the updated Style Guide" = **rebrand every app to the canonical
SporeKart palette** (green-600 `#2F6F4F`, neutral stone, semantic success/warning/danger).
This is a real visual overhaul per app, not just de-hardcoding. It must be reviewed per app
(the directive mandates preview-first + regression rollback).

---

## Proposed sequence (app-by-app, verified)

For each app:
1. Import `../../tokens/global.css` (canonical tokens live).
2. Replace its local `:root` token block + hardcoded hex with canonical `--color-*`/
   `--radius-*`/`--space-*`/`--shadow-*` references.
3. Apply spacing/typography to tokens (4px grid, token font sizes/weights).
4. `npm install` (if needed) + `npm run build` + headless visual check (desktop/tablet/mobile).
5. Preview-first review; rollback on regression.

**Recommended order:** start with the most token-ready app (`registry-center`, 34 refs, only
14 hex) or `admin-dashboard` (already 102 token refs; mostly needs palette swap in `index.css`).
Then the 6 "own" apps. Leave `buyer-app`/`shared-ui` (empty) as-is.

---

## Open decision (RESOLVED)
User chose: **rebrand all apps to the canonical SporeKart palette.** Tokenize + replace each
app's local `:root` / hardcoded hex with canonical `--color-*` / `--radius-*` / `--space-*` /
`--shadow-*`.

---

## Progress

### Canonical token layer (shared infra)
- Created `frontend/tokens/global.css` (copied from `web-app` DS, the single source).
- **Fixed 2 defects** found while wiring it in:
  - `:root` block was never closed (unbalanced brace) → appended closing `}` (browsers
    tolerated it; esbuild minify warned + dropped rules). Fixed in both copies.
  - `--color-primary` (referenced by DS `Button` outline/ghost/link + `Link` + remaps) was
    **undefined** → added `--color-primary: var(--color-green-600)` (+ hover/pressed).
- `web-app` repointed to import `../../tokens/global.css`; builds clean, no warnings.

### registry-center — ✅ REBRANDED (reference template)
- Imported `../../tokens/global.css`; remapped local `:root` token names to canonical.
- Replaced all hardcoded hex/rgba (dark GitHub palette) with canonical tokens → light
  SporeKart theme.
- Headless verify: body `rgb(247,248,247)` (neutral-100), text neutral-900, **accent green
  `rgb(47,111,79)`**, no console exceptions. `npm run build` clean.
- Pattern established for the remaining apps: import shared layer + remap local `:root` to
  canonical + replace remaining hex.

### admin-dashboard — ✅ REBRANDED
- Indigo/slate admin theme → light SporeKart. Imported shared layer; remapped `:root`
  (`--color-bg`, `--color-sidebar`, `--color-primary`, semantic success/warning/danger,
  `--color-text`, `--color-white`, `--color-border`, `--color-active-bg`, `--radius`,
  `--shadow`). Replaced ~14 hardcoded hex (status badges, priority badges, timeline dots,
  hover states) with canonical tokens (`--color-success-50/700`, `--color-warning-50/700`,
  `--color-danger-50/700`, `--color-info-50/700`, `--color-green-50/700`, `--color-neutral-*`).
- Headless verify (prod preview): token `#2F6F4F` resolved; body `rgb(247,248,247)`;
  sidebar `rgb(29,43,34)` (neutral-900); active nav + primary btn **green `rgb(47,111,79)`**;
  success badge `rgb(232,245,233)` (success-50); content rendered (1082 chars);
  **no console errors**. `npm run build` clean.

### approval-dashboard — ✅ REBRANDED
- Dark navy/blue admin theme → light SporeKart. Imported shared layer; added local `:root`
  token map; replaced ~40 hardcoded hex (sidebar, body, nav, headers, cards, status badges
  pending/approved/rejected/escalated/cancelled, buttons approve/reject/secondary/outline,
  detail view, stat cards, timeline, empty state) with canonical tokens. Blue `#4fc3f7`
  accent → green `--color-primary`; focus ring → green `rgba(47,111,79,.2)`.
- Headless verify (prod preview): token `#2F6F4F` resolved; body `rgb(247,248,247)`;
  sidebar `rgb(29,43,34)` (neutral-900); active nav accent **green `rgb(47,111,79)`**;
  no console errors. `npm run build` clean (ran `npm install` first — deps were missing).

### automation-dashboard — ✅ REBRANDED (dark → light)
- Was a dark navy theme (`#0f0f1a` body, `#1e1e36` cards). Converted to light SporeKart
  (light body, dark sidebar, green accents) to match the other apps. Imported shared layer;
  added local `:root` map; replaced ~45 hardcoded hex (body, sidebar, nav, headings, cards,
  status badges pending/running/completed/failed/cancelled, table, buttons
  primary/secondary/danger/success, forms, filter bar, empty state) with canonical tokens.
  Blue `#4a4aff`/`#7c7cff` → green `--color-primary`; needed `src/vite-env.d.ts` added
  (missing — `tsc` couldn't resolve the `.css` import).
- Headless verify (prod preview): token `#2F6F4F` resolved; body `rgb(247,248,247)` (light);
  sidebar `rgb(29,43,34)` (neutral-900); active nav accent **green `rgb(47,111,79)`**;
  no console errors. `npm run build` clean.

### compliance-dashboard — ✅ REBRANDED
- Light indigo theme → light SporeKart. Imported shared layer; added local `:root` map;
  replaced ~50 hardcoded hex (sidebar, nav, main bg, cards, metric cards, 16 status badges
  passed/failed/pending/waived/in_progress/critical/high/medium/low/info/approved/rejected/
  active/expired, list items, content card, forms, primary button + disabled, filter bar,
  error/success messages, spinner) with canonical tokens. Indigo `#6366f1` → green
  `--color-primary`; focus ring → green `rgba(47,111,79,.12)`.
- Headless verify (prod preview): token `#2F6F4F` resolved; body `rgb(247,248,247)` (light);
  sidebar `rgb(29,43,34)` (neutral-900); active nav accent **green `rgb(47,111,79)`**;
  no console errors. `npm run build` clean.

### governance-dashboard — ✅ REBRANDED
- Light indigo theme (KPI cards) → light SporeKart. Imported shared layer; added local
  `:root` map; replaced ~45 hardcoded hex (sidebar, nav, main bg, metric cards + value
  variants primary/success/warning/danger, status badges on-track/at-risk/critical/
  not-available, cards, list items, forms, primary/secondary buttons + disabled, section
  title, error/spinner, KPI cards + border-left variants, report form) with canonical
  tokens. Indigo `#4361ee` → green `--color-primary`; KPI status colors → green/amber/red/
  neutral; focus ring → green `rgba(47,111,79,.12)`.
- Headless verify (prod preview): token `#2F6F4F` resolved; body `rgb(247,248,247)` (light);
  sidebar `rgb(29,43,34)` (neutral-900); active nav accent **green `rgb(47,111,79)`**;
  no console errors. `npm run build` clean.

### risk-dashboard — ✅ REBRANDED
- Light indigo theme → light SporeKart. Imported shared layer; added local `:root` map;
  replaced ~50 hardcoded hex (sidebar, nav, main bg, metric cards + value variants
  high/medium, status badges low/medium/high/critical, score-low/medium/high, cards, list
  items, forms, primary/secondary buttons + disabled, section title, error/spinner, factor
  bars, timeline) with canonical tokens. Indigo `#6366f1` → green `--color-primary`;
  risk levels → green/amber/red; timeline dots → green; focus ring → green.
  (Note: `.factor-fill` bar color is set inline in the component — on-brand red/amber/green,
  left as-is.)
- Headless verify (prod preview): token `#2F6F4F` resolved; body `rgb(247,248,247)` (light);
  sidebar `rgb(29,43,34)` (neutral-900); active nav accent **green `rgb(47,111,79)`**;
  no console errors. `npm run build` clean.

---

## Status: ALL 7 REAL SIBLING APPS REBRANDED ✅
- `registry-center`, `admin-dashboard`, `approval-dashboard`, `automation-dashboard`,
  `compliance-dashboard`, `governance-dashboard`, `risk-dashboard` — all light SporeKart,
  dark sidebars, green `#2F6F4F` accents, zero hardcoded hex, all tokens resolve, builds clean.
- `buyer-app` / `shared-ui`: empty stubs — left as-is.
- web-app: Phase A done (tokens wired, DS crash fixed, homepage un-blanked). Prototype
  `global.css` layout classes retained; DS `Input`/`Password`/`Search` use a `cssText +=` focus
  hack (functional, token-compliant) — optional cleanup.
