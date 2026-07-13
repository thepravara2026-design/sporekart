# Accessibility Guidelines — SporeKart Enterprise Web Application
## Phase 5 / Sprint 19 (Part 1C): UX Standards & Accessibility Foundation

> **Status:** WCAG 2.2 AA mandatory. Quick-reference for developers. Full standard in `accessibility-standards.md`.

---

## 1. Conformance Checklist (Per Page)

| Requirement | WCAG | Test |
|-------------|------|------|
| Page title unique & descriptive | 2.4.2 | Manual |
| Heading hierarchy (H1→H2→H3, no skips) | 1.3.1 | axe |
| All images have alt (decorative: `alt=""`) | 1.1.1 | axe |
| Form labels explicit (`<label for>` or wrap) | 1.3.1, 3.3.2 | axe |
| Color contrast ≥ 4.5:1 (text), 3:1 (UI) | 1.4.3 | axe |
| Focus visible on all interactive | 2.4.7 | Keyboard |
| Focus order matches visual | 2.4.3 | Keyboard |
| No keyboard traps (except modal) | 2.1.2 | Keyboard |
| Skip link works | 2.4.1 | Keyboard |
| Landmarks: banner, nav, main, contentinfo | 1.3.1 | axe |
| ARIA valid, no redundant roles | 4.1.2 | axe |
| Language declared (`lang="en"`) | 3.1.1 | axe |
| Resize text 200% no horizontal scroll | 1.4.4 | Manual |
| `prefers-reduced-motion` respected | 2.3.3 | Manual |

---

## 2. Component Patterns (A11y Required)

| Component | Key Attributes |
|-----------|----------------|
| **Button** | `<button type="button">`, `aria-pressed` (toggle), `aria-expanded` (menu), `disabled` attr |
| **Link** | `<a href="...">`, never `href="#"`, `aria-current="page"` on active |
| **Input** | `id` + `<label for>`, `aria-describedby` (hint/error), `aria-invalid`, `aria-required`, `autocomplete` |
| **Select (native)** | `<select>`, `<option>`, `<label for>` |
| **Combobox** | `role="combobox"`, `aria-expanded`, `aria-controls`, `aria-activedescendant`, keyboard per ARIA 1.2 |
| **Dialog/Modal** | `<dialog>` or `role="dialog" aria-modal="true"`, focus trap, `Esc` close, restore focus |
| **Tabs** | `role="tablist"`, `role="tab" aria-selected`, `role="tabpanel" aria-labelledby`, arrow keys |
| **Menu** | `role="menu"`, `role="menuitem"`, `aria-expanded` on trigger, arrow keys, `Esc` close |
| **Table** | `<caption>`, `<thead><th scope="col">`, `<tbody>`, sortable: `<button aria-sort>` |
| **Toast** | `role="status"` (info/success), `role="alert"` (error), `aria-live="polite/assertive"` |
| **Breadcrumb** | `<nav aria-label="Breadcrumb"><ol><li><a>…</a></li><li aria-current="page">…</li></ol></nav>` |
| **Progress** | `<progress value max>` or `role="progressbar" aria-valuenow aria-valuemin aria-valuemax` |
| **Tooltip** | `role="tooltip"`, `aria-describedby` on trigger, show on hover/focus, delay 200ms |
| **Skip Link** | `<a href="#main" class="skip-link">Skip to content</a>`, first focusable |

---

## 3. Focus Management Rules

| Event | Action |
|-------|--------|
| Route change (SPA) | `document.getElementById('main').focus()` |
| Modal open | Focus first focusable (or close button) |
| Modal close | Focus trigger element |
| Palette open | Focus search input |
| Toast appear | **No focus steal** |
| Error submit | Focus first `aria-invalid="true"` |
| Drawer open | Focus first link in drawer |
| Expand/collapse | Focus stays on trigger |

---

## 4. Color & Contrast (Token Reference)

| Use | Minimum | Token (Part 1D) |
|-----|---------|-----------------|
| Body text | 4.5:1 | `--color-text-primary` on `--color-surface` |
| Large text (≥18px/14px bold) | 3:1 | `--color-text-primary` on `--color-surface` |
| UI borders, icons | 3:1 | `--color-border` on `--color-surface` |
| Focus ring | 3:1 vs adjacent | `--color-focus-ring` |
| Error text | 4.5:1 | `--color-error` on `--color-surface` |
| Success text | 4.5:1 | `--color-success` on `--color-surface` |
| Disabled text | — | `--color-text-disabled` (no contrast req) |

---

## 5. Motion & Animation

| Feature | Requirement |
|---------|-------------|
| Transitions | ≤ 300ms, `ease-out`, respect `prefers-reduced-motion: reduce` |
| Skeleton shimmer | Static if reduced-motion |
| Auto-play carousel/video | Pause on hover/focus; never auto-play >5s without controls |
| Scroll animations | Disabled if reduced-motion |
| Parallax | Disabled if reduced-motion |

---

## 6. Testing Checklist (Pre-Gate 3)

| Tool | Run On | Pass Criteria |
|------|--------|---------------|
| `axe-core` (playwright) | PR, nightly | 0 violations (AA) |
| `eslint-plugin-jsx-a11y` | PR, build | 0 errors |
| Storybook a11y addon | Component dev | 0 violations per story |
| Lighthouse CI | PR | Accessibility score = 100 |
| Keyboard-only | Manual (Gate 3) | All tasks completable |
| NVDA (Windows) | Manual (Gate 3) | All content announced |
| VoiceOver (Mac/iOS) | Manual (Gate 3) | All content announced |
| Zoom 200% | Manual | No horizontal scroll, no overlap |

---

## 7. Accessibility Statement Template

> **SporeKart Accessibility Statement**
>
> We aim for WCAG 2.2 AA conformance. Last audit: [date]. Known issues: [none]. Report barriers: accessibility@sporekart.com. We respond within 5 business days.