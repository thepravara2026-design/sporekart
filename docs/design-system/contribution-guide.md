# Contribution Guide — Adding New Components

## Naming Conventions

### Component Names

- PascalCase for component names: `Button`, `DatePicker`, `MultiSelect`
- Abbreviations only if universally known: `CTA`, `KPI`, `OTP`, `SKU`
- Avoid acronyms that are not industry standard; prefer full words: `FileUpload` not `FU`

### File Names

- kebab-case for files: `multi-select.tsx`, `date-picker.tsx`, `form-group.tsx`
- When a component has sub-components, use directory + index pattern:
  ```
  date-picker/
  ├── date-picker.tsx
  ├── date-picker-calendar.tsx
  ├── date-picker-input.tsx
  └── index.ts
  ```

### CSS Custom Properties

- Prefix with component namespace: `--button-bg`, `--card-radius`, `--dialog-elevation`
- Reference semantic tokens only, never primitives directly

## File Structure

Every component MUST include these files:

```
component-name/
├── component-name.tsx       # Main component implementation
├── component-name.test.tsx  # Unit tests
├── component-name.css       # Component-specific styles (if needed)
└── index.ts                 # Barrel export
```

### Required Files

| File | Required | Purpose |
|------|----------|---------|
| `component-name.tsx` | Yes | Component implementation with TypeScript |
| `index.ts` | Yes | Re-exports the component with correct typing |
| `component-name.test.tsx` | Yes | Unit and integration tests |
| `component-name.css` | No | Inline styles via design tokens preferred; CSS only for complex animations |

## Required Exports

The component's `index.ts` must export:

```typescript
export { ComponentName } from './component-name';
export type { ComponentNameProps } from './component-name';
```

The component itself must:

```typescript
export interface ComponentNameProps {
  // All props must be typed with JSDoc comments
}

export function ComponentName({ ...props }: ComponentNameProps) {
  // Implementation
}
```

## Token Usage

- Use CSS custom properties from the design token system for ALL visual properties
- Never hardcode colors, spacing, font sizes, or radii
- Accept token overrides via component CSS custom properties
- Document every token the component uses in the manifest entry

```typescript
// Good
const styles = {
  color: 'var(--color-text-primary)',
  padding: 'var(--spacing-md)',
  borderRadius: 'var(--radius-md)',
};

// Bad
const styles = {
  color: '#1D2B22',
  padding: '16px',
  borderRadius: '8px',
};
```

## Testing Requirements

### Unit Tests (Vitest)

| Test Type | Required | Coverage Target |
|-----------|----------|-----------------|
| Render test | Yes | 100% |
| Prop variants | Yes | Each variant |
| State transitions | Yes | All interactive states |
| Accessibility checks | Yes | axe-core 0 violations |
| Keyboard interactions | Yes | Full keyboard spec |
| Edge cases | Yes | Empty, overflow, boundary |

### Integration Tests

| Test | Purpose |
|------|---------|
| Form integration | Component in form context |
| Provider integration | Component in ToastProvider, DialogProvider, etc. |
| Theme switching | Renders correctly in light, dark, high-contrast |

### Visual Regression (Chromatic)

- Every variant and state must have a Chromatic story
- 0 visual regressions allowed on stable components

## PR Checklist

Before submitting a PR for a new component:

### Code Quality
- [ ] Component follows naming conventions
- [ ] All required files exist
- [ ] TypeScript compiles with zero errors
- [ ] ESLint passes with zero errors
- [ ] No hardcoded CSS values — all tokens
- [ ] Component exposes CSS custom property API for overrides
- [ ] Console warnings/errors are handled

### Testing
- [ ] Unit tests cover all variants and states
- [ ] Integration tests cover form/provider/theme contexts
- [ ] axe-core reports 0 violations
- [ ] Keyboard navigation is fully implemented
- [ ] Visual regression stories are added
- [ ] Test coverage ≥ 80%

### Documentation
- [ ] Component entry added to `componentManifest.ts`
- [ ] Doc path exists and is linked
- [ ] Props are documented with JSDoc
- [ ] Usage examples are provided
- [ ] Search index updated in `searchIndex.ts`

### Review
- [ ] Component is added to the playground preview
- [ ] Preview page shows all variants and states
- [ ] Responsive behavior verified at 1280px, 768px, 375px
- [ ] Dark mode verified
- [ ] High-contrast mode verified
- [ ] Bundle size is within budget (logged by CI)

### Final
- [ ] PR title follows Conventional Commits: `feat(component-name): description`
- [ ] Changelog entry is prepared
- [ ] Design review is requested from the Design System Council
- [ ] Component owner is assigned in the manifest
