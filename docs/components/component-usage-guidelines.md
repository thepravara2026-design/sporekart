# Component Usage Guidelines

> **Status:** v1.0 — General guidelines for Sprint 20 Part 2 component usage

## General Principles

- Every interactive element must be keyboard accessible
- Use semantic HTML where possible; ARIA only where necessary
- All components consume design tokens — no hardcoded values
- Components must support `className` prop for overrides
- All components must accept a `ref` via `forwardRef`

## Button vs Link

| Use Button when… | Use Link when… |
|------------------|----------------|
| Triggers an action (submit, save, delete) | Navigates to a URL or route |
| Does not change the URL | Changes the URL or opens a new tab |
| Inside a form as submit | Inside body text or paragraph |
| Opens a modal or drawer | Provides a reference or footnote |
| Performs an async operation | Downloads a file (with `download`) |

## Button Hierarchy

- **One primary per page/section** — guides user to the main action
- **All other actions are secondary, outline, or ghost**
- Destructive actions should be `destructive` variant with a confirmation step
- Never stack two `primary` buttons adjacent — use `primary` + `secondary`
- Loading state must be used for async operations (prevents double-submit)

## Input Validation Best Practices

- Validate on blur (not on every keystroke) for performance
- Show inline error below the input with `role="alert"`
- Never hide error messages — they must be seen and announced
- Success state only after async server validation (not on client-side formatting)
- Use `aria-describedby` to associate errors and hints
- Prevent form submission until all fields validate

## Form Layout Conventions

- Single-column layouts preferred (WCAG SC 1.3.2)
- Labels stacked above inputs (not inline)
- Related fields grouped in `<fieldset>` with `<legend>`
- Submit button left-aligned, not centered
- Cancel links (not buttons) placed beside Submit
- Required indicator (`*`) shown, optional marked with "(optional)"

## Icon Usage Guidelines

- Decorative icons: no `aria-label`, `aria-hidden="true"`, `focusable="false"`
- Semantic icons: `aria-label` describing the icon's meaning
- Icons accompanying text are decorative
- Icon-only buttons must have `aria-label`
- Maintain consistent icon sizes per component variant
- Register all custom icons via the registry system

## Naming Conventions

| Pattern | Example |
|---------|---------|
| `PascalCase` for components | `Button`, `Input`, `CheckboxGroup` |
| `camelCase` for props and handlers | `onChange`, `isLoading` |
| `kebab-case` for CSS classes | `btn-primary`, `input--error` |
| `--token-name` for CSS custom properties | `--color-bg-primary` |

## Import Conventions

```tsx
// Named imports from the package
import { Button, Input, Checkbox } from '@sporekart/ui';

// Single component import (tree-shakeable)
import { Button } from '@sporekart/ui/button';

// Icons
import { Icon, registerIcon } from '@sporekart/icons';
import { plusSVG, searchSVG } from '@sporekart/icons/generated';
registerIcon('plus', plusSVG);
registerIcon('search', searchSVG);

// Tokens
import { tokens } from '@sporekart/tokens';
```
