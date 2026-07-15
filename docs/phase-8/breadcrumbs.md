# Admin Breadcrumb System

## Implementation

The admin breadcrumb system uses the design system's `Breadcrumb` component with a custom builder function.

### Builder Function

```tsx
function buildAdminBreadcrumbs(pathname: string): Crumb[]
```

Logic:
1. Always starts with "Admin" crumb → `/admin/dashboard`
2. Parses pathname segments after `/admin`
3. Maps each segment to a label + href using a label dictionary
4. If segment isn't in the dictionary, uses the raw segment
5. Supports arbitrary nesting for future sub-pages

### Breadcrumb Component

```tsx
<Breadcrumb
  crumbs={crumbs}
  maxItems={4}
  collapsedLabel="…"
  aria-label="Breadcrumb"
/>
```

### Features
- **Auto-detection**: Breadcrumbs generated from React Router pathname
- **Responsive collapse**: When crumbs exceed `maxItems`, intermediate crumbs collapse into a "…" button with tooltip
- **Icons**: Each crumb can include an icon (via Crumb.icon)
- **Last crumb**: Rendered as text with `aria-current="page"` (not a link)
- **Chevron separators**: SVG chevrons between crumbs

### Examples

| Path | Breadcrumbs |
|---|---|
| `/admin/dashboard` | Admin > Dashboard |
| `/admin/settings` | Admin > Settings |
| `/admin/system` | Admin > System |
| `/admin/profile` | Admin > Profile |
