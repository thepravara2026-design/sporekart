# Typography System — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1D): Enterprise Design Language & Brand Guidelines

> **Status:** Mandatory type hierarchy. All text uses these tokens. Single font family, modular scale, responsive.

---

## 1. Design Principles

1. **Readability Above All** — Farmers on low-end phones, bright sun, varying literacy
2. **Scientific Clarity** — Data, units, labels unambiguous
3. **Single Family** — One typeface for UI + content; optional mono for data/IDs
4. **Modular Scale** — 1.25 ratio (major third) — harmonious, not arbitrary
5. **Responsive by Default** — Fluid scaling via clamp(), no breakpoint jumps
6. **System Font Stack** — Performance, familiarity, OS integration

---

## 2. Font Family

### 2.1 Primary — System UI Stack (Default)
```css
--font-family-sans: system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
```
**Rationale:** Zero download, perfect hinting, OS-level accessibility, Devanagari support on Android/iOS.

### 2.2 Mono — Data/Code
```css
--font-family-mono: ui-monospace, SFMono-Regular, "SF Mono", Menlo, Consolas, "Liberation Mono", monospace;
```
**Usage:** Numeric IDs, codes, amounts in tables, code blocks, OTP inputs.

### 2.3 Future Brand Font (Part 1E)
- Slot reserved: `--font-family-brand`
- Must support: Latin + Devanagari, variable font, 400/500/600/700
- Evaluation criteria: readability at 12px, scientific character, performance

---

## 3. Type Scale (Modular 1.25 Ratio)

| Token | Size (rem) | px @ 16px base | Line Height | Weight | Letter Spacing | Use Case |
|-------|------------|----------------|-------------|--------|----------------|----------|
| `text-display` | `clamp(2.44rem, 2rem + 2vw, 3.05rem)` | 39–49 | 1.15 | 700 | -0.02em | Hero, marketing |
| `text-h1` | `clamp(1.95rem, 1.75rem + 1vw, 2.44rem)` | 31–39 | 1.2 | 700 | -0.01em | Page title (H1) |
| `text-h2` | `clamp(1.56rem, 1.38rem + 0.9vw, 1.95rem)` | 25–31 | 1.25 | 600 | -0.01em | Section heading (H2) |
| `text-h3` | `clamp(1.25rem, 1.13rem + 0.6vw, 1.56rem)` | 20–25 | 1.3 | 600 | 0 | Sub-section (H3) |
| `text-h4` | `1.25rem` | 20 | 1.35 | 600 | 0 | Card title, H4 |
| `text-h5` | `1.125rem` | 18 | 1.4 | 500 | 0 | Minor heading |
| `text-h6` | `1rem` | 16 | 1.45 | 500 | 0 | Tiny heading |
| `text-subtitle` | `1rem` | 16 | 1.5 | 400 | 0 | Page subtitle, meta |
| `text-body-lg` | `1.125rem` | 18 | 1.6 | 400 | 0 | Comfortable reading |
| `text-body` | `1rem` | 16 | 1.6 | 400 | 0 | **Default body** |
| `text-body-sm` | `0.875rem` | 14 | 1.55 | 400 | 0 | Secondary content |
| `text-caption` | `0.8125rem` | 13 | 1.5 | 400 | 0.01em | Captions, hints |
| `text-label` | `0.8125rem` | 13 | 1.5 | 500 | 0.02em | Form labels, buttons |
| `text-button` | `0.875rem` | 14 | 1.4 | 600 | 0.02em | Button text |
| `text-table` | `0.875rem` | 14 | 1.5 | 400 | 0 | Table cells |
| `text-table-h` | `0.75rem` | 12 | 1.4 | 600 | 0.05em | Table headers |
| `text-code` | `0.875rem` | 14 | 1.5 | 400 | 0 | Inline code, IDs |
| `text-numeric` | `1rem` | 16 | 1.6 | 500 | 0 | Amounts, quantities |
| `text-otp` | `2rem` | 32 | 1.2 | 600 | 0.25em | OTP input digits |

---

## 4. Responsive Scaling Strategy

```css
/* Fluid type via clamp(min, preferred, max) */
--text-h1: clamp(1.95rem, 1.75rem + 1vw, 2.44rem);
--text-body: 1rem; /* Base never changes — user zoom handles it */

/* Viewport-based adjustments only for display sizes */
@media (min-width: 1280px) {
  :root { --text-h1: 2.44rem; }
}
@media (max-width: 640px) {
  :root { --text-h1: 1.95rem; }
}
```

**Rules:**
- Base body = 1rem (16px) — never smaller
- User zoom (browser setting) respected — no `max-scale` prevention
- Display sizes fluid; semantic sizes (body, caption) fixed rem

---

## 5. Font Weight System

