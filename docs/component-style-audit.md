# Component Style Audit

**Part of:** Frontend Style-Guide Migration (`docs/frontend-style-migration.md`)
**Audited:** `frontend/web-app/src/design-system` (token-driven component library)
**Method:** static scan for the systemic React anti-pattern + token-wiring check.

---

## A. Systemic defect — CSS-string passed to React `style` prop

React 18 rejects a **string** for the `style` prop and throws:
`The 'style' prop expects a mapping from style properties to values, not a string.`
The DS builds styles as multi-line CSS-string template literals and passes them as
`style={style as React.CSSProperties}`. **30 files** are affected.

| # | File | Notes |
|---|------|-------|
| 1 | composite/Card.tsx | also `&:hover` inside string (needs pseudo handling) |
| 2 | core/Button.tsx | also `cssText +=` focus hack |
| 3 | core/ButtonGroup.tsx | |
| 4 | core/Checkbox.tsx | |
| 5 | core/FloatingActionButton.tsx | |
| 6 | core/Input.tsx | |
| 7 | core/Link.tsx | multiple `&:hover`/`&:focus`/`&:active` in string |
| 8 | core/OtpInput.tsx | |
| 9 | core/Password.tsx | |
| 10 | core/RadioGroup.tsx | |
| 11 | core/Search.tsx | |
| 12 | core/ToggleSwitch.tsx | |
| 13 | display/Avatar.tsx | |
| 14 | feedback/AlertDialog.tsx | |
| 15 | feedback/ConfirmationDialog.tsx | |
| 16 | feedback/Dialog.tsx | |
| 17 | feedback/ErrorDialog.tsx | |
| 18 | feedback/ImageModal.tsx | |
| 19 | feedback/InformationDialog.tsx | |
| 20 | feedback/LoadingDialog.tsx | |
| 21 | feedback/Modal.tsx | |
| 22 | feedback/ResponsiveDialog.tsx | |
| 23 | feedback/ResponsiveModal.tsx | |
| 24 | feedback/ScrollableModal.tsx | |
| 25 | feedback/SuccessDialog.tsx | |
| 26 | feedback/VideoModal.tsx | |
| 27 | feedback/WarningDialog.tsx | |
| 28 | feedback/WizardModal.tsx | |
| 29 | icons/Icon.tsx | |
| 30 | playground/pages/IconLibrary.tsx | (playground only) |

**Fix pattern (reusable):** a `useScopedStyle(cssString)` helper that:
- mints a unique class via `React.useId()`,
- rewrites `&` → `.{class}` (so `&:hover` → `.{class}:hover`),
- renders the rules inside a real `<style>` element,
- returns `{ className, StyleTag }`.
Components then drop `style={...}` and use `className={cls}` (+ their own class).
This preserves tokens, hover/focus, and is non-destructive.

---

## B. Token wiring

| Check | Result |
|-------|--------|
| Canonical token CSS imported in app? | ❌ `main.tsx` imports only prototype `src/styles/global.css` |
| `var(--color-*)` / `--radius-*` / `--space-*` defined? | ✅ in `design-system/styles/global.css` (not loaded) |
| DS components reference tokens (not hardcoded hex)? | ✅ yes — already token-driven |
| Two conflicting `global.css`? | ⚠️ prototype (`--sk-*`) vs canonical (`--color-*`) |

---

## C. Compliance verdict (pre-migration)
- **Token design:** 100% present (JSON + compiled CSS).
- **Token activation:** 0% (not imported → DS unstyled even if it rendered).
- **DS renderability:** broken (string-style crash) on the 30 files above.
- **Active surfaces:** navigation prototype + Sprint 21 public site run on drifted
  `--sk-*` prototype palette, not canonical tokens.

## D. Remediation order
1. Wire token layer (B) — Phase A.
2. Fix the 3 live components (Button/Card/FeatureCard) + repoint homepage — Phase A.
3. Fix remaining 27 DS files (incl. `&` pseudo handling) — Phase B.
4. Reconcile prototype `global.css` + align surfaces + sibling apps — Phase C.
