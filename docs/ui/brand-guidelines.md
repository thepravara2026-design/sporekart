# Brand Guidelines — SporeKart Enterprise AI Platform
## Phase 5 / Sprint 19 (Part 1D): Enterprise Design Language & Brand Guidelines

> **Status:** Mandatory brand standards. All visual and verbal expression follows this.

---

## 1. Brand Identity

### 1.1 Brand Essence
**SporeKart** — The trusted agricultural intelligence platform that brings scientific rigor to every farmer, grower, trainer, and business in India's food ecosystem.

### 1.2 Brand Promise
"Confidence at every interaction."

### 1.3 Brand Personality
| Dimension | Position |
|-----------|----------|
| **Scientific** | Evidence-based, precise, honest |
| **Grounded** | Rooted in Indian agriculture, not abstract tech |
| **Calm** | No urgency theater; steady, reliable |
| **Inclusive** | Works for the farmer with a basic phone AND the enterprise admin |
| **Premium** | Craft quality in every detail, not ornamentation |

---

## 2. Logo System

### 2.1 Primary Mark
```
❖ SporeKart
```
- **Mark:** Diamond (❖) — represents a spore, a seed, a data point
- **Wordmark:** Custom geometric sans, medium weight
- **Clear Space:** 1× mark height on all sides
- **Minimum Width:** 160px (digital), 30mm (print)

### 2.2 Logo Variants
| Variant | Use Case |
|---------|----------|
| **Full (Mark + Wordmark)** | Default, headers, sign-in, marketing |
| **Mark Only** | Favicon, app icon, avatar fallback, tight spaces |
| **Wordmark Only** | Where mark is redundant (e.g., footer with mark nearby) |
| **Monochrome** | Single-color printing, watermarks, low-contrast backgrounds |

### 2.4 Do / Don't
| Do | Don't |
|----|-------|
| Use approved SVG only | Recreate in CSS/Canvas |
| Maintain clear space | Crowd with other elements |
| Use monochrome on busy backgrounds | Apply drop shadows, gradients, outlines |
| Scale proportionally | Stretch, skew, rotate |

---

## 3. Color Palette (Reference — Tokens in color-system.md)

| Role | Token | Light Theme | Usage |
|------|-------|-------------|-------|
| **Primary** | `--color-primary` | `#2F6F4F` | Primary actions, key accents, focus rings |
| **Primary Hover** | `--color-primary-hover` | `#265D42` | Button hover |
| **Primary Pressed** | `--color-primary-pressed` | `#1D4D36` | Button active |
| **Surface** | `--color-surface` | `#FFFFFF` | Cards, panels, modals |
| **Background** | `--color-background` | `#F7F8F7` | Page background |
| **Border** | `--color-border` | `#E3E6E3` | Dividers, input borders |
| **Text Primary** | `--color-text-primary` | `#1D2B22` | Headings, body |
| **Text Secondary** | `--color-text-secondary` | `#5D6B62` | Captions, hints |
| **Success** | `--color-success` | `#2E7D32` | Positive states |
| **Warning** | `--color-warning` | `#F57F17` | Caution states |
| **Danger** | `--color-danger` | `#C62828` | Errors, destructive |
| **Info** | `--color-info` | `#1565C0` | Informational |

---

## 4. Typography (Reference — Tokens in typography.md)

| Role | Font |
|------|------|
| **UI / Body** | System UI stack (`system-ui, -apple-system, "Segoe UI", Roboto, sans-serif`) |
| **Brand / Display** | `Inter` (self-hosted, WOFF2, subset Latin + Devanagari) |
| **Mono / Data** | `JetBrains Mono` (WOFF2, for IDs, codes, tabular nums) |

**Scaling:** Modular scale 1.25 ratio from 16px base. Responsive clamp at breakpoints.

---

## 5. Voice & Tone (Reference — Full in microcopy-guidelines.md)

| Context | Tone |
|---------|------|
| **Onboarding / Empty** | Encouraging, guiding, simple |
| **Errors / Warnings** | Direct, calm, actionable, no blame |
| **Success** | Warm, brief, confirmatory |
| **Settings / Admin** | Authoritative, precise |
| **Help / Tooltips** | Instructive, scannable |
| **Legal / Compliance** | Formal, exact |

**Lexicon Lock:** Order / Product / Customer / Catalog / Training / Workspace / Governance / Analytics / AI Assistant — never substitute.

---

## 6. Iconography (Reference — Tokens in iconography.md)

- **Style:** 2px stroke, rounded caps/joins, 24×24px viewBox
- **Weights:** Outline (default), Filled (active/selected states only)
- **Color:** `currentColor` — inherits text color
- **Sizing:** 16, 20, 24, 28, 32px tokens

---

## 7. Imagery & Illustration (Reference — illustration-guidelines.md)

| Category | Style |
|----------|-------|
| **Empty States** | Minimal line art, single accent color, 120×120px |
| **Success** | Checkmark + subtle celebration, green accent |
| **Error/Offline** | Warning triangle + helpful gesture, amber/red accent |
| **Agriculture** | Scientific line drawings (seed, leaf, soil, crop) — not pastoral stock |
| **AI/Governance** | Abstract geometric (nodes, shields, flows) — not robots |

---

## 8. Motion (Reference — Tokens in design-tokens.md)

| Property | Value |
|----------|-------|
| **Duration** | 150ms (micro), 200ms (standard), 300ms (macro) |
| **Easing** | `cubic-bezier(0.2, 0, 0.2, 1)` (Material standard) |
| **Reduced Motion** | All transitions ≤ 50ms or instant; no parallax |

---

## 9. Accessibility Commitment

- WCAG 2.2 AA baseline (contrast, focus, semantics)
- No color-only information
- All motion respects `prefers-reduced-motion`
- 200% zoom without horizontal scroll
- Screen reader tested (NVDA, VoiceOver)

---

## 10. Co-Branding / Partner Guidelines

- Partner logo: max 50% height of SporeKart mark
- Clear space: 1× SporeKart mark height between marks
- Never combine marks into single lockup
- "Powered by SporeKart" lockup available for integrations