import React from 'react';
import { Card } from '../../../../design-system/components/composite/Card';
import { StatusBadge } from '../../../components/status';
import { KPIGrid } from '../../../dashboard/kpi/KPIGrid';
import type { KPIData } from '../../../dashboard/types';
import { ProductActivityTimeline } from '../components/ProductActivityTimeline';
import { ProductQuickActions } from '../components/ProductQuickActions';
import { ProductStatCard } from '../components/ProductStatCard';
import { lifecycleLabel, lifecycleToBadge, PRODUCT_LIFECYCLE_STATES } from '../lifecycle';
import { MOCK_BRANDS, MOCK_CATEGORIES, MOCK_COLLECTIONS, MOCK_PRODUCTS } from '../mock/mockProducts';
import type { ProductLifecycleState } from '../types';

function buildKpis(): KPIData[] {
  const total = MOCK_PRODUCTS.length;
  const countState = (state: ProductLifecycleState) => MOCK_PRODUCTS.filter((p) => p.lifecycleState === state).length;

  const latestCreated = MOCK_PRODUCTS.reduce((max, p) => (p.createdAt > max ? p.createdAt : max), MOCK_PRODUCTS[0].createdAt);
  const threshold = new Date(new Date(latestCreated).getTime() - 7 * 24 * 60 * 60 * 1000).toISOString();
  const addedThisWeek = MOCK_PRODUCTS.filter((p) => p.createdAt >= threshold).length;

  return [
    { id: 'total', title: 'Total Products', value: String(total), trend: 'up', percentage: 8, comparison: 'vs last month', icon: 'package', color: 'var(--color-primary)' },
    { id: 'published', title: 'Published', value: String(countState('published')), trend: 'up', percentage: 5, comparison: 'vs last month', icon: 'check-circle', color: 'var(--color-success)' },
    { id: 'draft', title: 'Draft', value: String(countState('draft')), trend: 'neutral', percentage: 0, comparison: 'no change', icon: 'file-text', color: 'var(--color-text-secondary)' },
    { id: 'archived', title: 'Archived', value: String(countState('archived')), trend: 'down', percentage: 2, comparison: 'vs last month', icon: 'archive', color: 'var(--color-warning)' },
    { id: 'categories', title: 'Categories', value: String(MOCK_CATEGORIES.length), trend: 'neutral', percentage: 0, comparison: 'no change', icon: 'tag', color: 'var(--color-info)' },
    { id: 'brands', title: 'Brands', value: String(MOCK_BRANDS.length), trend: 'up', percentage: 3, comparison: 'vs last month', icon: 'shield', color: 'var(--color-primary)' },
    { id: 'collections', title: 'Collections', value: String(MOCK_COLLECTIONS.length), trend: 'up', percentage: 4, comparison: 'vs last month', icon: 'bookmark', color: 'var(--color-primary)' },
    { id: 'added', title: 'Added This Week', value: String(addedThisWeek), trend: 'up', percentage: 6, comparison: 'vs last week', icon: 'trending-up', color: 'var(--color-success)' },
  ];
}

function CardSection({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <Card variant="default" padding="md" as="section" aria-label={title}>
      <h2 style={{ fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)', margin: '0 0 var(--space-stack-xs)', fontWeight: 'var(--weight-semibold)' }}>{title}</h2>
      {children}
    </Card>
  );
}

export const ProductDashboard = React.memo(function ProductDashboard() {
  const kpis = React.useMemo(buildKpis, []);
  const recent = React.useMemo(
    () => [...MOCK_PRODUCTS].sort((a, b) => (a.updatedAt < b.updatedAt ? 1 : -1)).slice(0, 5),
    [],
  );
  const latestCreated = MOCK_PRODUCTS.reduce((max, p) => (p.createdAt > max ? p.createdAt : max), MOCK_PRODUCTS[0].createdAt);
  const threshold = new Date(new Date(latestCreated).getTime() - 7 * 24 * 60 * 60 * 1000).toISOString();
  const added = React.useMemo(() => MOCK_PRODUCTS.filter((p) => p.createdAt >= threshold).slice(0, 5), [threshold]);

  const lifecycleCounts = React.useMemo(() => {
    return PRODUCT_LIFECYCLE_STATES.map((meta) => ({
      meta,
      count: MOCK_PRODUCTS.filter((p) => p.lifecycleState === meta.state).length,
    }));
  }, []);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <ProductQuickActions />

      <CardSection title="Key Metrics">
        <KPIGrid kpis={kpis} columns={4} />
      </CardSection>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: 'var(--space-section-gap)' }}>
        <CardSection title="Recently Updated">
          <ul style={{ listStyle: 'none', margin: 0, padding: 0, display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-xs)' }}>
            {recent.map((p) => (
              <li key={p.id} style={{ display: 'flex', justifyContent: 'space-between', gap: 'var(--space-inline-xs)' }}>
                <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>{p.name}</span>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{new Date(p.updatedAt).toLocaleDateString()}</span>
              </li>
            ))}
          </ul>
        </CardSection>

        <CardSection title="Products Added This Week">
          <ul style={{ listStyle: 'none', margin: 0, padding: 0, display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-xs)' }}>
            {added.length === 0 && (
              <li style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)' }}>No products added this week.</li>
            )}
            {added.map((p) => (
              <li key={p.id} style={{ display: 'flex', justifyContent: 'space-between', gap: 'var(--space-inline-xs)' }}>
                <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)' }}>{p.name}</span>
                <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>{new Date(p.createdAt).toLocaleDateString()}</span>
              </li>
            ))}
          </ul>
        </CardSection>

        <CardSection title="Activity">
          <ProductActivityTimeline />
        </CardSection>
      </div>

      <CardSection title="Publishing Status">
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: 'var(--space-component-gap)' }}>
          {lifecycleCounts.map(({ meta, count }) => (
            <div key={meta.state} style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-inline-xs)' }}>
              <StatusBadge status={lifecycleLabel(meta.state)} variant={lifecycleToBadge(meta.state)} size="md" />
              <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{count}</span>
            </div>
          ))}
        </div>
      </CardSection>

      <CardSection title="Metric Cards (Variety)">
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: 'var(--space-component-gap)' }}>
          {kpis.slice(0, 4).map((kpi) => (
            <ProductStatCard key={kpi.id} kpi={kpi} />
          ))}
        </div>
      </CardSection>
    </div>
  );
});

export default ProductDashboard;
