# Phase 8 Architecture — Enterprise Admin Platform

## Overview

The Enterprise Admin Platform is a new application shell within the existing SporeKart web application. It provides a reusable administrative framework that all future enterprise admin features will extend.

## Architecture Principles

1. **Shell-first**: Every admin page inherits from `AdminLayout` which provides sidebar, header, breadcrumbs, and footer.
2. **Config-driven navigation**: Sidebar and top nav items are defined in a typed configuration file, not hardcoded in components.
3. **Design system reuse**: All layout components come from the central design system (`AuthenticatedLayout`, `Sidebar`, `TopNav`, `Breadcrumb`, `PageHeader`, `PageFooter`).
4. **Lazy loading**: All admin page components are lazy-loaded via `React.lazy()` and `Suspense`.
5. **Isolation**: Admin routes are separated from the customer/public route tree via `isNonEnterpriseRoute()` check in `App.tsx`.

## Route Architecture

```
App.tsx
├── Non-Enterprise Routes (public, auth, customer, admin)
│   ├── / (public website)
│   ├── /login, /register, etc. (auth)
│   ├── /dashboard/* (customer workspace)
│   ├── /admin/*           ← NEW: Admin Platform
│   │   ├── /admin/dashboard
│   │   ├── /admin/workspace
│   │   ├── /admin/profile
│   │   ├── /admin/settings
│   │   ├── /admin/system
│   │   └── /admin/help
│   └── /preview/* (design previews)
│       └── /preview/admin/*  ← NEW: Admin previews
└── Enterprise Routes (design system demo, navigation prototype)
    └── (not affected)
```

## Layout Hierarchy

```
AdminLayout
├── AuthenticatedLayout (from design system)
│   ├── Header (admin-header)
│   │   ├── Brand logo + "Admin" label
│   │   ├── Mobile hamburger trigger
│   │   └── TopNav (Profile, Settings, Help)
│   └── Sidebar (from design system)
│       ├── Header (SporeKart brand)
│       ├── SidebarNav (config-driven items)
│       └── Footer (version info)
├── Content Area
│   ├── PageHeader (title + description)
│   ├── Breadcrumb (from design system)
│   ├── <Outlet /> (page content via React Router)
│   └── PageFooter (copyright + links)
```

## Component Relationships

```
adminNavigation.tsx
  ├── ADMIN_SIDEBAR_ITEMS → SidebarItemData[]
  ├── ADMIN_TOP_NAV      → TopNavItem[]
  ├── buildAdminBreadcrumbs() → Crumb[]
  └── getAdminActiveId() → string

AdminLayout.tsx
  ├── Uses ADMIN_SIDEBAR_ITEMS → <Sidebar items={...} />
  ├── Uses ADMIN_TOP_NAV      → <TopNav items={...} />
  ├── Uses buildAdminBreadcrumbs() → <Breadcrumb crumbs={...} />
  └── Uses getAdminActiveId() → activeId

Dashboard Framework (Sprint 23 Part 4)
├── DashboardLayout (composes all sections)
├── kpi/ (KPICard, KPIGrid)
├── widgets/ (WidgetCard, WidgetGrid, WidgetContent — 10 types)
├── activity/ (ActivityFeed)
├── actions/ (QuickActions)
├── announcements/ (AnnouncementBanner)
├── status/ (SystemStatus)
└── mock/ (structured mock data)

Component Library (Sprint 23 Part 2)
├── forms/
│   ├── Textarea, NumberInput, CurrencyInput
│   ├── EmailInput, PhoneInput, TimePicker
│   └── TagSelector
├── data/
│   ├── PanelContainer
│   └── NoResults
├── navigation/
│   ├── Tabs, Accordion, Pagination
├── status/
│   ├── StatusBadge, PriorityBadge, OrderBadge
└── {buttons,feedback,loading}/
    └── design system re-exports
```

## File Locations

| Layer | Path |
|---|---|
| Layout | `src/admin/AdminLayout.tsx` |
| Config | `src/admin/config/adminNavigation.tsx` |
| Pages | `src/admin/pages/*.tsx` |
| Previews | `src/admin/preview/AdminPreviews.tsx` |
| Admin Component Library | `src/admin/components/*/{forms,data,navigation,status,preview}/` |
| Design System Re-exports | `src/admin/components/{buttons,feedback,loading}/` |
| Styles | `src/admin/admin.css` |
| Docs | `docs/phase-8/*.md` |
| Components | `src/admin/components/` |
| Dashboard | `src/admin/dashboard/` |
