# Layout Templates

## Overview

Nine layout templates provide pre-configured page structures for different contexts. Each template composes the Application Shell components with appropriate settings for header, sidebar, content, and footer.

## Templates

---

### PublicLayout

Used for unauthenticated pages: landing, login, signup, pricing, etc.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `children` | `ReactNode` | — | Page content |
| `header` | `'default' \| 'transparent' \| 'none'` | `'default'` | Header variant |
| `footer` | `'default' \| 'none'` | `'default'` | Footer variant |
| `maxWidth` | `ContainerSize` | `'lg'` | Content width |

**Use case:** Marketing pages, authentication flows.

**Example:**
```tsx
<PublicLayout header="transparent">
  <HeroSection />
  <FeatureGrid />
</PublicLayout>
```

---

### AuthenticatedLayout

Standard layout for logged-in users with sidebar and header.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `children` | `ReactNode` | — | Page content |
| `sidebarItems` | `SidebarItemData[]` | — | Navigation items |
| `headerVariant` | `'primary' \| 'secondary'` | `'primary'` | Header variant |

**Use case:** General authenticated pages.

**Example:**
```tsx
<AuthenticatedLayout sidebarItems={navItems}>
  <PageHeader title="Dashboard" />
  <DashboardGrid />
</AuthenticatedLayout>
```

---

### DashboardLayout

Optimized for data-heavy dashboard pages with metrics and charts.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `children` | `ReactNode` | — | Page content |
| `sidebarItems` | `SidebarItemData[]` | — | Navigation items |
| `toolbar` | `ReactNode` | — | Filter/action toolbar |

**Use case:** Analytics, reports, KPI pages.

---

### ContentLayout

Focuses on reading/content consumption with minimal chrome.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `children` | `ReactNode` | — | Page content |
| `showBreadcrumbs` | `boolean` | `true` | Breadcrumb visibility |
| `maxWidth` | `ContainerSize` | `'md'` | Reading width |

**Use case:** Article, documentation, profile pages.

---

### SplitLayout

Divides the page into two resizable panels.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `left` | `ReactNode` | — | Left panel content |
| `right` | `ReactNode` | — | Right panel content |
| `defaultRatio` | `number` | `0.5` | Left/right split ratio |
| `minWidth` | `string` | `'300px'` | Minimum panel width |

**Use case:** Editor/preview, list/detail, compare views.

---

### CenteredLayout

Centers a single content block horizontally and vertically.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `children` | `ReactNode` | — | Centered content |
| `maxWidth` | `ContainerSize` | `'sm'` | Content max width |

**Use case:** Login forms, error pages, empty states.

---

### FullWidthLayout

Full-width layout with no container constraints.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `children` | `ReactNode` | — | Page content |

**Use case:** Design mockups, map views, media galleries.

---

### BlankLayout

Minimal layout with no chrome — only the page content.

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `children` | `ReactNode` | — | Page content |

**Use case:** Embedded views, modals, print layouts.

---

### ErrorLayout

Specialized layout for error pages (401, 403, 404, 500).

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `statusCode` | `number` | — | HTTP status code |
| `title` | `string` | — | Error title |
| `message` | `string` | — | Error description |
| `action` | `ReactNode` | — | Call-to-action button |

**Use case:** Error pages with centered content and minimal chrome.

**Example:**
```tsx
<ErrorLayout
  statusCode={404}
  title="Page Not Found"
  message="The page you are looking for doesn't exist."
  action={<Button onClick={() => navigate('/')}>Go Home</Button>}
/>
```

## Composition Guide

Each template is a convenience wrapper around AppShell components:

```tsx
// PublicLayout internally composes:
<AppShell
  header={<Header variant="transparent" />}
  sidebar={null}
>
  <ContentContainer maxWidth="lg">
    {children}
  </ContentContainer>
  <PageFooter />
</AppShell>

// AuthenticatedLayout internally composes:
<AppShell
  header={<Header variant="primary" />}
  sidebar={<Sidebar items={sidebarItems} />}
>
  <ContentContainer>
    <Breadcrumb crumbs={breadcrumbs} />
    {children}
  </ContentContainer>
</AppShell>
```

## Responsive Behavior

| Template | xs/sm | md | lg+ |
|----------|-------|-----|-----|
| PublicLayout | Full width | Contained | Contained |
| AuthenticatedLayout | Sidebar overlay | Sidebar mini | Full sidebar |
| DashboardLayout | Single column | 2 columns | Multi-column grid |
| ContentLayout | Full width | Contained | Reading width |
| SplitLayout | Stacked vertical | Side-by-side | Side-by-side |
| CenteredLayout | Full width | Contained | Contained |
| FullWidthLayout | Full width | Full width | Full width |
| BlankLayout | Full width | Full width | Full width |
| ErrorLayout | Centered | Centered | Centered |
