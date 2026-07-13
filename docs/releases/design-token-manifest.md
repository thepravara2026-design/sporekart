# Design Token Manifest — v1.0.0 (Frozen)

> **Status**: FROZEN  
> **Version**: 1.0.0  
> **Freeze Date**: 2026-07-13  

All design tokens in this manifest are frozen as of v1.0.0. No changes are permitted without going through the formal governance process.

---

## Token Governance

Changes to any frozen token **must** follow this workflow:

```
RFC Document → Review → Approval → Version Bump → Migration Guide
```

- **All** tokens frozen in v1.0.0.
- Patch and minor releases may **add** new tokens but may **not** alter, rename, or remove existing ones.
- Major releases may break tokens but **must** include a migration guide.
- Every change requires sign-off from the Design System Architect.

---

## Color Tokens

All color tokens use semantic naming. Primitives are mapped at the theme level; components **must** reference semantic tokens only.

| Group | Token Group | Purpose | Example Values |
|-------|-------------|---------|----------------|
| Primary | `--color-primary-*` | Brand actions, links, focus | `--color-primary-500: #3B82F6` |
| Neutral | `--color-neutral-*` | Backgrounds, text, borders | `--color-neutral-900: #111827` |
| Success | `--color-success-*` | Positive feedback, confirmations | `--color-success-500: #22C55E` |
| Warning | `--color-warning-*` | Caution, non-critical alerts | `--color-warning-500: #F59E0B` |
| Danger | `--color-danger-*` | Errors, destructive actions | `--color-danger-500: #EF4444` |
| Info | `--color-info-*` | Informational banners, tooltips | `--color-info-500: #3B82F6` |

Each group exposes a 50–900 scale (50, 100, 200, 300, 400, 500, 600, 700, 800, 900) plus `-contrast` variants for text-on-color accessibility.

---

## Typography Tokens

| Category | Tokens | Example Values |
|----------|--------|----------------|
| Font Size | `--font-size-xs`, `--font-size-sm`, `--font-size-base`, `--font-size-lg`, `--font-size-xl`, `--font-size-2xl`, `--font-size-3xl`, `--font-size-4xl` | `xs: 0.75rem`, `base: 1rem`, `4xl: 2.25rem` |
| Font Weight | `--font-weight-light`, `--font-weight-normal`, `--font-weight-medium`, `--font-weight-semibold`, `--font-weight-bold`, `--font-weight-black` | `light: 300`, `normal: 400`, `black: 900` |
| Line Height | `--line-height-tight`, `--line-height-normal`, `--line-height-relaxed`, `--line-height-loose` | `tight: 1.25`, `loose: 1.75` |
| Font Family | `--font-family-sans`, `--font-family-mono` | `sans: 'Inter', system-ui, sans-serif` |

---

## Spacing Tokens

| Token | Rem | Pixels (16px base) |
|-------|-----|--------------------|
| `--spacing-xs` | 0.25rem | 4px |
| `--spacing-sm` | 0.5rem | 8px |
| `--spacing-md` | 1rem | 16px |
| `--spacing-lg` | 1.5rem | 24px |
| `--spacing-xl` | 2rem | 32px |
| `--spacing-2xl` | 3rem | 48px |
| `--spacing-3xl` | 4rem | 64px |

---

## Radius Tokens

| Token | Value |
|-------|-------|
| `--radius-sm` | 4px |
| `--radius-md` | 6px |
| `--radius-lg` | 8px |
| `--radius-xl` | 12px |
| `--radius-full` | 9999px |

---

## Elevation Tokens

| Token | Shadow Value |
|-------|--------------|
| `--elevation-1` | `0 1px 2px rgba(0,0,0,0.05)` |
| `--elevation-2` | `0 1px 3px rgba(0,0,0,0.1), 0 1px 2px rgba(0,0,0,0.06)` |
| `--elevation-3` | `0 4px 6px rgba(0,0,0,0.1), 0 2px 4px rgba(0,0,0,0.06)` |
| `--elevation-4` | `0 10px 15px rgba(0,0,0,0.1), 0 4px 6px rgba(0,0,0,0.05)` |
| `--elevation-5` | `0 20px 25px rgba(0,0,0,0.1), 0 10px 10px rgba(0,0,0,0.04)` |

---

## Animation Tokens

| Token | Value |
|-------|-------|
| `--transition-fast` | 150ms ease |
| `--transition-normal` | 250ms ease |
| `--transition-slow` | 400ms ease |
| `--duration-100` | 100ms |
| `--duration-200` | 200ms |
| `--duration-300` | 300ms |
| `--duration-500` | 500ms |
| `--duration-700` | 700ms |
| `--duration-1000` | 1000ms |
| `--easing-in` | cubic-bezier(0.4, 0, 1, 1) |
| `--easing-out` | cubic-bezier(0, 0, 0.2, 1) |
| `--easing-in-out` | cubic-bezier(0.4, 0, 0.2, 1) |

---

## Breakpoint Tokens

| Token | Viewport Width |
|-------|----------------|
| `--breakpoint-mobile` | 375px |
| `--breakpoint-tablet` | 768px |
| `--breakpoint-laptop` | 1024px |
| `--breakpoint-desktop` | 1280px+ |

---

## Token Usage Rules

1. **NEVER** hardcode colors, spacing, typography, shadows, radius, or transitions.
2. **ALWAYS** use `var(--token-name)` via CSS custom properties.
3. **ALWAYS** cast inline styles as `React.CSSProperties`.
4. Use semantic tokens over primitive tokens where possible.

### Violation Consequences

Any component submitted for review that contains hardcoded values will be **rejected immediately**. The author must fix all violations before resubmission. Repeated violations may result in escalated review requirements.
