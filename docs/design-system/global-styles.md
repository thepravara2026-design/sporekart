# Global Styles

## Files

All global styles live in `src/design-system/styles/`.

| File | Purpose |
|------|---------|
| `reset.css` | CSS reset (box-sizing, margin removal, base font smoothing) |
| `tokens.css` | All CSS custom properties (primitive + semantic + component tokens) |
| `global.css` | Root-level styles (scrollbar, selection, focus ring, body defaults) |
| `utilities.css` | Utility classes (`.sr-only`, `.truncate`, `.no-scrollbar`, `.aspect-*`) |
| `animations.css` | `@keyframes` definitions (fade, slide, scale, spin, pulse) |

## Contents

### CSS Reset

- `box-sizing: border-box` on all elements
- Remove default margin/padding on body, headings, lists
- `font-smoothing: antialiased` on body
- `img`, `video` max-width 100%

### Design Token CSS Variables

All tokens are emitted as `@property` or plain `--custom-properties` in `tokens.css`. They are scoped to `:root` for light and `[data-theme="dark"]` for dark.

```css
:root {
  --color-gray-50: #f9fafb;
  --color-blue-600: #2563eb;
  --space-4: 1rem;
  --font-size-base: 1rem;
  /* ... */
}

[data-theme="dark"] {
  --color-bg-primary: #0f172a;
  --color-text-body: #e2e8f0;
}
```

### Semantic Class Names

- `-ds-*` prefix to avoid collisions
- `-ds-theme-light`, `-ds-theme-dark`, `-ds-theme-high-contrast`
- `-ds-reduced-motion` — applied when `prefers-reduced-motion: reduce`

### Utility Classes

```css
.ds-sr-only { /* screen-reader only */ }
.ds-truncate { /* text overflow ellipsis */ }
.ds-no-scrollbar { /* hide scrollbar */ }
.ds-aspect-square { aspect-ratio: 1/1; }
.ds-aspect-video  { aspect-ratio: 16/9; }
```

### Scrollbar Styling

```css
::-webkit-scrollbar { width: 8px; }
::-webkit-scrollbar-thumb { background: var(--color-gray-300); border-radius: var(--radius-full); }
```

### Selection Colors

```css
::selection { background: var(--color-blue-200); color: var(--color-gray-900); }
```

### Focus Ring

```css
:focus-visible { outline: 2px solid var(--color-border-focus); outline-offset: 2px; }
/* Remove focus ring for mouse clicks */
:focus:not(:focus-visible) { outline: none; }
```

### Reduced Motion

```css
@media (prefers-reduced-motion: reduce) {
  *, *::before, *::after {
    animation-duration: 0.01ms !important;
    transition-duration: 0.01ms !important;
  }
}
```

### Container Queries

```css
@container (min-width: 480px) { /* ... */ }
```

Set via CSS on components: `container-type: inline-size`.

## How to Add New Global Styles

1. Add to the appropriate file in `src/design-system/styles/`.
2. If adding a CSS custom property, also update the token definition in `src/design-system/tokens/`.
3. If adding a utility, add the corresponding TypeScript type in `utils/cx.ts` if used programmatically.
4. Import the new file in `src/design-system/styles/index.ts`.
