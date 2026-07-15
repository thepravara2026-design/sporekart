import React from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import { Card } from '../../../../design-system/components/composite/Card';
import { StatusBadge } from '../../../components/status';
import type { Crumb } from '../../../../design-system/components/navigation/Breadcrumb';
import { ProductLayout } from '../layout/ProductLayout';
import { ProductWorkspace } from '../workspace/ProductWorkspace';
import { ProductDashboard } from '../dashboard/ProductDashboard';
import { LifecycleBadge } from '../components/LifecycleBadge';
import { PRODUCT_LIFECYCLE_STATES } from '../lifecycle';
import { ProductCatalog } from '../catalog/ProductCatalog';
import ProductCreationWizard from '../creation/ProductCreationWizard';
import { ProductEditingWorkspace } from '../editing/ProductEditingWorkspace';
import { ProductDetailWorkspace } from '../editing/ProductDetailWorkspace';
import { OrganizationPreviewApp } from '../organization/preview/OrganizationPreviewApp';
import { PricingPreviewApp } from '../pricing/preview/PricingPreviewApp';
import { VariantPreviewApp } from '../variants/preview/VariantPreviewApp';
import { SeoPreviewApp } from '../seo/preview/SeoPreviewApp';
import { ValidationPreviewApp } from '../validation/preview/ValidationPreviewApp';
import { AnalyticsPreviewApp } from '../analytics/preview/AnalyticsPreviewApp';
import {
  EditPreview,
  HistoryPreview,
  TimelinePreview,
  LifecyclePreviewPage,
  ComparePreview,
} from '../editing/ProductEditingPreviews';

export interface ProductRouteManifestEntry {
  path: string;
  label: string;
  section: string;
  status: 'planned' | 'active';
}

export const PRODUCT_ROUTE_MANIFEST: ProductRouteManifestEntry[] = [
  { path: '/products', label: 'Products', section: 'overview', status: 'planned' },
  { path: '/products/dashboard', label: 'Dashboard', section: 'overview', status: 'planned' },
  { path: '/products/list', label: 'Product List', section: 'products', status: 'planned' },
  { path: '/products/grid', label: 'Product Grid', section: 'products', status: 'planned' },
  { path: '/products/create', label: 'Create Product', section: 'products', status: 'planned' },
  { path: '/products/edit', label: 'Edit Product', section: 'products', status: 'active' },
  { path: '/products/details', label: 'Product Details', section: 'products', status: 'active' },
  { path: '/products/history', label: 'Version History', section: 'history', status: 'active' },
  { path: '/products/lifecycle', label: 'Lifecycle', section: 'publishing', status: 'active' },
  { path: '/products/compare', label: 'Compare Versions', section: 'history', status: 'active' },
  { path: '/products/timeline', label: 'Activity Timeline', section: 'activity', status: 'active' },
  { path: '/products/categories', label: 'Categories', section: 'categories', status: 'planned' },
  { path: '/products/collections', label: 'Collections', section: 'collections', status: 'planned' },
  { path: '/products/brands', label: 'Brands', section: 'brands', status: 'planned' },
  { path: '/products/media', label: 'Media', section: 'media', status: 'planned' },
  { path: '/products/pricing', label: 'Pricing', section: 'pricing', status: 'planned' },
  { path: '/products/publishing', label: 'Publishing', section: 'publishing', status: 'planned' },
  { path: '/products/activity', label: 'Activity', section: 'activity', status: 'planned' },
  { path: '/products/settings', label: 'Settings', section: 'settings', status: 'planned' },
];

const PreviewNote = React.memo(function PreviewNote({ title }: { title: string }) {
  return (
    <Card variant="outlined" padding="sm" as="section" aria-label={`${title} notes`} style={{ marginTop: 'var(--space-component-gap)' }}>
      <h3 style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', margin: '0 0 4px', fontWeight: 'var(--weight-semibold)' }}>{title}</h3>
      <ul style={{ listStyle: 'none', margin: 0, padding: 0, display: 'flex', flexDirection: 'column', gap: 2 }}>
        <li style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Responsive: Desktop (≥1280px), Tablet (768–1279px), Mobile (&lt;768px) breakpoints supported.</li>
        <li style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Theme: Honors Dark/Light via design tokens; no hardcoded colors.</li>
        <li style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Accessibility: Semantic landmarks, aria-labels, keyboard focus states.</li>
        <li style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Performance: Memoized components, no network calls (Mock Mode).</li>
      </ul>
    </Card>
  );
});

const LayoutPreview = React.memo(function LayoutPreview() {
  const crumbs: Crumb[] = [
    { label: 'Admin', href: '/' },
    { label: 'Products', href: '/products' },
    { label: 'Layout' },
  ];
  return (
    <ProductLayout
      title="Product Layout"
      breadcrumbs={crumbs}
      actions={<StatusBadge status="Demo" variant="info" />}
    >
      <Card variant="default" padding="md">
        <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-primary)', margin: 0 }}>
          This page demonstrates the module-local <strong>ProductLayout</strong> shell: a header with title and actions,
          an enterprise breadcrumb, a content region, and a status footer. It mirrors the admin shell structure
          without modifying <code>AdminLayout</code>.
        </p>
      </Card>
      <PreviewNote title="Layout Preview Notes" />
    </ProductLayout>
  );
});

