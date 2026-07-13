# Design Tokens Architecture — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1D): Enterprise Design Language & Brand Guidelines

> **Status:** Mandatory token architecture. Single source of truth. All components consume ONLY tokens. Hardcoded values prohibited.

---

## 1. Token Philosophy

1. **Centralized** — One repository: `src/tokens/`
2. **Platform-Agnostic** — JSON format (Style Dictionary / Token Studio compatible)
3. **Semantic** — Primitives → Semantic → Component layers
4. **Typed** — TypeScript definitions auto-generated from JSON
5. **Versioned** — Semantic versioning; breaking changes = major
6. **Documented** — Every token has description, example, a11y note

---

## 2. Token Hierarchy

```
Primitive (Raw Values)
    ↓
Semantic (Meaning)
    ↓
Component (Usage)
```

| Layer | Example | Consumers |
|-------|---------|-----------|
| **Primitive** | `green-600: "#2F6F4F"` | Never used directly |
| **Semantic** | `color-primary: "{green.600}"` | Theme builders, global styles |
| **Component** | `button-primary-bg: "{color.primary}"` | Component libraries |

---

## 3. Token Categories & Structure

```
tokens/
├── color.json           # All color primitives + semantic
├── typography.json      # Font families, sizes, weights, line heights
├── spacing.json         # Spacing scale, padding, margin, layout
├── radius.json          # Border radius tokens
├── elevation.json       # Shadows, surfaces, z-index
├── breakpoints.json     # Breakpoint tokens
├── z-index.json         # Z-index tokens
├── opacity.json         # Opacity tokens
├── animation.json       # Duration, easing, motion tokens
├── border.json          # Border width, style, color tokens
├── sizing.json          # Width, height, icon, illustration tokens
├── index.json           # Combined export + metadata
```

---

## 4. Token Naming Convention (CTI-Inspired)

```
{category}.{property}.{variant}.{state}.{scale}
```

| Part | Values | Example |
|------|--------|---------|
| **category** | `color`, `typography`, `spacing`, `radius`, `elevation`, `border`, `opacity`, `animation`, `breakpoint`, `zIndex`, `sizing` | `color` |
| **property** | `bg`, `text`, `border`, `shadow`, `fontFamily`, `fontSize`, `fontWeight`, `lineHeight`, `letterSpacing`, `duration`, `easing`, `width`, `height`, `icon`, `illustration` | `bg` |
| **variant** | `primary`, `secondary`, `success`, `warning`, `danger`, `info`, `neutral`, `surface`, `overlay`, `focus`, `skeleton` | `primary` |
| **state** | `default`, `hover`, `pressed`, `focus`, `disabled`, `selected`, `active`, `error`, `loading` | `hover` |
| **scale** | `xs`, `sm`, `md`, `lg`, `xl`, `2xl`, `3xl`, `none`, `auto` | `md` |

### Examples

| Token | Resolves To |
|-------|-------------|
| `color.bg.primary.default` | `#2F6F4F` |
| `color.bg.primary.hover` | `#265D3F` |
| `color.text.on-primary.default` | `#FFFFFF` |
| `spacing.padding.md` | `16px` |
| `radius.card.default` | `12px` |
| `elevation.card.hover` | `0 1px 2px...` |
| `typography.fontSize.body.md` | `1rem` |
| `animation.duration.fast` | `150ms` |
| `breakpoint.md` | `768px` |
| `zIndex.modal` | `210` |
| `sizing.icon.md` | `24px` |

---

## 5. JSON Format (Token Studio / Style Dictionary Compatible)

```json
{
  "color": {
    "green": {
      "50": { "value": "#E8F5E9", "type": "color", "description": "Lightest green" },
      "100": { "value": "#C8E6C9", "type": "color" },
      "600": { "value": "#2F6F4F", "type": "color", "description": "Primary brand" },
      "700": { "value": "#265D3F", "type": "color" }
    },
    "neutral": {
      "100": { "value": "#F7F8F7", "type": "color" },
      "900": { "value": "#1D2B22", "type": "color" }
    },
    "bg": {
      "primary": {
        "default": { "value": "{color.green.600}", "type": "color" },
        "hover": { "value": "{color.green.700}", "type": "color" }
      }
    }
  },
  "spacing": {
    "base": { "value": "4px", "type": "dimension" },
    "scale": {
      "xs": { "value": "{spacing.base}", "type": "dimension" },
      "sm": { "value": "calc({spacing.base} * 2)", "type": "dimension" },
      "md": { "value": "calc({spacing.base} * 4)", "type": "dimension" }
    },
    "padding": {
      "md": { "value": "{spacing.scale.md}", "type": "dimension" }
    }
  }
}
```

---

## 6. TypeScript Generation

```ts
// Auto-generated from JSON via build script
export interface Tokens {
  color: {
    green: { 50: string; 100: string; 600: string; 700: string };
    neutral: { 100: string; 900: string };
    bg: {
      primary: { default: string; hover: string };
    };
  };
  spacing: {
    base: string;
    scale: { xs: string; sm: string; md: string };
    padding: { md: string };
  };
  // ...
}
```

**Usage in Components:**
```tsx
import { tokens } from '@sporekart/tokens';

const styles = {
  backgroundColor: tokens.color.bg.primary.default,
  padding: tokens.spacing.padding.md,
};
```

---

## 7. CSS Custom Properties Output

Build step generates `:root` CSS vars:

```css
:root {
  --color-green-600: #2F6F4F;
  --color-bg-primary-default: #2F6F4F;
  --color-bg-primary-hover: #265D3F;
  --spacing-base: 4px;
  --spacing-padding-md: 16px;
  --radius-card: 12px;
  --elevation-1: 0 1px 2px...;
  --z-modal: 210;
  --duration-fast: 150ms;
}
```

**Components use CSS vars directly:**
```css
.btn-primary {
  background: var(--color-bg-primary-default);
  padding: var(--spacing-padding-md);
  border-radius: var(--radius-btn);
}
```

---

## 8. Theme Architecture

| Theme | File | Overrides |
|-------|------|-----------|
| **Light (Default)** | `tokens/light.json` | Base values |
| **Dark** | `tokens/dark.json` | `--color-bg`, `--color-surface`, `--color-text-*`, `--color-primary` lighter |
| **High Contrast** | `tokens/high-contrast.json` | System colors, 3px focus |
| **Brand (Partner)** | `tokens/brand-{name}.json` | Primary hue, logo |

**Switching:** Data attribute on `<html>`: `<html data-theme="dark">`

---

## 9. Build Pipeline

| Step | Tool | Output |
|------|------|--------|
| 1. Validate JSON | `style-dictionary` / custom | Error if invalid |
| 2. Generate TS types | `token-transformer` | `tokens.d.ts` |
| 3. Generate CSS vars | `style-dictionary` | `tokens.css` |
| 4. Generate SCSS map | `style-dictionary` | `_tokens.scss` |
| 5. Publish npm | `npm pack` | `@sporekart/tokens` |

---

## 10. Governance

- **Immutable** — Tokens never change without version bump
- **Reviewed** — Design token changes = Gate 2 (Visual Design) review
- **Documented** — Each token has `description`, `example`, `a11y`
- **Tested** — Snapshot tests for generated outputs
- **Versioned** — `@sporekart/tokens@1.x.x` — breaking = major