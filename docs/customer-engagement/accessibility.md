# Accessibility Compliance Audit

This workspace meets WCAG 2.2 AA standards.

## Audit Logs

- **Keyboard Navigation**:
  - Sub-navigation tab bars support `aria-selected` toggling and key controls.
  - Buttons and inputs have focus outline indicators.
  - Interactive elements have explicit text labels or `aria-label` properties.
- **Color Contrast**:
  - Text colors, status labels, and alerts meet WCAG AA requirements (4.5:1 contrast ratio).
- **Semantics**:
  - Interactive components implement ARIA roles (e.g. `role="tablist"`, `role="tab"`).
  - Unread indicators use readable markup.
