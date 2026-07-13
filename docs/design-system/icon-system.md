# Icon System

## Architecture

Icons use a **registry pattern**: each SVG is an individual React component registered by name in a central map. The `<Icon>` component looks up the registry by name and renders the matching component.

```
icons/
├── assets/        # Raw SVG files
├── createIcon.tsx # Factory function to wrap SVGs into React components
├── registry.ts    # Central map: { iconName: SvgComponent }
├── types.ts       # IconProps interface
└── index.ts       # Public API
```

## Available Icons

The icon set includes (but is not limited to):

- **Navigation**: `chevron-left`, `chevron-right`, `chevron-down`, `chevron-up`, `menu`, `close`, `arrow-left`, `arrow-right`
- **Actions**: `plus`, `minus`, `edit`, `trash`, `search`, `download`, `upload`, `share`
- **Status**: `check`, `alert-circle`, `info`, `warning`, `x-circle`
- **Media**: `play`, `pause`, `stop`, `volume`, `camera`, `image`
- **Commerce**: `cart`, `tag`, `credit-card`, `truck`, `star`
- **Social**: `user`, `users`, `bell`, `settings`, `heart`
- **Misc**: `clock`, `calendar`, `file`, `folder`, `external-link`, `more-horizontal`

Full list: see `registry.ts`.

## Icon Component API

```tsx
interface IconProps {
  name: string;                           // registry key
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl' | '2xl';
  color?: string;                         // any CSS color value
  decorative?: boolean;                   // true = aria-hidden, false = aria-label required
  label?: string;                         // aria-label when decorative={false}
  className?: string;
}
```

## Size Tokens

| Name | Pixels |
|------|--------|
| `xs` | 12 |
| `sm` | 16 |
| `md` | 20 |
| `lg` | 24 |
| `xl` | 32 |
| `2xl` | 40 |

## Color Tokens

By default, icons inherit `currentColor`. Pass any CSS color string to `color` or use a token: `var(--color-text-muted)`.

## Accessibility

- **Decorative icons** (`decorative={true}`, default): rendered with `aria-hidden="true"`.
- **Semantic icons** (`decorative={false}`): require a `label` prop which sets `aria-label`.
- All SVGs have `focusable="false"` to prevent focus in IE/Edge.

## How to Add a New Icon

1. Add the raw SVG to `icons/assets/<name>.svg`
2. Run `npm run generate-icons` (or manually use `createIcon` to wrap the SVG)
3. Register it in `icons/registry.ts`: `'my-icon': MyIcon`
4. The icon is immediately available as `<Icon name="my-icon" />`

## Tree-Shaking

Each icon is a separate React component. The registry uses dynamic `import()` for code-splitting at the route level where possible. Direct imports (`import { ChevronLeft } from '@/design-system/icons'`) enable per-icon tree-shaking without bundling the full registry.
