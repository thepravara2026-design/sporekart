# Spacing System — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1D): Enterprise Design Language & Brand Guidelines

> **Status:** Mandatory spatial rhythm. All layout, padding, margin, gaps derive from this 4px base grid.

---

## 1. Design Principles

1. **4px Base Unit** — All spacing = multiples of 4px (0.25rem). No arbitrary pixels.
2. **Semantic Naming** — Tokens describe purpose, not value (e.g., `space-component-gap`, not `space-16`).
3. **Responsive Scaling** — Space scales at breakpoints via clamp() or discrete steps.
4. **Consistent Rhythm** — Vertical flow follows baseline grid (4px); horizontal follows 4px.
4. **Component Isolation** — Components define internal spacing; layout controls external.

---

## 2. Primitive Scale (4px Base)

| Step | Token | rem | px | Usage |
|------|-------|-----|-----|-------|
| 0 | `space-0` | 0 | 0 | Reset, inline |
| 1 | `space-1` | 0.25rem | 4px | Micro gaps, icon-text |
| 2 | `space-2` | 0.5rem | 8px | Tight component internal |
| 3 | `space-3` | 0.75rem | 12px | Standard component internal |
| **4** | **`space-4`** | **1rem** | **16px** | **Base unit — default padding, gap** |
| 5 | `space-5` | 1.25rem | 20px | Generous internal |
| 6 | `space-6` | 1.5rem | 24px | Section spacing, card gap |
| 7 | `space-7` | 1.75rem | 28px | — |
| 8 | `space-8` | 2rem | 32px | Page section gap |
| 10 | `space-10` | 2.5rem | 40px | Major section gap |
| 12 | `space-12` | 3rem | 48px | Page-level separation |
| 16 | `space-16` | 4rem | 64px | Hero, landing |

---

## 3. Semantic Space Tokens (Component Consumption)

### 3.1 Layout Spacing
| Token | Primitive | Value | Usage |
|-------|-----------|-------|-------|
| `space-page-padding-x` | `space-4` / `space-5` / `space-6` | 16/20/24px | Page horizontal padding (responsive) |
| `space-page-padding-y` | `space-6` | 24px | Page vertical padding |
| `space-section-gap` | `space-8` | 32px | Between major sections |
| `space-component-gap` | `space-4` | 16px | Between sibling components |
| `space-group-gap` | `space-6` | 24px | Between form groups |

### 3.2 Component Internal
| Token | Primitive | Value | Usage |
|-------|-----------|-------|-------|
| `space-xs` | `space-1` | 4px | Icon + label, badge internal |
| `space-sm` | `space-2` | 8px | Button padding-x, input padding-x |
| `space-md` | `space-3` | 12px | Card padding, dropdown item |
| `space-lg` | `space-4` | 16px | Modal padding, panel padding |
| `space-xl` | `space-6` | 24px | Dialog padding, page header |

### 3.3 Inline / Stack
| Token | Primitive | Value | Usage |
|-------|-----------|-------|-------|
| `space-inline-xs` | `space-1` | 4px | Icon + text inline |
| `space-inline-sm` | `space-2` | 8px | Button group, chip gap |
| `space-inline-md` | `space-3` | 12px | Form field + hint |
| `space-stack-xs` | `space-2` | 8px | Stacked items tight |
| `space-stack-sm` | `space-3` | 12px | Stacked form fields |
| `space-stack-md` | `space-4` | 16px | Default vertical stack |
| `space-stack-lg` | `space-6` | 24px | Section stack |

### 3.4 Layout Specific
| Token | Primitive | Value | Usage |
|-------|-----------|-------|-------|
| `space-header-height` | — | 56px | Fixed header height |
| `space-sidebar-width` | — | 264px | Desktop sidebar |
| `space-sidebar-icon-width` | — | 72px | Collapsed sidebar |
| `space-breadcrumb-gap` | `space-2` | 8px | Breadcrumb separator gap |
| `space-tab-gap` | `space-1` | 4px | Tab list gap |

---

