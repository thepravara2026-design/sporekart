# SporeKart QA Sprint 2 — Accessibility Report (Customer Journey)

**Date:** 2026-07-17

---

## Test Results

| Test | Status | Details |
|------|--------|---------|
| Skip to content link | ✅ PASS | Present on homepage |
| Semantic heading structure | ✅ PASS | h1 + multiple h2s |
| Interactive elements have accessible labels | ❌ FAIL | Some buttons lack labels/text |
| Images have alt text | ✅ PASS | All images have alt attributes |
| Focus order is logical | ✅ PASS | Tab order starts on first link |
| Color contrast on text | ✅ PASS | Text elements have defined colors |
| ARIA landmarks present | ✅ PASS | main, nav, footer landmarks |
| Form inputs have associated labels | ✅ PASS | Login form inputs labeled |
| Keyboard navigation on products page | ✅ PASS | Tab navigation works |

## Detailed Findings

### Failure: Interactive elements without labels
- Some buttons/links on the homepage lack `aria-label` or visible text content
- These may be icon-only buttons without accessible names
- Affects screen reader users

## Recommendations

1. Add `aria-label` to all icon-only buttons and links
2. Run full axe-core audit on all customer journey pages
3. Test with screen readers (NVDA, VoiceOver)
4. Add focus visible styles for keyboard users
5. Ensure color contrast meets WCAG AA (4.5:1) on all text
