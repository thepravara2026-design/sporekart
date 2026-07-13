# Theme Foundation — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1D): Enterprise Design Language & Brand Guidelines

> **Status:** Light theme fully specified. Dark/High-Contrast/Brand theme architecture defined for future implementation.

---

## 1. Theme Architecture

| Layer | Responsibility |
|-------|----------------|
| **Primitives** | Raw values (hex, px, ms) — never change per theme |
| **Semantic Tokens** | Meaningful aliases — **theme-specific overrides here** |
| **Component Tokens** | Direct component mappings — reference semantic |
| **CSS Custom Properties** | Runtime injection via `:root[data-theme="..."]` |

---

## 2. Theme Variants

| Theme | Identifier | Status | Use Case |
|-------|------------|--------|----------|
| **Light** | `light` (default) | **Implemented** | Default, all users |
| **Dark** | `dark` | Architecture only | Low-light, OLED, preference |
| **High Contrast** | `high-contrast` | Architecture only | Accessibility, forced colors |
| **Brand (Partner)** | `brand-{partner}` | Architecture only | White-label deployments |

---

## 3. Light Theme (Reference Implementation)

All semantic tokens resolve to light values by default. See `color-system.md`, `typography.md`, `spacing-system.md`, `elevation-system.md` for full token maps.

### Key Light Values

| Semantic Token | Value | Primitive |
|----------------|-------|-----------|
| `color-background` | `#F7F8F7` | `neutral-100` |
| `color-surface` | `#FFFFFF` | `white` |
| `color-surface-raised` | `#FFFFFF` | `white` |
| `color-border` | `#E3E6E3` | `neutral-200` |
| `color-text-primary` | `#1D2B22` | `neutral-900` |
| `color-text-secondary` | `#6D6D6D` | `neutral-600` |
| `color-primary` | `#2F6F4F` | `green-600` |
| `color-primary-hover` | `#265D3F` | `green-700` |
| `color-focus-ring` | `#2F6F4F` | `green-600` |
| `elevation-1` | `0 1px 2px rgba(29,43,34,0.08)` | — |
| `elevation-2` | `0 4px 8px rgba(29,43,34,0.12)` | — |

---

## 4. Dark Theme Specification (Future)

### 4.1 Design Principles

| Principle | Implementation |
|-----------|----------------|
| **Surface Hierarchy** | Darker bg → lighter surfaces (opposite of light) |
| **Contrast** | Maintain 4.5:1 minimum on all text |
| **Primary Color** | Lighten (green-400) for dark backgrounds |
| **Shadows** | Darker, more opaque (surface tint + shadow) |
| **Images** | No inversion — use `filter: brightness(0.9)` for photos |

### 4.2 Dark Semantic Overrides

| Token | Light | Dark |
|-------|-------|------|
| `color-background` | `neutral-100` | `neutral-950` (`#0D140E`) |
| `color-surface` | `white` | `neutral-900` (`#1D2B22`) |
| `color-surface-raised` | `white` | `neutral-800` (`#2D3D30`) |
| `color-border` | `neutral-200` | `neutral-700` (`#4D4D4D`) |
| `color-text-primary` | `neutral-900` | `neutral-50` (`#FAFAFA`) |
| `color-text-secondary` | `neutral-600` | `neutral-400` (`#A8A8A8`) |
| `color-text-disabled` | `neutral-400` | `neutral-600` (`#8C8C8C`) |
| `color-primary` | `green-600` | `green-400` (`#66BB6A`) |
| `color-primary-hover` | `green-700` | `green-300` (`#81C784`) |
| `color-focus-ring` | `green-600` | `green-400` |
| `color-skeleton-base` | `neutral-200` | `neutral-800` |
| `color-skeleton-highlight` | `neutral-100` | `neutral-700` |
| `elevation-1` | `rgba(29,43,34,0.08)` | `rgba(0,0,0,0.4)` |
| `elevation-2` | `rgba(29,43,34,0.12)` | `rgba(0,0,0,0.5)` |

### 4.3 Component Adjustments (Dark)

