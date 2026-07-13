# Layout Helpers

## Overview

Layout helper components provide declarative, responsive layout primitives that map directly to CSS Flexbox and Grid, using design tokens for spacing.

## Stack

Vertical flex layout with consistent gap.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| gap | `SpacingToken \| string` | `'md'` | Vertical gap between children |
| align | `'start' \| 'center' \| 'end' \| 'stretch'` | `'stretch'` | Cross-axis alignment |
| justify | `'start' \| 'center' \| 'end' \| 'between' \| 'around'` | `'start'` | Main-axis distribution |
| as | `'div' \| 'section' \| 'article' \| 'nav' \| 'form'` | `'div'` | Semantic HTML element |
| className | `string` | — | Additional CSS classes |

```tsx
<Stack gap="lg" align="center">
  <Card>...</Card>
  <Card>...</Card>
  <Card>...</Card>
</Stack>
```

## Inline

Horizontal flex layout with wrapping.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| gap | `SpacingToken \| string` | `'sm'` | Horizontal gap |
| wrap | `boolean` | `true` | Allow wrapping |
| align | `'start' \| 'center' \| 'end' \| 'stretch' \| 'baseline'` | `'center'` | Cross-axis alignment |
| as | `'div' \| 'span' \| 'nav'` | `'div'` | Semantic element |
| className | `string` | — | Additional CSS classes |

```tsx
<Inline gap="md" wrap align="center">
  <Chip label="Organic" />
  <Chip label="Fresh" />
  <Chip label="Seasonal" />
</Inline>
```

## Cluster

Auto-wrapping layout with consistent gap, ideal for tag/chip groupings.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| gap | `SpacingToken \| string` | `'xs'` | Gap between items |
| minItemWidth | `string` | `'auto'` | Minimum item width before wrapping |
| className | `string` | — | Additional CSS classes |

```tsx
<Cluster gap="xs" minItemWidth="80px">
  {tags.map((tag) => <Tag key={tag} label={tag} />)}
</Cluster>
```

## Grid

CSS Grid layout with auto-fit or fixed column count.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| columns | `number \| 'auto-fit' \| 'auto-fill'` | `'auto-fit'` | Column configuration |
| minColumnWidth | `string` | `'280px'` | Min column width (auto modes) |
| gap | `SpacingToken \| string` | `'md'` | Grid gap |
| as | `'div' \| 'section' \| 'ul'` | `'div'` | Semantic element |
| className | `string` | — | Additional CSS classes |

```tsx
<Grid columns={3} gap="lg">
  <ProductCard ... />
  <ProductCard ... />
  <ProductCard ... />
</Grid>

// Responsive auto-fit grid
<Grid columns="auto-fit" minColumnWidth="300px" gap="md">
  {products.map((p) => <ProductCard key={p.id} {...p} />)}
</Grid>
```

## Container

Max-width centered container.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| maxWidth | `'sm' \| 'md' \| 'lg' \| 'xl' \| '2xl' \| 'full'` | `'xl'` | Container max-width |
| padding | `boolean` | `true` | Apply responsive horizontal padding |
| as | `'div' \| 'main' \| 'section'` | `'div'` | Semantic element |
| className | `string` | — | Additional CSS classes |

Max-widths map to breakpoints:
- `sm`: 640px
- `md`: 768px
- `lg`: 1024px
- `xl`: 1280px
- `2xl`: 1536px
- `full`: 100%

```tsx
<Container maxWidth="lg">
  <Stack gap="lg">
    <PageHeader title="Orders" />
    <Table columns={orderColumns} data={orders} />
  </Stack>
</Container>
```

## Composition Examples

### Dashboard Layout

```tsx
<Container maxWidth="2xl">
  <Stack gap="lg">
    <Inline gap="md" wrap>
      <StatCard label="Revenue" value="₹1.2L" trend="up" />
      <StatCard label="Orders" value="342" trend="up" />
      <StatCard label="Customers" value="89" trend="neutral" />
    </Inline>
    <Grid columns="auto-fit" minColumnWidth="320px" gap="md">
      <OrderCard ... />
      <OrderCard ... />
    </Grid>
  </Stack>
</Container>
```

### Filter Bar

```tsx
<Inline gap="sm" wrap align="center">
  <Cluster gap="xs">
    {activeFilters.map((f) => (
      <Chip key={f} type="removable" label={f} onRemove={() => {}} />
    ))}
  </Cluster>
  <SearchInput placeholder="Filter..." />
</Inline>
```

## Responsive Behavior

Layout helpers natively responsive:
- **Stack** — collapses to vertical always
- **InLine** — wraps children on small viewports
- **Grid** — `auto-fit` / `auto-fill` produce responsive columns without media queries
- **Container** — padding scales via `clamp()`; max-width prevents content from exceeding reading width

## Design Tokens

| Token | Mapping |
|-------|---------|
| `--layout-gap` | Spacing tokens (`--spacing-{xs/sm/md/lg/xl}`) |
| `--layout-container-padding` | `--spacing-md` (mobile), `--spacing-lg` (desktop) |
| `--layout-grid-gap` | `--spacing-md` |
| `--layout-inline-gap` | `--spacing-sm` |
