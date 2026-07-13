# Before / After Summary

**Part of:** Frontend Style-Guide Migration
**Status:** BEFORE documented; AFTER filled after Phase A foundation fix + headless capture.

---

## BEFORE (current, pre-migration)

| Surface | Observed state | Root cause |
|---------|---------------|------------|
| Sprint 21 homepage (`/`) | **Blank** (white, no crash text) | DS `Button`/`Card`/`FeatureCard` crash on string-`style`; prototype `global.css` only |
| Design System components | Unstyled / crash if imported | Token layer not imported (D2.1) + string-style crash (D2.2) |
| Navigation prototype | Renders, but on drifted `--sk-*` palette (`--sk-focus:#1b5e9c`, danger `#c0392b`) | prototype `global.css` only (D2.3) |
| Token system | Defined (JSON + compiled CSS) but **dead** in app | not imported (D2.1) |

**Compliance:** Token design 100% present, token *activation* 0%, DS renderability broken.

---

## AFTER (verified — Phase A, 2026-07-13)

| Surface | Verified state |
|---------|--------------|
| All surfaces | ✅ Canonical token layer live (`main.tsx` imports `design-system/styles/global.css`); `var(--color-*)`/etc. resolve |
| DS Button/Card/FeatureCard | ✅ Render with tokens; no console errors; primary btn `rgb(47,111,79)`=#2F6F4F green, radius 12px, weight 600 |
| Homepage (`/`) | ✅ Renders: 5,389 chars text, 16 `.sk-card`, 22 buttons, correct H1 (was blank) |
| Prototype `global.css` | ✅ Layout classes retained; canonical imported after so `body`/focus resolve to tokens (no conflict) |

**Headless verification (Chrome CDP, localhost:5173):**
- Console: only React Router v7 future-flag warnings; **no** "style prop expects a mapping" crash.
- DOM: `body.innerText` length 5389; `.sk-card`=16; `button`=22; `h1`="Grow premium mushrooms with confidence."
- Tokens: `--color-bg-primary-default`=#2F6F4F; primary button bg `rgb(47,111,79)`; body bg `rgb(247,248,247)` (neutral-100); radius 12px; weight 600.

**Root cause fixed:** systemic `style`-prop string crash in DS → reusable `useScopedStyle` helper; Button/Card repaired; Sprint 21 homepage repointed from drifted `ui.tsx` shim (which used a blue `#1d4ed8` primary fallback) to the canonical DS.

---

## Regression checklist (post-fix)
- [ ] Homepage renders, no blank, no console errors
- [ ] Buttons show primary/secondary/ghost variants with correct tokens
- [ ] Cards show radius 12–16px, elevation 1–2, semantic borders
- [ ] Focus-visible ring uses `--color-focus-ring` (green-600), not blue `#1b5e9c`
- [ ] Typography uses token sizes/weights (no hardcoded px)
- [ ] Reduced-motion respected