| Component | Change |
|-----------|--------|
| **Button Primary** | Text: `neutral-950` (dark on light green) |
| **Input** | Bg: `neutral-800`, Border: `neutral-700`, Placeholder: `neutral-500` |
| **Table** | Alt row: `neutral-900` / `neutral-800`, Hover: `neutral-800` |
| **Toast** | Bg: `neutral-800`, Border: `neutral-700` |
| **Modal/Dialog** | Overlay: `rgba(0,0,0,0.7)` |
| **Skeleton** | Base: `neutral-800`, Highlight: `neutral-700` |

---

## 5. High Contrast Theme (Future)

### 5.1 Principles
- Use **system colors** exclusively (`Canvas`, `CanvasText`, `Highlight`, `ButtonFace`, etc.)
- No custom hex — rely on `forced-colors: active` media query
- Focus: 3px solid `CanvasText`
- Borders: 2px solid `CanvasText`
- No shadows, no gradients, no background images

### 5.2 Token Mapping (CSS-only)

```css
@media (forced-colors: active) {
  :root {
    --color-background: Canvas;
    --color-surface: Canvas;
    --color-text-primary: CanvasText;
    --color-text-secondary: CanvasText;
    --color-border: CanvasText;
    --color-primary: Highlight;
    --color-focus-ring: CanvasText;
    --color-danger: #FF0000; /* system red */
    --color-success: #00FF00; /* system green */
  }
  .btn-primary { background: Highlight; color: HighlightText; }
  .btn-secondary { background: ButtonFace; color: ButtonText; border: 2px solid ButtonText; }
  .input { background: Canvas; color: CanvasText; border: 2px solid CanvasText; }
}
```

---

## 6. Brand Theme (Partner White-Label) — Future

| Customizable | Token Prefix | Example |
|--------------|--------------|---------|
| Primary Hue | `color-primary` | Partner green/blue/orange |
| Logo | `asset-logo-primary` | SVG URL |
| Favicon | `asset-favicon` | ICO URL |
| Font | `font-family-brand` | Custom font stack |
| Border Radius | `radius-brand` | Partner preference |

**Architecture:** Separate token file `tokens/brand-{partner}.json` extends base, overrides only brand tokens. Build produces separate CSS bundle.

---

## 7. Theme Switching Implementation

### 7.1 CSS Strategy

```css
/* Base (Light) */
:root { --color-background: var(--color-neutral-100); ... }

/* Dark */
[data-theme="dark"] {
  --color-background: var(--color-neutral-950);
  --color-surface: var(--color-neutral-900);
  ...
}

/* High Contrast (CSS-only, no JS) */
@media (forced-colors: active) {
  :root { --color-background: Canvas; ... }
}
```

### 7.2 React Context (Future)

```tsx
const ThemeContext = createContext<'light' | 'dark' | 'system'>('system');

function ThemeProvider({ children }) {
  const [theme, setTheme] = useState('system');
  useEffect(() => {
    const root = document.documentElement;
    if (theme === 'system') {
      const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
      root.dataset.theme = prefersDark ? 'dark' : 'light';
    } else {
      root.dataset.theme = theme;
    }
  }, [theme]);
  return <ThemeContext.Provider value={{ theme, setTheme }}>{children}</ThemeContext.Provider>;
}
```

### 7.3 Persistence
- `localStorage.setItem('theme', theme)`
- Respect `prefers-color-scheme` when `system`
- Sync across tabs via `storage` event

---

## 8. Token Fallback Chain

```
Component Token
    ↓
Semantic Token (Theme-specific)
    ↓
Semantic Token (Base/Light default)
    ↓
Primitive Token
```

**Rule:** Every semantic token MUST have a base (light) value. Dark/HC only override.

---

## 9. Testing Matrix

| Test | Light | Dark | High Contrast |
|------|-------|------|---------------|
| Text contrast (body) | ✅ | ✅ | ✅ (system) |
| Focus ring visibility | ✅ | ✅ | ✅ (3px) |
| Border visibility | ✅ | ✅ | ✅ (2px) |
| Primary button contrast | ✅ | ✅ | ✅ |
| Skeleton visibility | ✅ | ✅ | N/A |
| Image rendering | Normal | `brightness(0.9)` | Normal |
| Shadow perception | Subtle | Stronger | None |