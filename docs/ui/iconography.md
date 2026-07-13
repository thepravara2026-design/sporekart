# Iconography — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1D): Enterprise Design Language & Brand Guidelines

> **Status:** Mandatory icon standards. Single style, tokenized sizes, semantic usage.

---

## 1. Design Principles

1. **Single Style** — Outline (default) + Filled (active/selected) variants only
2. **24×24 Base** — Designed on 24px grid, stroke 2px, optical alignment
3. **Tokenized Sizes** — 16, 20, 24, 28, 32, 40, 48 via size tokens
4. **Semantic Colors** — `currentColor` default; semantic tokens for state
5. **Accessibility** — Decorative `aria-hidden="true"`; meaningful have labels
6. **No Icon Fonts** — Inline SVG only; sprite for caching

---

## 2. Visual Specification

| Property | Value |
|----------|-------|
| **Grid** | 24×24px |
| **Stroke Width** | 2px (1.5px at 16px, 2.5px at 32px+) |
| **Corner Radius** | 2px (matches `--radius-sm`) |
| **End Caps** | Round |
| **Join Style** | Round |
| **Alignment** | Center (optical) |
| **Filled Variant** | Same shapes, filled with `currentColor` |

---

## 3. Size Tokens

| Token | Size | Usage |
|-------|------|-------|
| `icon-xs` | 16×16px | Inline with caption/label, table actions |
| `icon-sm` | 20×20px | Button inline, breadcrumb, chips |
| `icon-md` | 24×24px | **Default** — Sidebar, header, toolbar |
| `icon-lg` | 28×28px | Empty state, card media |
| `icon-xl` | 32×32px | Feature callout, onboarding |
| `icon-2xl` | 40×40px | Large empty state |
| `icon-3xl` | 48×48px | Illustration placeholder |

**CSS:**
```css
:root {
  --icon-xs: 16px;
  --icon-sm: 20px;
  --icon-md: 24px;
  --icon-lg: 28px;
  --icon-xl: 32px;
  --icon-2xl: 40px;
  --icon-3xl: 48px;
}
```

---

## 4. Color Usage

| Context | Fill/Stroke | Token |
|---------|-------------|-------|
| Default | `currentColor` | Inherits text color |
| Disabled | `var(--color-text-disabled)` | 40% opacity |
| Primary Action | `var(--color-primary)` | Primary button icon |
| Destructive | `var(--color-danger)` | Delete, remove |
| Success | `var(--color-success)` | Check, confirmed |
| Warning | `var(--color-warning)` | Alert, pending |
| Info | `var(--color-info)` | Help, info |
| On Primary | `white` | Icon on primary button |

---

## 5. Required Icon Set (MVP)

### 5.1 Navigation / UI
| Name | Filled Variant? | Usage |
|------|-----------------|-------|
| `home` | Yes | Home, dashboard |
| `search` | No | Search trigger |
| `menu` / `close` | No | Mobile drawer |
| `chevron-left` / `right` | No | Breadcrumbs, accordion |
| `chevron-up` / `down` | No | Sort, select, collapse |
| `more-horizontal` / `vertical` | No | Overflow menu |
| `arrow-left` / `right` | No | Back, forward |
| `external-link` | No | External navigation |

### 5.2 Actions
| Name | Filled Variant? | Usage |
|------|-----------------|-------|
| `plus` | Yes | Create, add |
| `edit` / `pencil` | Yes | Edit |
| `trash` / `delete` | Yes | Delete (destructive) |
| `archive` | Yes | Archive |
| `download` / `upload` | No | Export, import |
| `refresh` / `sync` | No | Refresh, sync |
| `filter` | Yes | Filter active/inactive |
| `sort` | Yes | Sort |
| `save` | Yes | Save |
| `cancel` | No | Cancel |

### 5.3 Status / Feedback
| Name | Filled Variant? | Usage |
|------|-----------------|-------|
| `check` / `check-circle` | Yes | Success, complete |
| `alert-circle` / `alert-triangle` | Yes | Warning, error |
| `info` | Yes | Info |
| `help-circle` | Yes | Help tooltip |
| `loader` | No | Loading spinner |
| `eye` / `eye-off` | Yes | Visibility toggle |
| `lock` / `unlock` | Yes | Security, permissions |

### 5.4 Domain-Specific (Agriculture)
| Name | Filled Variant? | Usage |
|------|-----------------|-------|
| `seed` | Yes | Seeds, planting |
| `sprout` / `plant` | Yes | Growth, crops |
| `droplet` | Yes | Irrigation, water |
| `sun` | Yes | Weather, solar |
| `tractor` | No | Machinery, equipment |
| `warehouse` | Yes | Storage, inventory |
| `truck` | No | Logistics, delivery |
| `leaf` | Yes | Organic, natural |
| `flask` / `beaker` | Yes | Lab, testing, inputs |
| `certificate` | Yes | Authenticity, certification |

### 5.5 UI Specific
| Name | Filled Variant? | Usage |
|------|-----------------|-------|
| `user` / `user-plus` | Yes | Profile, invite |
| `settings` / `sliders` | No | Settings, config |
| `bell` / `bell-off` | Yes | Notifications |
| `mail` / `send` | No | Messages, email |
| `calendar` | Yes | Dates, scheduling |
| `clock` | No | Time, history |
| `tag` | Yes | Labels, categories |
| `folder` / `file` | Yes | CMS, documents |
| `image` | Yes | Media, photos |
| `link` | No | Links, references |
| `share` | No | Share action |
| `print` | No | Print |
| `download` / `upload` | No | Export/import |

---

## 6. Implementation

### 6.1 SVG Sprite (Recommended)
```html
<!-- In <head> or top of <body> -->
<svg style="display:none" aria-hidden="true">
  <symbol id="icon-home" viewBox="0 0 24 24">...</symbol>
  <symbol id="icon-search" viewBox="0 0 24 24">...</symbol>
  ...
</svg>

<!-- Usage -->
<svg class="icon icon-md" aria-hidden="true"><use href="#icon-home"/></svg>
<svg class="icon icon-md" aria-label="Search"><use href="#icon-search"/></svg>
```

### 6.2 CSS Classes
```css
.icon {
  display: inline-block;
  width: var(--icon-md);
  height: var(--icon-md);
  flex-shrink: 0;
  color: currentColor;
}
.icon-xs { width: var(--icon-xs); height: var(--icon-xs); }
.icon-sm { width: var(--icon-sm); height: var(--icon-sm); }
.icon-lg { width: var(--icon-lg); height: var(--icon-lg); }
.icon-xl { width: var(--icon-xl); height: var(--icon-xl); }
```

### 6.3 React Component (Future Part 1E)
```tsx
<Icon name="home" size="md" aria-label="Home" />
<Icon name="trash" size="sm" color="danger" />
```

---

## 7. Accessibility

- **Decorative:** `aria-hidden="true"` + no label
- **Standalone (no text):** `aria-label="Action name"` or `role="img" aria-label`
- **With text:** `aria-hidden="true"` (text is label)
- **Focusable icon button:** `<button><Icon aria-hidden="true"/> <span class="visually-hidden">Label</span></button>`
- **Color:** Never sole indicator — pair with text/shape

---

## 8. Future Expansion (Part 1E+)

- Animated icons (Lottie/SVG SMIL) for loading, success
- Duotone variant for marketing
- RTL-aware directional icons (auto-flip)
- Variable font icon font (if performance demands)