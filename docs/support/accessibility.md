# Accessibility Compliance Audit

Keyboard controls and screen reader accessibility guidelines.

## Specifications

- **Keyboard Focus**:
  - Forms use explicit tab indices.
  - Interactive stars, buttons, and radio controls are focusable.
- **ARIA Semantics**:
  - Accordion FAQ buttons use `aria-expanded` states.
  - Form fields are associated with correct `<label>` elements.
- **Motion settings**:
  - Chat timelines and ratings hover transitions respect system preferences (`prefers-reduced-motion`).
