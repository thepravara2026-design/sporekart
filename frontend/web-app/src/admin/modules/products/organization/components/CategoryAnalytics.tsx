import React from 'react';
import Icon from '../../../../../design-system/icons/Icon';
import { useCategoryAnalytics } from '../state/useAnalytics';

export const CategoryAnalytics = React.memo(function CategoryAnalytics() {
  const analytics = useCategoryAnalytics();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <KPIRow>
        <KPICard icon="tag" label="Total Categories" value={analytics.totalCategories} color="var(--color-accent-blue)" />
        <KPICard icon="check-circle" label="Active" value={analytics.activeCategories} color="var(--color-success)" />
        <KPICard icon="x-circle" label="Inactive" value={analytics.inactiveCategories} color="var(--color-warning)" />
        <KPICard icon="archive" label="Archived" value={analytics.archivedCategories} color="var(--color-text-tertiary)" />
      </KPIRow>

      <KPIRow>
        <KPICard icon="star" label="Featured" value={analytics.featuredCategories} color="var(--color-accent-orange)" />
        <KPICard icon="layers" label="Root Categories" value={analytics.rootCategories} color="var(--color-accent-purple)" />
        <KPICard icon="bar-chart" label="Max Depth" value={analytics.maxDepth} color="var(--color-accent-cyan)" />
        <KPICard icon="heart" label="Health Score" value={`${analytics.healthScore}%`} color={analytics.healthScore > 80 ? 'var(--color-success)' : analytics.healthScore > 50 ? 'var(--color-warning)' : 'var(--color-text-danger)'} />
      </KPIRow>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-section-gap)' }}>
        <Section title="Largest Categories">
          {analytics.largestCategories.map((c, i) => (
            <BarRow key={i} label={c.name} value={c.count} max={Math.max(...analytics.largestCategories.map((x) => x.count), 1)} />
          ))}
          {analytics.largestCategories.length === 0 && <EmptyText>No categories with products.</EmptyText>}
        </Section>

        <Section title="Unused Categories">
          <div style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-danger)', margin: '8px 0' }}>{analytics.unusedCategories}</div>
          <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>categories have zero products assigned</span>
          {analytics.unusedCategories > 0 && (
            <div style={{ marginTop: 8, display: 'flex', flexWrap: 'wrap', gap: 4 }}>
              {analytics.productsPerCategory.filter((c) => c.count === 0).slice(0, 5).map((c) => (
                <span key={c.name} style={{ padding: '2px 8px', borderRadius: 'var(--radius-full)', background: 'var(--color-bg-warning-weak)', fontSize: 'var(--text-caption)', color: 'var(--color-warning)' }}>{c.name}</span>
              ))}
            </div>
          )}
        </Section>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-section-gap)' }}>
        <Section title="Recently Updated">
          {analytics.recentlyUpdated.length > 0 ? (
            <ul style={{ margin: 0, padding: 0, listStyle: 'none', display: 'flex', flexDirection: 'column', gap: 6 }}>
              {analytics.recentlyUpdated.map((name) => (
                <li key={name} style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', padding: '4px 0', borderBottom: '1px solid var(--color-border-weak)' }}>{name}</li>
              ))}
            </ul>
          ) : <EmptyText>No recent updates.</EmptyText>}
        </Section>
        <Section title="Recently Created">
          {analytics.recentlyCreated.length > 0 ? (
            <ul style={{ margin: 0, padding: 0, listStyle: 'none', display: 'flex', flexDirection: 'column', gap: 6 }}>
              {analytics.recentlyCreated.map((name) => (
                <li key={name} style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', padding: '4px 0', borderBottom: '1px solid var(--color-border-weak)' }}>{name}</li>
              ))}
            </ul>
          ) : <EmptyText>No new categories.</EmptyText>}
        </Section>
      </div>

      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', padding: '8px 0', borderTop: '1px solid var(--color-border)' }}>
        Mock Analytics — all data is from static mock datasets. Future: integrate with product analytics service.
      </div>
    </div>
  );
});

function KPIRow({ children }: { children: React.ReactNode }) {
  return <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: 'var(--space-component-gap)' }}>{children}</div>;
}

function KPICard({ icon, label, value, color }: { icon: string; label: string; value: string | number; color: string }) {
  return (
    <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 8 }}>
        <Icon name={icon} size={18} style={{ color }} />
        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)', textTransform: 'uppercase', letterSpacing: 'var(--tracking-wide)' }}>{label}</span>
      </div>
      <div style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-bold)', color }}>{value}</div>
    </div>
  );
}

function Section({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <div style={{ padding: 'var(--space-4)', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
      <h4 style={{ margin: '0 0 12px', fontSize: 'var(--text-h5)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)' }}>{title}</h4>
      {children}
    </div>
  );
}

function BarRow({ label, value, max }: { label: string; value: number; max: number }) {
  return (
    <div style={{ marginBottom: 8 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', marginBottom: 4 }}>
        <span>{label}</span>
        <span style={{ fontWeight: 'var(--weight-semibold)' }}>{value}</span>
      </div>
      <div style={{ height: 6, background: 'var(--color-bg-surface-raised)', borderRadius: 'var(--radius-full)', overflow: 'hidden' }}>
        <div style={{ height: '100%', width: `${(value / max) * 100}%`, background: 'var(--color-primary)', borderRadius: 'var(--radius-full)', transition: 'width var(--duration-normal) var(--easing-standard)' }} />
      </div>
    </div>
  );
}

function EmptyText({ children }: { children: string }) {
  return <p style={{ margin: 0, fontSize: 'var(--text-body-sm)', color: 'var(--color-text-tertiary)' }}>{children}</p>;
}

export default CategoryAnalytics;
