# Typography

## Type Scale

| Style | Size | Line Height | Weight | Letter Spacing |
|-------|------|-------------|--------|----------------|
| **display** | clamp(2.5rem, 5vw, 4rem) | 1.1 | 700 | -0.02em |
| **h1** | clamp(2rem, 4vw, 3rem) | 1.2 | 700 | -0.02em |
| **h2** | clamp(1.5rem, 3vw, 2.25rem) | 1.25 | 600 | -0.01em |
| **h3** | clamp(1.25rem, 2.5vw, 1.75rem) | 1.3 | 600 | -0.01em |
| **h4** | clamp(1.125rem, 2vw, 1.5rem) | 1.35 | 600 | 0 |
| **h5** | clamp(1rem, 1.5vw, 1.25rem) | 1.4 | 500 | 0 |
| **h6** | clamp(0.875rem, 1vw, 1rem) | 1.4 | 500 | 0 |
| **subtitle** | 1.125rem | 1.5 | 500 | 0 |
| **body-lg** | 1.125rem | 1.6 | 400 | 0 |
| **body** | 1rem | 1.6 | 400 | 0 |
| **body-sm** | 0.875rem | 1.5 | 400 | 0 |
| **caption** | 0.75rem | 1.4 | 400 | 0.01em |
| **label** | 0.875rem | 1.25 | 500 | 0.01em |
| **button** | 0.875rem | 1 | 600 | 0.02em |
| **table** | 0.875rem | 1.4 | 400 | 0 |
| **code** | 0.875rem | 1.5 | 400 | 0 |
| **numeric** | 1rem | 1 | 600 | 0.02em (tabular-nums) |
| **otp** | 1.5rem | 1 | 700 | 0.1em |

## Font Families

```css
--font-sans:  'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
--font-mono:  'JetBrains Mono', 'Fira Code', 'Consolas', monospace;
--font-brand: 'Cabinet Grotesk', 'Inter', sans-serif;
```

## Font Weights

| Token | Value |
|-------|-------|
| `--font-weight-regular` | 400 |
| `--font-weight-medium` | 500 |
| `--font-weight-semibold` | 600 |
| `--font-weight-bold` | 700 |

## Usage

```tsx
import { Text } from '@/design-system';

<Text as="h1">Heading</Text>
<Text variant="body">Body text</Text>
<Text variant="caption" color="muted">Caption</Text>
<Text variant="numeric">42</Text>
```

```css
/* Direct CSS */
.title {
  font-family: var(--font-sans);
  font-size: var(--font-size-h2);
  font-weight: var(--font-weight-semibold);
  line-height: 1.25;
}
```

## Responsive Behavior

All display and heading sizes use `clamp()` for fluid scaling between viewport widths. Body text uses static `rem` values for readability.
