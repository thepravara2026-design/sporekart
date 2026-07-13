# Accessibility Checklist — Sprint 20 Part 2

> **Target:** WCAG 2.2 AA for all components

## Focus Management

- [ ] All interactive elements have a visible focus indicator (min 3px ring, 2px offset)
- [ ] `:focus-visible` used — focus ring only appears on keyboard navigation
- [ ] `outline: none` is never set without a visible replacement
- [ ] Focus order matches visual DOM order
- [ ] No focus traps (except modals/dialogs with proper management)
- [ ] Roving tabindex implemented for radio groups, button groups, and tab panels

## Keyboard Navigation

- [ ] All functionality operable via keyboard alone
- [ ] Tab / Shift+Tab moves between groups
- [ ] Arrow keys navigate within groups (radio group, button group, OTP)
- [ ] Enter / Space activates buttons, checkboxes, toggles
- [ ] Escape closes overlays, clears search
- [ ] Home / End navigates to first/last item in a group
- [ ] No single-key shortcuts without modifier (except search fields)

## ARIA Roles and Attributes

- [ ] All custom interactive components have correct `role`
- [ ] `aria-checked` on checkboxes (`true` / `false` / `mixed`) and switches
- [ ] `aria-disabled` on disabled elements
- [ ] `aria-required` on required form fields
- [ ] `aria-invalid` on invalid form fields
- [ ] `aria-describedby` linking to hints and error messages
- [ ] `aria-label` or `aria-labelledby` on icon-only buttons
- [ ] `aria-busy` on loading states
- [ ] `aria-expanded` on expandable controls
- [ ] `aria-pressed` on toggle buttons (including password visibility)
- [ ] `role="alert"` on error messages
- [ ] `role="status"` / `aria-live="polite"` on dynamic status updates
- [ ] `role="radiogroup"` on radio groups
- [ ] `role="group"` on button groups and checkbox groups
- [ ] `role="switch"` on toggle switches
- [ ] `role="img"` on semantic icons
- [ ] `aria-hidden="true"` on decorative icons

## Color Contrast

- [ ] Body text ≥ 4.5:1 against background
- [ ] Large text (≥18px / ≥14px bold) ≥ 3:1
- [ ] UI components (borders, icons) ≥ 3:1
- [ ] Focus ring ≥ 3:1 against adjacent colors
- [ ] Error / success states not conveyed by color alone — always include icon or text
- [ ] Disabled states maintain sufficient contrast (opacity alone is insufficient)

## Screen Reader Support

- [ ] All form inputs have associated labels
- [ ] Error messages announced automatically (live region or role="alert")
- [ ] Dynamic content changes announced via `aria-live`
- [ ] Status messages (loading, success) announced
- [ ] Page title is unique and descriptive
- [ ] Heading hierarchy is logical (no skips)
- [ ] Decorative images have `alt=""` (or are hidden via `aria-hidden`)
- [ ] Informative icons have descriptive `aria-label`
- [ ] Custom controls announce their role, state, and name

## Reduced Motion

- [ ] `prefers-reduced-motion` respected on all transitions and animations
- [ ] Loading spinners replace shimmer animations under reduced motion
- [ ] All animations < 100ms duration when reduced motion preferred
- [ ] No auto-playing content > 5 seconds without user control

## Testing Procedures

### Automated
- [ ] `axe-core` scan — 0 violations at AA level
- [ ] `eslint-plugin-jsx-a11y` — errors enforced in CI
- [ ] Color contrast check via `@axe-core/react` or `puppeteer`

### Manual
- [ ] Full keyboard navigation (Tab through entire flow)
- [ ] NVDA screen reader (Windows) — all components navigable
- [ ] VoiceOver (macOS/iOS) — all components navigable
- [ ] Browser zoom to 200% — no content loss or horizontal scroll
- [ ] Dark mode / high contrast mode — all text readable
- [ ] Touch target sizes ≥ 44×44px (WCAG 2.2)
