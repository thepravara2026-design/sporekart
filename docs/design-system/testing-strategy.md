# Testing Strategy

## Component Testing Approach

Each component is tested at three levels:

| Level | Scope | Tool |
|-------|-------|------|
| **Unit** | Individual component behavior | Vitest + RTL |
| **Integration** | Multi-component interaction | Vitest + RTL |
| **Accessibility** | ARIA roles, keyboard nav, color contrast | jest-axe / axe-core |

## Unit Testing with RTL

```tsx
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { Button } from './Button';

describe('Button', () => {
  it('renders children', () => {
    render(<Button>Click</Button>);
    expect(screen.getByRole('button')).toHaveTextContent('Click');
  });

  it('calls onClick when clicked', async () => {
    const onClick = vi.fn();
    render(<Button onClick={onClick}>Click</Button>);
    await userEvent.click(screen.getByRole('button'));
    expect(onClick).toHaveBeenCalledTimes(1);
  });
});
```

Conventions:
- Test from user perspective (getByRole, getByLabelText, etc.)
- Use `userEvent` over `fireEvent` for realistic interactions
- Test states: default, hover, focus, active, disabled, loading, error
- Test variants: each visual variant renders correctly

## Keyboard Navigation Tests

```tsx
it('supports keyboard navigation', async () => {
  render(<Button>Submit</Button>);
  const button = screen.getByRole('button');
  await userEvent.tab();
  expect(button).toHaveFocus();
  await userEvent.keyboard('{Enter}');
  expect(onClick).toHaveBeenCalled();
});
```

- Verify all interactive elements are reachable via Tab
- Verify Enter/Space triggers the action
- Verify Escape closes overlays
- Verify Arrow keys navigate composite widgets (Tabs, Select, etc.)

## Accessibility (axe-core)

```tsx
import { axe } from 'jest-axe';

it('has no accessibility violations', async () => {
  const { container } = render(<Button>Accessible</Button>);
  const results = await axe(container);
  expect(results).toHaveNoViolations();
});
```

- Run on every component in every variant
- Test with and without ARIA labels
- Test in light, dark, and high-contrast theme contexts
- Color contrast checked against WCAG AA (4.5:1 ratio)

## Visual Regression (Optional)

When visual regression is enabled:

```bash
npm run test:visual     # Runs Playwright screenshot tests
npm run test:visual:update  # Updates reference screenshots
```

- Captures all component variants at multiple breakpoints
- Run in CI on PRs targeting `main`
- Store reference images in `__visual__/` alongside components

## Token Compliance Testing

```tsx
it('uses only design token values', () => {
  const styles = getComputedStyle(render(<Button />).container.firstChild);
  // Assert no hardcoded color, spacing, or radius values
  expect(styles.color).toMatch(/^var\(--/);
});
```

- Lint rule: `no-hardcoded-colors` (ESLint)
- Lint rule: `no-hardcoded-spacing`
- CI checks that all CSS custom properties reference valid tokens

## CI Integration Recommendations

| Check | Tool | When |
|-------|------|------|
| Unit + integration | Vitest | Every commit |
| Accessibility | jest-axe | Every commit |
| Token compliance | ESLint + custom checks | Every commit |
| Visual regression | Playwright | PR to `main` |
| Bundle size impact | size-limit | PR to `main` |
