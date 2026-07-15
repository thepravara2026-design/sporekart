# Accessibility Compliance Audit

Audited against WCAG 2.2 AA standards.

## Audit Logs

- **Keyboard Navigation**:
  - Filter tabs support Tab focus and arrow key tab switching.
  - Form items, checkboxes, buttons, and links are accessible via keyboard (`Enter`/`Space`).
  - Focus outlines utilize `--color-focus-ring` token.
- **ARIA & Semantics**:
  - Milestones lists use semantic ordered lists.
  - Interactive elements have accessible names and appropriate ARIA attributes.
  - SVG map is hidden from screen readers (`aria-hidden="true"`).
- **Motion Options**:
  - Pulsing double circles on the SVG map respect `prefers-reduced-motion` settings.
  - Shimmer loaders pause when reduced motion is requested.
