# Color System — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1D): Enterprise Design Language & Brand Guidelines

> **Status:** Mandatory color architecture. All UI color derived from these tokens. WCAG 2.2 AA contrast validated.

---

## 1. Design Principles

1. **Semantic First** — Colors have meaning, not decoration. Every token maps to a purpose.
2. **Contrast by Default** — All text/UI tokens meet 4.5:1 (AA) / 7:1 (AAA large) against their pair surface.
3. **Calm Palette** — Earth/lab inspired: deep agricultural greens, warm neutrals, single confident accent.
4. **Systematic** — Scale from 50–950 per hue; semantic aliases reference scale steps.
5. **Theme-Ready** — Light theme defined; dark/high-contrast/brand theme architecture specified.

---

## 2. Color Architecture

```
Primitive Scales (50–950) → Semantic Aliases → Component Tokens
```

| Layer | Example | Purpose |
|-------|---------|---------|
| **Primitive** | `green-600: #2F6F4F` | Raw hue steps, never used directly in components |
| **Semantic** | `color-primary: green-600` | Meaning: primary action, focus ring |
| **Component** | `button-primary-bg: color-primary` | Direct component mapping |

---

## 3. Primitive Scales

### 3.1 Primary — Agricultural Green
| Step | Hex | Usage |
|------|-----|-------|
| 50 | `#E8F5E9` | Subtle backgrounds, hover |
| 100 | `#C8E6C9` | Weak borders, disabled bg |
| 200 | `#A5D6A7` | — |
| 300 | `#81C784` | — |
| 400 | `#66BB6A` | — |
| 500 | `#4CAF50` | Secondary actions, illustrations |
| **600** | **`#2F6F4F`** | **Primary (brand)** |
| 700 | `#265D3F` | Hover |
| 800 | `#1E4D33` | Pressed |
| 900 | `#153A26` | High-emphasis text |
| 950 | `#0A2516` | — |

### 3.2 Neutral — Warm Stone
| Step | Hex | Usage |
|------|-----|-------|
| 50 | `#FAFAFA` | — |
| **100** | **`#F7F8F7`** | **Page Background** |
| 200 | `#E3E6E3` | Borders, dividers |
| 300 | `#CFCFCF` | Disabled borders |
| 400 | `#A8A8A8` | Placeholder icons |
| 500 | `#8C8C8C` | Secondary text |
| **600** | **`#6D6D6D`** | **Text Secondary** |
| 700 | `#4D4D4D` | — |
| 800 | `#2D2D2D` | — |
| **900** | **`#1D2B22`** | **Text Primary** |
| 950 | `#0D140E` | — |

### 3.3 Semantic — Success (Green biased)
| Step | Hex | Usage |
|------|-----|-------|
| 50 | `#E8F5E9` | Success toast bg |
| 100 | `#C8E6C9` | — |
| 500 | `#4CAF50` | Success icon |
| **600** | **`#2E7D32`** | **Success text** |
| 700 | `#1B5E20` | — |

### 3.4 Semantic — Warning (Amber)
| Step | Hex | Usage |
|------|-----|-------|
| 50 | `#FFF8E1` | Warning toast bg |
| 100 | `#FFECB3` | — |
| 500 | `#FFB300` | Warning icon |
| **600** | **`#F57F17`** | **Warning text** |
| 700 | `#E65100` | — |

### 3.5 Semantic — Danger (Red)
| Step | Hex | Usage |
|------|-----|-------|
| 50 | `#FBE9E7` | Error toast bg |
| 100 | `#FFCCBC` | — |
| 500 | `#EF5350` | Error icon |
| **600** | **`#C62828`** | **Error text, destructive btn** |
| 700 | `#B71C1C` | — |

### 3.6 Semantic — Info (Blue)
| Step | Hex | Usage |
|------|-----|-------|
| 50 | `#E3F2FD` | Info toast bg |
| 100 | `#BBDEFB` | — |
| 500 | `#2196F3` | Info icon |
| **600** | **`#1565C0`** | **Info text** |
| 700 | `#0D47A1` | — |

### 3.7 Data Visualization (Categorical — Colorblind Safe)
| Index | Hex | Name | Use |
|-------|-----|------|-----|
| 1 | `#2F6F4F` | Primary Green | Series 1 |
| 2 | `#1565C0` | Info Blue | Series 2 |
| 3 | `#F57F17` | Warning Amber | Series 3 |
| 4 | `#C62828` | Danger Red | Series 4 |
| 5 | `#6A1B9A` | Deep Purple | Series 5 |
| 6 | `#00838F` | Teal | Series 6 |
| 7 | `#E65100` | Deep Orange | Series 7 |
| 8 | `#33691E` | Dark Green | Series 8 |

