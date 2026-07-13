# Application Shell

## Overview

The Application Shell (`AppShell`) is the top-level layout wrapper that provides the structural foundation for every page in SporeKart. It manages the header, sidebar, content area, and auxiliary panels within a responsive CSS grid.

## Architecture

```
+----------------------------------------------------------+
| Header (sticky)                                           |
+------------------+---------------------------------------+
| Sidebar          | ContentContainer                      |
| (collapsible)    | +-----------------------------------+ |
|                  | | PageContainer                     | |
|                  | | +-------------------------------+ | |
|                  | | | PageHeader                    | | |
|                  | | | PageToolbar                   | | |
|                  | | +-------------------------------+ | |
|                  | | | SectionContainer              | | |
|                  | | | +---------------------------+ | | |
|                  | | | | ScrollableContent         | | | |
|                  | | | +---------------------------+ | | |
|                  | | +-------------------------------+ | |
|                  | +-----------------------------------+ |
|                  | PageFooter (sticky bottom)            |
+------------------+---------------------------------------+
```

## AppShell

The root layout component.

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `header` | `ReactNode` | — | Header component |
| `sidebar` | `ReactNode` | — | Sidebar component |
| `children` | `ReactNode` | — | Main content |
| `sidebarCollapsed` | `boolean` | `false` | Initial sidebar state |
| `onToggleSidebar` | `() => void` | — | Sidebar toggle callback |
| `maxWidth` | `ContainerSize` | `'xl'` | Content max width |
| `className` | `string` | — | Additional CSS classes |

### Responsive Breakpoints

| Breakpoint | Sidebar | Header | Content |
|------------|---------|--------|---------|
| `xs` (0–639px) | Overlay drawer | Compact | Full width |
| `sm` (640–767px) | Overlay drawer | Compact | Full width |
| `md` (768–1023px) | Mini variant | Standard | Padded |
| `lg` (1024–1279px) | Expanded | Standard | Max-width 960px |
| `xl` (1280–1535px) | Expanded | Standard | Max-width 1200px |
| `2xl` (1536px+) | Expanded | Standard | Max-width 1400px |

## ContentContainer

Wraps the main content area with max-width constraints and padding.

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `children` | `ReactNode` | — | Content children |
| `maxWidth` | `ContainerSize` | `'xl'` | Max width constraint |
| `padding` | `SpacingToken` | `'page'` | Padding token |
| `className` | `string` | — | Additional CSS classes |

## PageContainer

Provides vertical spacing and structure for a single page's content stack.

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `children` | `ReactNode` | — | Page content |
| `spacing` | `SpacingToken` | `'page'` | Gap between sections |
| `className` | `string` | — | Additional CSS classes |

## SectionContainer

Groups related content within a page. Optionally renders a section heading.

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `title` | `string` | — | Section heading text |
| `description` | `string` | — | Section description |
| `actions` | `ReactNode` | — | Action buttons/links |
| `children` | `ReactNode` | — | Section content |
| `variant` | `'default' \| 'card' \| 'bordered'` | `'default'` | Visual variant |
| `className` | `string` | — | Additional CSS classes |

## PageHeader

Displays page title, description, breadcrumbs, and primary actions.

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `title` | `string` | — | Page title |
| `description` | `string` | — | Page description |
| `breadcrumbs` | `Crumb[]` | — | Breadcrumb trail |
| `actions` | `ReactNode` | — | Primary action buttons |
| `icon` | `ReactNode` | — | Leading icon |
| `className` | `string` | — | Additional CSS classes |

## PageToolbar

Action toolbar below the page header, typically containing filters, search, and bulk actions.

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `children` | `ReactNode` | — | Toolbar content |
| `variant` | `'default' \| 'sticky'` | `'default'` | Stickiness behavior |
| `className` | `string` | — | Additional CSS classes |

## PageFooter

Sticky footer at the bottom of the content area, typically for pagination, save/cancel, or status.

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `children` | `ReactNode` | — | Footer content |
| `sticky` | `boolean` | `true` | Sticks to bottom of viewport |
| `className` | `string` | — | Additional CSS classes |

## ScrollableContent

A scrollable container for content that may exceed the viewport height.

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `children` | `ReactNode` | — | Scrollable content |
| `maxHeight` | `string` | — | Constrains height (e.g., `'calc(100vh - 200px)'`) |
| `showScrollbar` | `boolean` | `false` | Always show scrollbar |
| `className` | `string` | — | Additional CSS classes |

## Composition Examples

### Basic Page

```tsx
<AppShell header={<Header />} sidebar={<Sidebar />}>
  <ContentContainer>
    <PageContainer>
      <PageHeader title="Dashboard" breadcrumbs={breadcrumbs} />
      <SectionContainer title="Overview">
        <ScrollableContent>
          {/* page content */}
        </ScrollableContent>
      </SectionContainer>
      <PageFooter>
        <Pagination />
      </PageFooter>
    </PageContainer>
  </ContentContainer>
</AppShell>
```

### Full-Page Form

```tsx
<AppShell header={<Header />} sidebar={<Sidebar />}>
  <ContentContainer>
    <PageContainer>
      <PageHeader title="Create Order" />
      <PageToolbar>
        <SearchInput />
        <Button>Save Draft</Button>
      </PageToolbar>
      <SectionContainer variant="card">
        <OrderForm />
      </SectionContainer>
      <PageFooter>
        <Button variant="primary">Submit</Button>
        <Button variant="ghost">Cancel</Button>
      </PageFooter>
    </PageContainer>
  </ContentContainer>
</AppShell>
```

## Design Tokens Used

| Token | Usage |
|-------|-------|
| `--layout-header-height` | Header row height |
| `--layout-sidebar-width` | Sidebar expanded width |
| `--layout-sidebar-collapsed-width` | Sidebar collapsed width |
| `--spacing-page` | Page padding |
| `--spacing-section` | Section gap |
| `--color-surface` | Content background |
| `--color-border` | Container borders |
| `--elevation-level-1` | Header shadow |
| `--radius-container` | Container border radius |