const RoutesPreview = React.memo(function RoutesPreview() {
  const crumbs: Crumb[] = [
    { label: 'Admin', href: '/' },
    { label: 'Products', href: '/products' },
    { label: 'Routing Plan' },
  ];
  return (
    <ProductLayout title="Product Routing Plan" breadcrumbs={crumbs}>
      <Card variant="default" padding="md">
        <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', margin: '0 0 var(--space-stack-xs)' }}>
          Intended enterprise routes for the Product Management module. Status is <em>Planned</em> for Sprint 24 Part 2+.
        </p>
        <ul style={{ listStyle: 'none', margin: 0, padding: 0, display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-xs)' }}>
          {PRODUCT_ROUTE_MANIFEST.map((entry) => (
            <li
              key={entry.path}
              style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: 'var(--space-inline-xs)', padding: '8px 12px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)' }}
            >
              <code style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>{entry.path}</code>
              <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)' }}>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{entry.section}</span>
                <StatusBadge status="Planned" variant="neutral" />
              </div>
            </li>
          ))}
        </ul>
      </Card>
      <PreviewNote title="Routing Notes" />
    </ProductLayout>
  );
});

const WorkspacePreview = React.memo(function WorkspacePreview() {
  return (
    <ProductWorkspace activeSection="overview">
      <Card variant="default" padding="md">
        <p style={{ fontSize: 'var(--text-body)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-xs)' }}>
          Product Workspace shell with the module-local navigation. The left rail lists product sections; the right pane
          renders a sample overview.
        </p>
        <ProductDashboard />
      </Card>
      <PreviewNote title="Workspace Notes" />
    </ProductWorkspace>
  );
});

const LifecyclePreview = React.memo(function LifecyclePreview() {
  const crumbs: Crumb[] = [
    { label: 'Admin', href: '/' },
    { label: 'Products', href: '/products' },
    { label: 'Lifecycle' },
  ];
  return (
    <ProductLayout title="Product Lifecycle" breadcrumbs={crumbs}>
      <Card variant="default" padding="md">
        <ul style={{ listStyle: 'none', margin: 0, padding: 0, display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-xs)' }}>
          {PRODUCT_LIFECYCLE_STATES.map((meta) => (
            <li
              key={meta.state}
              style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)', padding: '10px 12px', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-sm)' }}
            >
              <LifecycleBadge state={meta.state} size="md" />
              <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', minWidth: 140 }}>{meta.state}</span>
              <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{meta.description}</span>
            </li>
          ))}
        </ul>
      </Card>
      <PreviewNote title="Lifecycle Notes" />
    </ProductLayout>
  );
});

export const ProductPreviewApp: React.FC = () => {
  return (
    <Routes>
      <Route index element={<Navigate to="catalog" replace />} />
      <Route path="layout" element={<LayoutPreview />} />
      <Route
        path="dashboard"
        element={
          <ProductWorkspace activeSection="dashboard">
            <ProductDashboard />
          </ProductWorkspace>
        }
      />
      <Route path="routes" element={<RoutesPreview />} />
      <Route path="workspace" element={<WorkspacePreview />} />
      <Route path="lifecycle" element={<LifecyclePreview />} />
      <Route path="catalog" element={<ProductCatalog />} />
      <Route path="table" element={<ProductCatalog initialView="table" />} />
      <Route path="cards" element={<ProductCatalog initialView="card" />} />
      <Route path="search" element={<ProductCatalog focusSearch />} />
      <Route path="filters" element={<ProductCatalog focusFilters />} />
      <Route path="pagination" element={<ProductCatalog pageSize={5} />} />
      <Route path="create" element={<ProductCreationWizard />} />
      <Route path="wizard" element={<ProductCreationWizard initialStep="basic" />} />
      <Route path="forms" element={<ProductCreationWizard initialStep="basic" />} />
      <Route path="review" element={<ProductCreationWizard initialStep="review" />} />
      <Route path="confirmation" element={<ProductCreationWizard demoConfirmation />} />
      <Route path="/products/create" element={<ProductCreationWizard />} />
      <Route path="edit" element={<ProductEditingWorkspace />} />
      <Route path="details" element={<ProductDetailWorkspace />} />
      <Route path="history" element={<HistoryPreview />} />
      <Route path="lifecycle" element={<LifecyclePreviewPage />} />
      <Route path="compare" element={<ComparePreview />} />
      <Route path="timeline" element={<TimelinePreview />} />
      <Route path="overview" element={<EditPreview />} />
      <Route path="pricing/*" element={<PricingPreviewApp />} />
      <Route path="variants/*" element={<VariantPreviewApp />} />
      <Route path="organization/*" element={<OrganizationPreviewApp />} />
      <Route path="seo/*" element={<SeoPreviewApp />} />
      <Route path="validation/*" element={<ValidationPreviewApp />} />
      <Route path="analytics/*" element={<AnalyticsPreviewApp />} />
    </Routes>
  );
};

export default ProductPreviewApp;
