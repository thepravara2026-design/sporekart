import React from 'react';
import Icon from '../../../../../design-system/icons/Icon';
import { useBrandAnalytics } from '../state/useAnalytics';

export const BrandAnalyticsDashboard = React.memo(function BrandAnalyticsDashboard() {
  const analytics = useBrandAnalytics();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: 'var(--space-component-gap)' }}>
        <KPICard icon="shield" label="Total Brands" value={analytics.totalBrands} color="var(--color-accent-purple)" />
        <KPICard icon="check-circle" label="Active" value={analytics.activeBrands} color="var(--color-success)" />
        <KPICard icon="x-circle" label="Inactive" value={analytics.inactiveBrands} color="var(--color-warning)" />
        <KPICard icon="star" label="Featured" value={analytics.featuredBrands} color="var(--color-accent-orange)" />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: 'var(--space-component-gap)' }}>
        <KPICard icon="archive" label="Archived" value={analytics.archivedBrands} color="var(--color-text-tertiary)" />
        <KPICard icon="heart" label="Brand Health" value={`${analytics.brandHealth}%`} color={analytics.brandHealth > 80 ? 'var(--color-success)' : analytics.brandHealth > 50 ? 'var(--color-warning)' : 'var(--color-text-danger)'} />
        <div /><div />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-section-gap)' }}>
        <Section title="Top Brands by Products">
          {analytics.topBrands.map((b, i) => (
            <BarRow key={i} label={b.name} value={b.count} max={Math.max(...analytics.topBrands.map((x) => x.count), 1)} />
          ))}
          {analytics.topBrands.length === 0 && <EmptyText>No brands with products.</EmptyText>}
        </Section>
        <Section title="Recently Added Brands">
          {analytics.recentlyAdded.length > 0 ? (
            <ul style={{ margin: 0, padding: 0, listStyle: 'none', display: 'flex', flexDirection: 'column', gap: 6 }}>
              {analytics.recentlyAdded.map((name) => (
                <li key={name} style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', padding: '4px 0', borderBottom: '1px solid var(--color-border-weak)' }}>{name}</li>
              ))}
            </ul>
          ) : <EmptyText>No recent brands.</EmptyText>}
        </Section>
      </div>

      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', padding: '8px 0', borderTop: '1px solid var(--color-border)' }}>
        Mock Analytics — all data from static mock datasets. Future: integrate with brand analytics service.
      </div>
    </div>
  );
});

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

export default BrandAnalyticsDashboard;