## 4. Responsive Scaling

```css
/* Page padding fluid */
--space-page-padding-x: clamp(1rem, 1rem + 0.5vw, 1.5rem); /* 16–24px */
--space-page-padding-y: 1.5rem; /* 24px fixed */

/* Section gap */
--space-section-gap: clamp(1.5rem, 1.5rem + 0.5vw, 2rem); /* 24–32px */

/* Component gap fluid */
--space-component-gap: clamp(0.75rem, 0.75rem + 0.25vw, 1rem); /* 12–16px */
```

### Breakpoint Overrides (if clamp insufficient)
| Breakpoint | `space-page-padding-x` | `space-section-gap` |
|------------|------------------------|---------------------|
| xs (<640) | 16px | 24px |
| sm (640–767) | 16px | 24px |
| md (768–1023) | 20px | 28px |
| lg (1024–1279) | 24px | 32px |
| xl (1280+) | 24px | 32px |

---

## 5. Baseline Grid

- **4px baseline** — All text line heights, component heights, spacing align to 4px grid
- **Line heights** chosen to fit grid: 20, 24, 28, 32, 36, 40, 44, 48, 56, 64
- **Component heights:** Button 40px (compact 36px), Input 40px, Select 40px, Table row 48px (comfort) / 36px (compact)

---

## 6. Negative Space (Margin)

| Token | Value | Usage |
|-------|-------|-------|
| `space-margin-auto` | `auto` | Centering |
| `space-margin-neg-sm` | `-8px` | Overlapping cards |
| `space-margin-neg-md` | `-12px` | Modal close button bleed |
| `space-margin-neg-lg` | `-16px` | Full-bleed section in padded container |

---

## 7. Usage Rules

1. **Never hardcode px/rem** in component styles — use semantic tokens
2. **Internal spacing** owned by component; **external** by layout/parent
3. **Stacks** use `space-stack-*`; **inlines** use `space-inline-*`
4. **Responsive** via clamp() — avoid media query overrides
5. **Grid gaps** use `space-component-gap` (or `space-4` directly)
6. **No magic numbers** — if a value not in scale, add to scale

---

## 8. CSS Token Definitions

```css
:root {
  /* Primitive */
  --space-0: 0;
  --space-1: 0.25rem;   /* 4px */
  --space-2: 0.5rem;    /* 8px */
  --space-3: 0.75rem;   /* 12px */
  --space-4: 1rem;      /* 16px — BASE */
  --space-5: 1.25rem;   /* 20px */
  --space-6: 1.5rem;    /* 24px */
  --space-7: 1.75rem;   /* 28px */
  --space-8: 2rem;      /* 32px */
  --space-10: 2.5rem;   /* 40px */
  --space-12: 3rem;     /* 48px */
  --space-16: 4rem;     /* 64px */

  /* Semantic Layout */
  --space-page-padding-x: clamp(1rem, 1rem + 0.5vw, 1.5rem);
  --space-page-padding-y: var(--space-6);
  --space-section-gap: clamp(1.5rem, 1.5rem + 0.5vw, 2rem);
  --space-component-gap: clamp(0.75rem, 0.75rem + 0.25vw, 1rem);
  --space-group-gap: var(--space-6);

  /* Component Internal */
  --space-xs: var(--space-1);
  --space-sm: var(--space-2);
  --space-md: var(--space-3);
  --space-lg: var(--space-4);
  --space-xl: var(--space-6);

  /* Inline */
  --space-inline-xs: var(--space-1);
  --space-inline-sm: var(--space-2);
  --space-inline-md: var(--space-3);

  /* Stack */
  --space-stack-xs: var(--space-2);
  --space-stack-sm: var(--space-3);
  --space-stack-md: var(--space-4);
  --space-stack-lg: var(--space-6);

  /* Layout */
  --space-header-height: 56px;
  --space-sidebar-width: 264px;
  --space-sidebar-icon-width: 72px;
  --space-breadcrumb-gap: var(--space-2);
  --space-tab-gap: var(--space-1);
}
```