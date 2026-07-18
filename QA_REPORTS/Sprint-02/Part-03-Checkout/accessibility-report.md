# SporeKart QA Sprint 2 — Accessibility Report (Checkout)

**Date:** 2026-07-17  
**Scope:** Skip-to-content, ARIA landmarks, image alt text  

---

## Test Results

| Test | Chromium | WebKit | Mobile Chrome | Mobile Safari | Status |
|------|----------|--------|---------------|---------------|--------|
| Skip to Content Link | PASS | PASS | PASS | PASS | PASS |
| ARIA Landmarks | PASS | PASS | DEFECT | DEFECT | DEFECT |
| Images Have Alt Text | PASS | PASS | PASS | PASS | PASS |

**Total: 12 executions, 8 pass, 4 fail**

---

## Detailed Findings

### Skip to Content Link
- **Status:** PASS (all browsers)
- Skip-to-content link present and visible on homepage
- Links to `#main` or `#main-content`
- Consistent across all viewports

### ARIA Landmarks
- **Desktop (Chromium, WebKit):** PASS
  - `<main>`, `<nav>`, `<footer>` landmarks present and visible
- **Mobile (Chrome, Safari):** DEFECT
  - Navigation `<nav aria-label="Primary">` is present but **hidden** on mobile viewport
  - Locator resolves to element but CSS visibility is `hidden`
  - Likely a responsive design issue — mobile nav collapses but is still rendered in DOM

### Image Alt Text
- **Status:** PASS (all browsers)
- All `<img>` elements have non-null alt attributes
- Zero missing alt text instances

---

## Bug Reference

See `bug-register.md` for BUG-CHK-002 (ARIA nav landmark hidden on mobile).

## Known Legacy Issues (From Part 1, Not Duplicated)

| Issue | Reference | Status |
|-------|-----------|--------|
| LoginPage WCAG violations (BUG-009) | Master Bug Register | Open |
| RegisterPage WCAG violation (BUG-010) | Master Bug Register | Open |
| Keyboard tab order (BUG-011) | Master Bug Register | Open |
| Interactive elements lack labels (BUG-012) | Master Bug Register | Open |

---

## Recommendations

1. Fix mobile navigation to be visible/accessible to screen readers
2. Replace CSS `visibility: hidden` with conditional rendering or `aria-hidden`
3. Run full axe-core audit on checkout pages when implemented
4. Address legacy WCAG violations from Part 1 (BUG-009 through BUG-012)
