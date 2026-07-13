# Elevation System — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1D): Enterprise Design Language & Brand Guidelines

> **Status:** Mandatory depth system. Shadows, surfaces, and z-index derive from this.

---

## 1. Philosophy

- **Depth = Surface + Shadow** — never shadow alone
- **Elevation communicates hierarchy** — modal > dropdown > card > page
- **Restrained** — max elevation 4; no heavy drop shadows
- **Semantic** — tokens describe purpose, not pixel values

---

## 2. Surface Levels

| Level | Token | Background | Border | Shadow | Use Case |
|-------|-------|------------|--------|--------|----------|
| **0** | `surface-0` | `--color-background` | None | None | Page background |
| **1** | `surface-1` | `--color-surface` | `1px solid var(--color-border)` | None | Cards, panels, sidebar |
| **2** | `surface-2` | `--color-surface` | `1px solid var(--color-border)` | `elevation-1` | Raised cards, hover state |
| **3** | `surface-3` | `--color-surface` | `1px solid var(--color-border)` | `elevation-2` | Dropdowns, popovers, tooltips |
| **4** | `surface-4` | `--color-surface` | `1px solid var(--color-border)` | `elevation-3` | Modals, drawers, side sheets |
| **5** | `surface-5` | `--color-surface` | None | `elevation-4` | Full-screen overlay, loader |

---

## 3. Shadow Tokens

| Token | Value | Elevation Level | Use Case |
|-------|-------|-----------------|----------|
| `elevation-none` | `none` | 0 | Flat, surface-0/1 |
| `elevation-1` | `0 1px 2px rgba(0,0,0,0.04), 0 1px 3px rgba(0,0,0,0.08)` | 1 | Hover card, subtle lift |
| `elevation-2` | `0 4px 8px rgba(0,0,0,0.06), 0 2px 4px rgba(0,0,0,0.05)` | 2 | Dropdown, popover, tooltip |
| `elevation-3` | `0 8px 24px rgba(0,0,0,0.08), 0 4px 12px rgba(0,0,0,0.05)` | 3 | Modal, drawer, side sheet |
| `elevation-4` | `0 16px 48px rgba(0,0,0,0.12), 0 8px 24px rgba(0,0,0,0.08)` | 4 | Full-screen overlay, loading |

**Color:** Always `rgba(0,0,0,α)` — never colored shadows
**Inner shadow:** Not used (except focus ring which is separate)

---

## 4. Component → Elevation Mapping

| Component | Resting | Hover | Active/Focus | Open |
|-----------|---------|-------|--------------|------|
| **Card** | surface-1 | surface-2 (elev-1) | surface-2 | — |
| **Button** | surface-1 | elev-1 | pressed: inset | — |
| **Input** | surface-1 | border primary | focus ring | — |
| **Dropdown** | — | — | — | surface-3 + elev-2 |
| **Popover** | — | — | — | surface-3 + elev-2 |
| **Tooltip** | — | — | — | surface-3 + elev-2 |
| **Modal** | — | — | — | surface-4 + elev-3 |
| **Drawer** | — | — | — | surface-4 + elev-3 |
| **Side Sheet** | — | — | — | surface-4 + elev-3 |
| **Toast** | surface-3 + elev-2 | — | — | — |
| **Banner** | surface-1 | — | — | — |
| **Table Row Hover** | surface-2 (elev-1) | — | — | — |

---

## 5. Overlay / Backdrop

| Token | Value | Use Case |
|-------|-------|----------|
| `backdrop-light` | `rgba(20, 30, 24, 0.32)` | Modal, drawer, side sheet |
| `backdrop-heavy` | `rgba(20, 30, 24, 0.48)` | Full-screen loader, critical modal |

---

## 6. Z-Index System

| Token | Value | Layer |
|-------|-------|-------|
| `z-base` | 0 | Page content |
| `z-sticky` | 10 | Sticky headers in scroll containers |
| `z-header` | 50 | Global header |
| `z-sidebar` | 60 | Desktop sidebar |
| `z-drawer` | 80 | Mobile drawer |
| `z-dropdown` | 100 | Dropdown, popover, autocomplete |
| `z-tooltip` | 110 | Tooltip |
| `z-toast` | 120 | Toast stack |
| `z-modal-backdrop` | 200 | Modal backdrop |
| `z-modal` | 210 | Modal dialog |
| `z-drawer-panel` | 200 | Drawer panel |
| `z-command-palette` | 300 | Command palette |
| `z-max` | 9999 | Emergency (debug only) |

**Rule:** Never use raw z-index in components — only tokens. Components declare their layer: `z-index: var(--z-dropdown);`

---

## 7. Focus Ring (Separate from Elevation)

| Token | Value |
|-------|-------|
| `focus-ring-width` | 3px |
| `focus-ring-color` | `--color-focus` (primary) |
| `focus-ring-offset` | 2px |
| `focus-ring-style` | `solid` |

```css
:focus-visible {
  outline: var(--focus-ring-width) solid var(--focus-ring-color);
  outline-offset: var(--focus-ring-offset);
}
```

---

## 8. CSS Token Definitions

```css
:root {
  /* Surfaces */
  --surface-0: var(--color-background);
  --surface-1: var(--color-surface);
  --surface-2: var(--color-surface);
  --surface-3: var(--color-surface);
  --surface-4: var(--color-surface);
  --surface-5: var(--color-surface);

  /* Shadows */
  --elevation-none: none;
  --elevation-1: 0 1px 2px rgba(0,0,0,0.04), 0 1px 3px rgba(0,0,0,0.08);
  --elevation-2: 0 4px 8px rgba(0,0,0,0.06), 0 2px 4px rgba(0,0,0,0.05);
  --elevation-3: 0 8px 24px rgba(0,0,0,0.08), 0 4px 12px rgba(0,0,0,0.05);
  --elevation-4: 0 16px 48px rgba(0,0,0,0.12), 0 8px 24px rgba(0,0,0,0.08);

  /* Backdrops */
  --backdrop-light: rgba(20, 30, 24, 0.32);
  --backdrop-heavy: rgba(20, 30, 24, 0.48);

  /* Z-Index */
  --z-base: 0;
  --z-sticky: 10;
  --z-header: 50;
  --z-sidebar: 60;
  --z-drawer: 80;
  --z-dropdown: 100;
  --z-tooltip: 110;
  --z-toast: 120;
  --z-modal-backdrop: 200;
  --z-modal: 210;
  --z-drawer-panel: 200;
  --z-command-palette: 300;
  --z-max: 9999;

  /* Focus */
  --focus-ring-width: 3px;
  --focus-ring-color: var(--color-primary);
  --focus-ring-offset: 2px;
}
```