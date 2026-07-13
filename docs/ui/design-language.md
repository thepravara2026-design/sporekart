# Design Language — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1D): Enterprise Design Language & Brand Guidelines

> **Status:** Mandatory visual language. Every pixel in the product must justify its existence against these principles.

---

## 1. Visual Tone & Personality

| Attribute | Target | Anti-Pattern |
|-----------|--------|--------------|
| **Premium** | Quality evident in restraint, not ornament | Gold accents, drop shadows for "luxury" |
| **Modern** | Contemporary patterns, not dated enterprise | Heavy borders, beveled buttons, gradients |
| **Minimal** | Only what serves the task | Decorative dividers, filler icons, visual noise |
| **Scientific** | Data presented with rigor, honesty | Vanity metrics, pseudo-charts, inflated claims |
| **Trustworthy** | Transparent, predictable, secure by design | Hidden fees, dark patterns, misleading CTAs |
| **Natural** | Earth/lab palette; organic feel without kitsch | Neon greens, synthetic textures, stock photos |
| **Professional** | Enterprise gravity with warmth | Cold, bureaucratic, intimidating |
| **Elegant** | Proportion, restraint, intent | Flashy animations, excessive whitespace |
| **Human** | Respectful of farmer, grower, business | Jargon-heavy, academic, sterile |
| **Calm** | Low cognitive load; generous whitespace | Urgency theater, clutter, competing CTAs |
| **Farmer-Friendly** | Works for low-literacy, vernacular, low-bandwidth | Complex language, tiny tap targets, heavy assets |

---

## 2. Shape Language

| Element | Specification |
|---------|---------------|
| **Corner Radius** | 4px base unit → 4, 8, 12, 16, 24, 9999 (pill) |
| **Edge Softness** | Subtle rounding; never sharp (0) or bubble (9999 except pills) |
| **Component Geometry** | Rectangular containers; buttons/input = 8px; cards = 12px; dialogs = 16px |
| **Border Strategy** | 1px solid for structure; 2px for focus; no double borders; semantic color |
| **Divider Philosophy** | Space > lines; 1px divider only when space insufficient |
| **Whitespace** | 4px base unit; 4/8/12/16/24/32/48/64 scale; never arbitrary px |
| **Surface Philosophy** | Layered: bg (0) → surface (1) → raised (2) → overlay (3) → modal (4) |
| **Depth Philosophy** | Elevation = shadow + surface tint; no fake 3D, no heavy drop shadows |
| **Content Density** | Comfortable default (16px base); compact opt-in (12px); never cramped |

---

## 3. Visual Hierarchy Rules

| Level | Treatment | Example |
|-------|-----------|---------|
| **Primary Action** | Filled, brand color, 600 weight, 16px | "Place Order" |
| **Secondary Action** | Outline, brand color, 500 weight, 16px | "Cancel" |
| **Supporting Action** | Ghost, muted color, 500 weight, 14px | "View details" |
| **Destructive Action** | Filled, danger color, 600 weight | "Delete" |
| **H1 (Page Title)** | 32px/40px, 700 weight, brand dark | "Orders" |
| **H2 (Section)** | 24px/32px, 600 weight | "Order Details" |
| **H3 (Subsection)** | 20px/28px, 600 weight | "Line Items" |
| **Body** | 16px/24px, 400 weight | Description text |
| **Caption/Label** | 14px/20px, 500 weight, uppercase tracking | "ORDER DATE" |
| **Numeric/Data** | Tabular nums, 16px/24px, 500 weight | "₹1,250.00" |

---

## 4. Application Rules (Per Screen)

1. **One primary action** per view — visually dominant, top-right (desktop) / bottom (mobile)
2. **Space creates hierarchy** — no boxes where padding suffices
3. **Color carries meaning** — never decorative; semantic only
4. **Consistent density** — 16px base rhythm; all components align to 4px grid
5. **Calm surfaces** — white/off-white backgrounds; subtle borders; no patterns
6. **Respect the farmer** — large tap targets (≥48px), clear labels, vernacular-ready
7. **Trust through honesty** — skeletons not spinners; real units; source attribution
8. **Scientific restraint** — data tables over charts where precision matters
9. **Elegant minimalism** — remove until it breaks; then add back the essential
10. **Premium feel** — perfect alignment, consistent radii, smooth transitions (150-300ms)

---

## 5. Do / Don't Quick Reference

| Do | Don't |
|----|-------|
| Use 4px spacing scale exclusively | Use 5px, 6px, 7px, 9px, etc. |
| Use semantic color tokens | Hardcode `#2f6f4f` or `#fff` |
| One primary button per view | Two primary buttons side by side |
| Space as divider (16px+ gap) | 1px lines everywhere |
| Rounded corners (8px/12px) | Sharp corners (0px) or pills everywhere |
| Subtle shadows (elevation 1-2) | Heavy drop shadows (elevation 4+) |
| Tabular numerals for data | Proportional numerals in tables |
| System font stack + 1 brand font | Multiple font families |
| 200% zoom without horizontal scroll | Fixed px containers that break |
| Skeleton loading | "Loading..." spinner on white |