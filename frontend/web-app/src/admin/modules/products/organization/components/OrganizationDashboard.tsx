import React from 'react';
import Icon from '../../../../../design-system/icons/Icon';
import { useCategoryAnalytics, useBrandAnalytics, useCollectionAnalytics } from '../state/useAnalytics';
import { MOCK_ACTIVITY_EVENTS } from '../mock/mockActivity';
import { ActivityTimeline } from './ActivityTimeline';

export const OrganizationDashboard = React.memo(function OrganizationDashboard() {
  const catAnalytics = useCategoryAnalytics();
  const brandAnalytics = useBrandAnalytics();
  const colAnalytics = useCollectionAnalytics();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: 'var(--space-component-gap)' }}>
        <StatCard icon="tag" label="Categories" value={catAnalytics.activeCategories} total={catAnalytics.totalCategories} color="var(--color-accent-blue)" />
        <StatCard icon="bookmark" label="Collections" value={colAnalytics.activeCollections} total={colAnalytics.totalCollections} color="var(--color-accent-green)" />
        <StatCard icon="shield" label="Brands" value={brandAnalytics.activeBrands} total={brandAnalytics.totalBrands} color="var(--color-accent-purple)" />
        <StatCard icon="pricetag" label="Tags in Use" value={catAnalytics.categoriesWithProducts} total={catAnalytics.totalCategories} color="var(--color-accent-orange)" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-section-gap)' }}>
        <Card title="Category Health">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            <HealthBar label="Active" value={catAnalytics.activeCategories} max={catAnalytics.totalCategories} color="var(--color-success)" />
            <HealthBar label="Inactive" value={catAnalytics.inactiveCategories} max={catAnalytics.totalCategories} color="var(--color-warning)" />
            <HealthBar label="Archived" value={catAnalytics.archivedCategories} max={catAnalytics.totalCategories} color="var(--color-text-tertiary)" />
            <div style={{ marginTop: 8, display: 'flex', alignItems: 'center', gap: 8 }}>
              <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>Health Score</span>
              <span style={{ fontSize: 'var(--text-h3)', fontWeight: 'var(--weight-bold)', color: catAnalytics.healthScore > 80 ? 'var(--color-success)' : catAnalytics.healthScore > 50 ? 'var(--color-warning)' : 'var(--color-text-danger)' }}>
                {catAnalytics.healthScore}%
              </span>
            </div>
          </div>
        </Card>

        <Card title="Collection Distribution">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
            {colAnalytics.typeDistribution.map((t) => (
              <HealthBar key={t.type} label={t.type} value={t.count} max={colAnalytics.totalCollections} color="var(--color-accent-cyan)" />
            ))}
          </div>
        </Card>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-section-gap)' }}>
        <Card title="Largest Categories">
          {catAnalytics.largestCategories.length > 0 ? (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
              {catAnalytics.largestCategories.map((c, i) => (
                <div key={i} style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', padding: '4px 0', borderBottom: '1px solid var(--color-border-weak)' }}>
                  <span>{c.name}</span>
                  <span style={{ fontWeight: 'var(--weight-semibold)' }}>{c.count} products</span>
                </div>
              ))}
            </div>
          ) : <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)' }}>No data available.</p>}
        </Card>

        <Card title="Top Brands">
          {brandAnalytics.topBrands.length > 0 ? (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
              {brandAnalytics.topBrands.map((b, i) => (
                <div key={i} style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', padding: '4px 0', borderBottom: '1px solid var(--color-border-weak)' }}>
                  <span>{b.name}</span>
                  <span style={{ fontWeight: 'var(--weight-semibold)' }}>{b.count} products</span>
                </div>
              ))}
            </div>
          ) : <p style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)' }}>No data available.</p>}
        </Card>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-section-gap)' }}>
        <Card title="Recent Activity">
          <ActivityTimeline events={MOCK_ACTIVITY_EVENTS} maxItems={5} />
        </Card>
        <Card title="Quick Stats">
          <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
            <QuickStat label="Category Tree Depth" value={`${catAnalytics.maxDepth} levels`} />
            <QuickStat label="Featured Categories" value={catAnalytics.featuredCategories} />
            <QuickStat label="Featured Brands" value={brandAnalytics.featuredBrands} />
            <QuickStat label="Unused Categories" value={catAnalytics.unusedCategories} />
            <QuickStat label="Featured Collections" value={colAnalytics.featuredCollections} />
            <QuickStat label="Seasonal Collections" value={colAnalytics.seasonalCollections} />
          </div>
        </Card>
      </div>

      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', padding: '8px 0', borderTop: '1px solid var(--color-border)' }}>
        Organization Dashboard — Mock Mode. All analytics derived from static data. Future: live metrics from product service.
      </div>
    </div>
  );
});

function StatCard({ icon, label, value, total, color }: { icon: string; label: string; value: number; total: number; color: string }) {
  const pct = total > 0 ? Math.round((value / total) * 100) : 0;
  return (
    <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 8 }}>
        <Icon name={icon} size={18} style={{ color }} />
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', textTransform: 'uppercase', letterSpacing: 'var(--tracking-wide)' }}>{label}</span>
      </div>
      <div style={{ display: 'flex', alignItems: 'baseline', gap: 8 }}>
        <span style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', color }}>{value}</span>
        <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)' }}>/ {total} ({pct}%)</span>
      </div>
    </div>
  );
}

function Card({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
      <h4 style={{ margin: '0 0 12px', fontSize: 'var(--text-h5)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>{title}</h4>
      {children}
    </div>
  );
}

function HealthBar({ label, value, max, color }: { label: string; value: number; max: number; color: string }) {
  const pct = max > 0 ? (value / max) * 100 : 0;
  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', marginBottom: 2 }}>
        <span>{label}</span>
        <span>{value}</span>
      </div>
      <div style={{ height: 6, background: 'var(--color-bg-surface-raised)', borderRadius: 'var(--radius-full)', overflow: 'hidden' }}>
        <div style={{ height: '100%', width: `${pct}%`, background: color, borderRadius: 'var(--radius-full)', transition: 'width var(--duration-normal) var(--easing-standard)' }} />
      </div>
    </div>
  );
}

function QuickStat({ label, value }: { label: string; value: string | number }) {
  return (
    <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', padding: '4px 0', borderBottom: '1px solid var(--color-border-weak)' }}>
      <span style={{ color: 'var(--color-text-secondary)' }}>{label}</span>
      <span style={{ fontWeight: 'var(--weight-semibold)' }}>{value}</span>
    </div>
  );
}

export default OrganizationDashboard;