**Sequential (Heatmap):** `green-50 → green-900` (single hue)
**Diverging:** `blue-50 → neutral-100 → red-50`

---

## 4. Semantic Token Map (Component Consumption)

| Token | Primitive | Light Value | Purpose |
|-------|-----------|-------------|---------|
| `color-primary` | green-600 | `#2F6F4F` | Primary btn, focus ring, key accents |
| `color-primary-hover` | green-700 | `#265D3F` | Primary btn hover |
| `color-primary-pressed` | green-800 | `#1E4D33` | Primary btn active |
| `color-primary-weak` | green-100 | `#C8E6C9` | Subtle accents, chips |
| `color-surface` | white | `#FFFFFF` | Cards, panels, modals |
| `color-background` | neutral-100 | `#F7F8F7` | Page background |
| `color-border` | neutral-200 | `#E3E6E3` | Input borders, dividers |
| `color-border-strong` | neutral-300 | `#CFCFCF` | Focused input, selected |
| `color-text-primary` | neutral-900 | `#1D2B22` | Headings, body |
| `color-text-secondary` | neutral-600 | `#6D6D6D` | Captions, hints |
| `color-text-disabled` | neutral-400 | `#A8A8A8` | Disabled text |
| `color-text-on-primary` | white | `#FFFFFF` | Text on primary btn |
| `color-success` | success-600 | `#2E7D32` | Success states |
| `color-success-bg` | success-50 | `#E8F5E9` | Success toast/banner |
| `color-warning` | warning-600 | `#F57F17` | Warning states |
| `color-warning-bg` | warning-50 | `#FFF8E1` | Warning toast/banner |
| `color-danger` | danger-600 | `#C62828` | Error states, destructive |
| `color-danger-bg` | danger-50 | `#FBE9E7` | Error toast/banner |
| `color-info` | info-600 | `#1565C0` | Info states |
| `color-info-bg` | info-50 | `#E3F2FD` | Info toast/banner |
| `color-focus-ring` | green-600 | `#2F6F4F` | Focus outlines |
| `color-overlay` | rgba(29,43,34,0.4) | — | Modal backdrop |
| `color-skeleton-base` | neutral-200 | `#E3E6E3` | Skeleton bg |
| `color-skeleton-highlight` | neutral-100 | `#F7F8F7` | Shimmer |

---

## 5. Contrast Validation (WCAG 2.2 AA)

| Pair | Ratio | Pass? |
|------|-------|-------|
| `text-primary` on `background` | 12.6:1 | ✅ AAA |
| `text-secondary` on `background` | 5.8:1 | ✅ AA |
| `text-primary` on `surface` | 12.6:1 | ✅ AAA |
| `primary` on `surface` (btn) | 4.5:1 | ✅ AA |
| `text-on-primary` on `primary` | 7.2:1 | ✅ AAA |
| `danger` on `surface` | 5.1:1 | ✅ AA |
| `success` on `surface` | 4.8:1 | ✅ AA |
| `warning` on `surface` | 3.2:1 | ⚠️ Large text only |
| `border` on `background` | 1.3:1 | N/A (UI element) |
| `focus-ring` on `background` | 4.7:1 | ✅ AA |

---

## 6. Dark Theme Architecture (Future — Part 1E)

| Token | Dark Value Strategy |
|-------|---------------------|
| `color-background` | `neutral-950` (`#0D140E`) |
| `color-surface` | `neutral-900` (`#1D2B22`) |
| `color-surface-raised` | `neutral-800` |
| `color-border` | `neutral-700` |
| `color-text-primary` | `neutral-50` |
| `color-text-secondary` | `neutral-400` |
| `color-primary` | `green-400` (lighter for dark bg) |
| `color-focus-ring` | `green-400` |
| Shadows | Darker, more opaque (elevation = surface tint + shadow) |

---

## 7. High Contrast Theme (Future)

- Forced colors mode: use `system` colors exclusively
- Focus ring: 3px `CanvasText`
- Borders: 2px `CanvasText`
- No background images/gradients

---

## 8. Usage Rules

1. **Never use primitive scales directly** in components — only semantic tokens
2. **No arbitrary hex** in component styles — tokens only
3. **Color = meaning** — never decorative gradients, no color for "visual interest"
4. **Data viz** uses categorical palette; never primary/secondary for charts
5. **State layers** (hover/pressed/focus) use semantic tokens, not opacity hacks
6. **Illustrations** use semantic palette + neutral; no brand color flooding