| Token | Weight | Usage |
|-------|--------|-------|
| `weight-light` | 300 | — (reserved) |
| `weight-normal` | 400 | Body, caption, subtitle |
| `weight-medium` | 500 | Labels, table headers, numeric, button |
| `weight-semibold` | 600 | H2–H6, button text, emphasis |
| `weight-bold` | 700 | H1, Display, strong emphasis |

---

## 6. Line Height System

| Token | Value | Usage |
|-------|-------|-------|
| `leading-tight` | 1.15 | Display, H1 |
| `leading-snug` | 1.25 | H2 |
| `leading-normal` | 1.35 | H3–H4 |
| `leading-relaxed` | 1.5 | Body, body-sm, table |
| `leading-loose` | 1.6 | Body-lg, comfortable reading |

---

## 7. Letter Spacing

| Token | Value | Usage |
|-------|-------|-------|
| `tracking-tight` | -0.02em | Display, H1 |
| `tracking-snug` | -0.01em | H2 |
| `tracking-normal` | 0 | Body, H3–H6 |
| `tracking-wide` | 0.02em | Labels, buttons, captions |
| `tracking-wider` | 0.05em | Table headers (uppercase) |
| `tracking-otp` | 0.25em | OTP digit inputs |

---

## 8. Component Typography Mapping

| Component | Token | Fallback |
|-----------|-------|----------|
| Page H1 | `text-h1` / `weight-bold` / `leading-tight` | — |
| Section H2 | `text-h2` / `weight-semibold` / `leading-snug` | — |
| Card Title | `text-h4` / `weight-semibold` / `leading-normal` | — |
| Body Text | `text-body` / `weight-normal` / `leading-relaxed` | — |
| Caption/Hint | `text-caption` / `weight-normal` / `leading-normal` | — |
| Form Label | `text-label` / `weight-medium` / `leading-normal` | — |
| Input Value | `text-body` / `weight-normal` / `leading-relaxed` | — |
| Button Primary | `text-button` / `weight-semibold` / `leading-normal` | — |
| Button Secondary | `text-button` / `weight-medium` | — |
| Table Header | `text-table-h` / `weight-semibold` / `tracking-wider` | uppercase |
| Table Cell | `text-table` / `weight-normal` / `leading-relaxed` | — |
| Numeric Cell | `text-numeric` / `weight-medium` / `font-mono` | tabular-nums |
| Toast | `text-body-sm` / `weight-normal` | — |
| Tooltip | `text-caption` / `weight-normal` | — |
| Badge | `text-caption` / `weight-medium` | — |

---

## 9. Numeric Formatting (Tabular Figures)

```css
.text-numeric {
  font-variant-numeric: tabular-nums lining-nums;
  font-family: var(--font-family-mono);
}
```

---

## 10. Accessibility

- **Minimum size:** 16px (1rem) for body — no exceptions
- **Contrast:** All text tokens meet 4.5:1 on their paired background
- **Zoom:** 200% browser zoom = no horizontal scroll, no clipping
- **Line height:** ≥ 1.5 for body (WCAG 1.4.8)
- **Letter spacing:** ≥ 0.12em for uppercase (table headers)
- **Font loading:** `font-display: swap` for any future web font

---

## 11. CSS Token Definitions (Reference)

```css
:root {
  /* Families */
  --font-family-sans: system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
  --font-family-mono: ui-monospace, SFMono-Regular, "SF Mono", Menlo, Consolas, "Liberation Mono", monospace;

  /* Scale */
  --text-display: clamp(2.44rem, 2rem + 2vw, 3.05rem);
  --text-h1: clamp(1.95rem, 1.75rem + 1vw, 2.44rem);
  --text-h2: clamp(1.56rem, 1.38rem + 0.9vw, 1.95rem);
  --text-h3: clamp(1.25rem, 1.13rem + 0.6vw, 1.56rem);
  --text-h4: 1.25rem;
  --text-h5: 1.125rem;
  --text-h6: 1rem;
  --text-subtitle: 1rem;
  --text-body-lg: 1.125rem;
  --text-body: 1rem;
  --text-body-sm: 0.875rem;
  --text-caption: 0.8125rem;
  --text-label: 0.8125rem;
  --text-button: 0.875rem;
  --text-table: 0.875rem;
  --text-table-h: 0.75rem;
  --text-code: 0.875rem;
  --text-numeric: 1rem;
  --text-otp: 2rem;

  /* Weights */
  --weight-normal: 400;
  --weight-medium: 500;
  --weight-semibold: 600;
  --weight-bold: 700;

  /* Line Heights */
  --leading-tight: 1.15;
  --leading-snug: 1.25;
  --leading-normal: 1.35;
  --leading-relaxed: 1.5;
  --leading-loose: 1.6;

  /* Tracking */
  --tracking-tight: -0.02em;
  --tracking-snug: -0.01em;
  --tracking-normal: 0;
  --tracking-wide: 0.02em;
  --tracking-wider: 0.05em;
  --tracking-otp: 0.25em;
}
```