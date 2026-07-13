# Token Naming Convention — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1D): Enterprise Design Language & Brand Guidelines

> **Status:** Mandatory naming standard. All tokens follow this convention. Enforced by linting and code review.

---

## 1. Core Principle

**CTI (Community Token Format) inspired** — predictable, parseable, tool-friendly.

```
{category}.{property}.{variant}.{state}.{scale}
```

---

## 2. Category Vocabulary

| Category | Prefix | Description |
|----------|--------|-------------|
| Color | `color` | All color tokens (primitives + semantic) |
| Typography | `typography` | Font, size, weight, line height, tracking |
| Spacing | `spacing` | Padding, margin, gap, layout dimensions |
| Radius | `radius` | Border radius tokens |
| Elevation | `elevation` | Shadows, surfaces, z-index |
| Border | `border` | Width, style, color |
| Opacity | `opacity` | Opacity values |
| Animation | `animation` | Duration, easing, motion |
| Breakpoint | `breakpoint` | Viewport breakpoints |
| Z-Index | `zIndex` | Stacking context |
| Sizing | `sizing` | Width, height, icon, illustration |

---

## 3. Property Vocabulary (per Category)

### 3.1 Color
| Property | Meaning |
|----------|---------|
| `bg` | Background |
| `text` | Text color |
| `border` | Border color |
| `icon` | Icon color |
| `shadow` | Shadow color |
| `overlay` | Modal/overlay backdrop |
| `skeleton` | Skeleton loader |
| `focus` | Focus ring |

### 3.2 Typography
| Property | Meaning |
|----------|---------|
| `fontFamily` | Font family |
| `fontSize` | Font size |
| `fontWeight` | Font weight |
| `lineHeight` | Line height |
| `letterSpacing` | Letter spacing |

### 3.3 Spacing
| Property | Meaning |
|----------|---------|
| `padding` | Internal padding |
| `margin` | External margin |
| `gap` | Grid/flex gap |
| `space` | Generic space |
| `inset` | Position inset (top/right/bottom/left) |

### 3.4 Radius
| Property | Meaning |
|----------|---------|
| `radius` | Border radius |

### 3.5 Elevation
| Property | Meaning |
|----------|---------|
| `shadow` | Box shadow |
| `surface` | Surface level (0–4) |

### 3.6 Border
| Property | Meaning |
|----------|---------|
| `width` | Border width |
| `style` | Border style |
| `color` | Border color (alias to color.border) |

### 3.7 Animation
| Property | Meaning |
|----------|---------|
| `duration` | Transition/animation duration |
| `easing` | Easing function |
| `motion` | Motion reduction token |

### 3.8 Sizing
| Property | Meaning |
|----------|---------|
| `width` | Width |
| `height` | Height |
| `icon` | Icon size |
| `illustration` | Illustration size |

---

## 4. Variant Vocabulary

| Category | Variants |
|----------|----------|
| **Color (Semantic)** | `primary`, `secondary`, `success`, `warning`, `danger`, `info`, `neutral`, `surface`, `overlay`, `focus`, `skeleton`, `onPrimary`, `onSurface` |
| **Color (Primitive)** | `green`, `neutral`, `blue`, `amber`, `red`, `purple`, `teal`, `orange` |
| **Surface** | `base`, `raised`, `overlay`, `modal` |
| **State** | `default`, `hover`, `pressed`, `focus`, `disabled`, `selected`, `active`, `error`, `loading`, `visited` |
| **Scale** | `none`, `xs`, `sm`, `md`, `lg`, `xl`, `2xl`, `3xl`, `auto`, `full` |

---

## 5. Complete Examples

| Token | Breakdown |
|-------|-----------|
| `color.bg.primary.default` | Category: color, Property: bg, Variant: primary, State: default |
| `color.bg.primary.hover` | State: hover |
| `color.text.onPrimary.default` | Property: text, Variant: onPrimary |
| `color.border.neutral.default` | Variant: neutral |
| `color.focus.ring.default` | Property: focus, Variant: ring |
| `typography.fontSize.body.md` | Category: typography, Property: fontSize, Variant: body, Scale: md |
| `typography.fontWeight.semibold` | Property: fontWeight, Variant: semibold |
| `typography.lineHeight.relaxed` | Property: lineHeight, Variant: relaxed |
| `spacing.padding.md` | Category: spacing, Property: padding, Scale: md |
| `spacing.gap.sm` | Property: gap, Scale: sm |
| `radius.card.default` | Category: radius, Variant: card, State: default |
| `radius.btn.default` | Variant: btn |
| `elevation.shadow.card.hover` | Category: elevation, Property: shadow, Variant: card, State: hover |
| `elevation.surface.raised` | Property: surface, Variant: raised |
| `border.width.thin` | Category: border, Property: width, Variant: thin |
| `animation.duration.fast` | Category: animation, Property: duration, Variant: fast |
| `animation.easing.standard` | Property: easing, Variant: standard |
| `breakpoint.md` | Category: breakpoint, Scale: md |
| `zIndex.modal` | Category: zIndex, Variant: modal |
| `sizing.icon.md` | Category: sizing, Property: icon, Scale: md |
| `sizing.illustration.md` | Property: illustration, Scale: md |

---

## 6. Primitive vs Semantic Naming

### Primitives (Raw Values)
```
color.green.600
color.neutral.900
spacing.base (4px)
radius.base (4px)
```

### Semantic (Meaning)
```
color.bg.primary.default → {color.green.600}
color.text.primary.default → {color.neutral.900}
spacing.padding.md → {spacing.base * 4}
radius.card.default → {radius.base * 3}
```

### Component (Usage)
```
button.primary.bg.default → {color.bg.primary.default}
card.radius.default → {radius.card.default}
input.padding.x → {spacing.padding.sm}
```

---

## 7. Rules

1. **No abbreviations** in category/property — `typography` not `type`, `spacing` not `space`
2. **Lowercase kebab-case** everywhere — `font-size` not `fontSize` or `font_size`
2. **State always explicit** — `default` not omitted
3. **Scale always explicit** — `md` not omitted even if "default"
4. **Primitive never in component** — components reference semantic only
5. **No magic numbers** — every value traces to primitive
6. **Alias with reference syntax** — `{color.green.600}` not `#2F6F4F`
7. **Version in package** — `@sporekart/tokens@1.2.0` — breaking = major

---

## 8. Linting Rules (Enforced)

| Rule | Severity |
|------|----------|
| Token name matches regex `^[a-z]+(\.[a-z]+){2,5}$` | Error |
| Primitive tokens only in `primitives/` folder | Error |
| Semantic tokens reference valid primitives | Error |
| Component tokens reference valid semantic | Error |
| No hardcoded hex/px/ms in component styles | Error |
| Every token has `description` field | Warning |
| Every token has `example` field | Warning |

---

## 9. File Organization

```
tokens/
├── primitives/
│   ├── color.json
│   ├── spacing.json
│   ├── radius.json
│   ├── typography.json
│   └── ...
├── semantic/
│   ├── color.json
│   ├── spacing.json
│   ├── radius.json
│   └── ...
├── component/
│   ├── button.json
│   ├── card.json
│   ├── input.json
│   └── ...
├── themes/
│   ├── light.json
│   ├── dark.json
│   └── high-contrast.json
└── index.json (merged export)
```

---

## 10. Migration / Deprecation

| Stage | Convention |
|-------|------------|
| **Active** | `color.bg.primary.default` |
| **Deprecated** | `color.bg.primary.default @deprecated use color.bg.primary.default` |
| **Removed** | Deleted — major version bump |

**Never rename** — add new, deprecate old, remove in next major.