export function DashboardSkeleton() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {Array.from({ length: 8 }).map((_, i) => <div key={i} style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', height: 100 }} />)}
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-component-gap)' }}>
        <div style={{ height: 200, background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)' }} />
        <div style={{ height: 200, background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)' }} />
      </div>
    </div>
  );
}

export function AnalyticsTableSkeleton({ rows = 5 }: { rows?: number }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
      {Array.from({ length: rows }).map((_, i) => (
        <div key={i} style={{ display: 'flex', gap: 12, padding: '12px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-sm)' }}>
          {Array.from({ length: 5 }).map((__, j) => (
            <div key={j} style={{ flex: 1, height: 14, background: 'var(--color-bg-skeleton-base)', borderRadius: 4 }} />
          ))}
        </div>
      ))}
    </div>
  );
}
