# Illustration Guidelines — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1D): Enterprise Design Language & Brand Guidelines

> **Status:** Mandatory illustration strategy. Consistent visual language across all illustrated moments.

---

## 1. Design Principles

1. **Purposeful** — Every illustration explains, guides, or reassures; never decorative filler
2. **Scientific Minimal** — Line art, single accent color, geometric — not pastoral stock photos
3. **Farmer-Friendly** — Recognizable at small sizes; works cross-culturally
3. **Token-Driven** — Colors from semantic palette; sizes from spacing tokens
4. **Systematic** — Reusable components (characters, objects, scenes) compose illustrations

---

## 2. Visual Style

| Property | Specification |
|----------|---------------|
| **Style** | Minimal line art (2px stroke) + single accent fill |
| **Stroke** | 2px (matches icon stroke), round caps/joins |
| **Corner Radius** | 4px (matches `--radius-md`) |
| **Color Palette** | Neutral stroke (`--color-text-secondary`) + **one** semantic accent |
| **Accent Colors** | Primary (green), Success, Warning, Danger, Info — per context |
| **Background** | Transparent (on surface) or `--color-surface` |
| **Perspective** | Flat / isometric (2:1) — no 3D, no drop shadows |
| **Characters** | Abstract geometric (circle head, rounded rect body) — no realistic humans |
| **Objects** | Simplified to essential silhouette (seed, leaf, box, truck, flask) |

---

## 3. Illustration Categories

### 3.1 Empty States (Standard)
- **Size:** 120×120px (token: `--space-30` / 120px)
- **Composition:** Single centered object + subtle ground line
- **Accent:** Primary (green) for "create first" actions; Neutral for "no results"
- **Variants:** See `empty-state-guidelines.md` for 30+ states

| Context | Illustration | Accent |
|---------|--------------|--------|
| No orders | Open box with receipt | Primary |
| No products | Seed packet + label | Primary |
| No trainings | Calendar + play triangle | Primary |
| No AI chats | Speech bubble + sparkle | Primary |
| No results | Magnifying glass + void | Neutral |
| No access | Lock + shield | Warning |
| Error empty | Warning triangle + cable | Danger |

### 3.2 Success / Confirmation
- **Size:** 80×80px (toast), 120×120px (modal)
- **Composition:** Checkmark circle + contextual object
- **Accent:** Success (green)

| Context | Illustration |
|---------|--------------|
| Order placed | Checkmark + box + truck |
| Profile saved | Checkmark + user card |
| Payment success | Checkmark + shield + rupee |
| Sync complete | Checkmark + cloud + arrows |

### 3.3 Error / Offline / Warning
- **Size:** 80×80px (toast), 120×120px (modal), 160×160px (page)
- **Composition:** Warning triangle / alert circle + contextual cue
- **Accent:** Danger (error), Warning (offline/retry), Amber (caution)

| Context | Illustration |
|---------|--------------|
| 404 | Magnifying glass over void |
| 403 | Lock + shield |
| 500 | Server rack + warning |
| Offline | Wifi-off + cloud + pause |
| Timeout | Clock + warning triangle |
| Conflict | Two overlapping boxes + alert |

### 3.4 Onboarding / First-Time Empty
- **Size:** 160×160px (larger, friendlier)
- **Composition:** Character + object + subtle environment hint
- **Accent:** Primary + secondary accent
- **Tone:** Welcoming, guiding

### 3.5 Feature Illustrations (Agriculture)
| Domain | Visual Language |
|--------|-----------------|
| **Seeds/Inputs** | Seed packet, droplet, flask, sprout — line art, green accent |
| **Crops/Growth** | Leaf stages (sprout → mature), sun, soil layers — isometric |
| **Soil/Testing** | Flask, test tube, soil probe, data points — lab aesthetic |
| **Weather** | Sun, cloud, rain, wind — minimal meteorological symbols |
| **Machinery** | Tractor, harvester, sprayer — simplified silhouette |
| **Logistics** | Truck, warehouse, box, pallet, route line — supply chain |

### 3.6 AI / Intelligence
| Concept | Visual Language |
|---------|-----------------|
| **Assistant** | Sparkle + speech bubble + subtle neural nodes |
| **Thinking** | Pulsing dots / rotating gears (reduced motion: static) |
| **Knowledge/RAG** | Stack of documents + neural connections |
| **Prompt** | Magic wand + text cursor |
| **Model** | Abstract brain / layered nodes |

### 3.7 Governance / Compliance
| Concept | Visual Language |
|---------|-----------------|
| **Policy** | Shield + document + checkmark |
| **Approval** | Clipboard + check + user avatar |
| **Compliance** | Checklist + shield + green check |
| **Audit** | Magnifying glass + ledger + check |
| **Access** | Key + shield + user group |

---

## 4. Composition Rules

1. **Center-weighted** — Illustrations centered in container
2. **Breathing room** — 24px padding around illustration in card/modal
3. **Aspect ratio** — 1:1 (square) for empty states; 4:3 for feature scenes
4. **Scale consistency** — Same object = same relative size across illustrations
5. **Ground line** — Subtle 1px curve at bottom 15% (anchors floating objects)

---

## 5. Tokenized Sizes

| Token | Dimensions | Use Case |
|-------|------------|----------|
| `illustration-xs` | 64×64px | Toast, inline |
| `illustration-sm` | 80×80px | Toast, modal icon |
| `illustration-md` | 120×120px | **Standard empty state** |
| `illustration-lg` | 160×160px | First-time empty, page error |
| `illustration-xl` | 240×240px | Onboarding hero, landing |

---

## 6. Accessibility

- **Decorative:** `aria-hidden="true"` + `role="img"` with `<title>` in SVG
- **Informative:** If illustration conveys unique info not in text, add `aria-labelledby` pointing to visible heading
- **Color:** Never sole meaning — always paired with text
- **Reduced Motion:** No animated illustrations (or static fallback)
- **High Contrast:** Stroke width increases to 3px in HC mode

---

## 7. SVG Implementation

```html
<!-- Standard empty state illustration -->
<svg
  class="illustration illustration-md"
  viewBox="0 0 120 120"
  aria-hidden="true"
  role="img"
>
  <title>No orders — open box with receipt</title>
  <!-- Ground line -->
  <path d="M10 90 Q60 95 110 90" stroke="var(--color-border)" stroke-width="1" fill="none"/>
  <!-- Box -->
  <rect x="35" y="45" width="50" height="40" rx="4" stroke="var(--color-text-secondary)" stroke-width="2" fill="none"/>
  <!-- Receipt -->
  <path d="M50 45 L50 30" stroke="var(--color-primary)" stroke-width="2" stroke-linecap="round"/>
  <rect x="40" y="18" width="20" height="20" rx="2" stroke="var(--color-primary)" stroke-width="2" fill="none"/>
  <path d="M45 28 L55 28 M45 34 L55 34" stroke="var(--color-primary)" stroke-width="1.5"/>
</svg>
```

---

## 8. Do / Don't

| Do | Don't |
|----|-------|
| Use semantic accent color from token | Use brand green everywhere |
| Keep stroke 2px consistent | Mix stroke weights |
| Abstract geometric characters | Realistic human figures |
| Single accent per illustration | Multi-color decorative |
| Transparent background | White/colored background box |
| Reuse object library | Draw unique each time |