# Authentication — Accessibility

**Sprint 21 · Part 7** · Target: WCAG 2.2 AA

## Structure & semantics
- `AuthLayout` renders the form inside `<main>`; the brand panel is `aria-hidden` (decorative).
- Single `<h1>` per screen; headings follow a logical outline.
- Forms use native `<form>`, `type="submit"`/`type="button"`, and real `<label>` associations.

## Labels & instructions
- Every `Input`/`OtpInput` has a visible label and programmatic association.
- Helper text and errors are linked via `aria-describedby`.
- OTP uses `role="group"` with `aria-label` and per-digit `aria-label` ("Digit 1"…).

## Errors & status
- Field errors: `aria-invalid` + `aria-describedby` → error message.
- Form-level errors: `AuthAlert` with `role="alert"`.
- Full-page states (`StatusScreen`) announce title as `<h1>`; alerts use `role="alert"`.

## Keyboard & focus
- Full keyboard operability; no `tabindex` traps.
- Visible focus ring via `--color-focus-ring` (`focus-visible`).
- OTP: auto-advance, Backspace-to-clear, Arrow Left/Right, Home/End, paste-to-fill; focus returns to first digit on error.
- Channel switch (phone/email) is a `role="radiogroup"` with `aria-checked` and arrow/click selection.
- On validation failure, focus moves to the first invalid field.

## Motion & contrast
- `prefers-reduced-motion` disables spinners/transitions (auth.css).
- Colors use the semantic token palette validated for AA contrast; error/warning states pair color with icon + text (never color alone).
- Touch targets meet minimum size via `--input-height-*` / button sizing tokens.

## Verification
- Manual keyboard pass (Tab/Shift+Tab, Enter/Space, Arrow keys in OTP + radios).
- Screen-reader smoke test (VoiceOver/NVDA): heading order, label announcement, alert announcement.
- `npm run build` ensures no broken ARIA wiring at compile time.
