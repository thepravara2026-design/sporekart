export function DashboardSkeleton() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {Array.from({ length: 6 }).map((_, i) => <div key={i} style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', height: 90 }} />)}
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 'var(--space-component-gap)' }}>
        <div style={{ height: 180, background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)' }} />
        <div style={{ height: 180, background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)' }} />
      </div>
    </div>
  );
}

export function ListSkeleton({ rows = 5 }: { rows?: number }) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
      {Array.from({ length: rows }).map((_, i) => (
        <div key={i} style={{ display: 'flex', gap: 12, padding: '12px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-sm)' }}>
          <div style={{ width: 32, height: 32, borderRadius: '50%', background: 'var(--color-bg-skeleton-base)', flexShrink: 0 }} />
          <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 6 }}>
            <div style={{ height: 14, background: 'var(--color-bg-skeleton-base)', borderRadius: 4, width: '60%' }} />
            <div style={{ height: 12, background: 'var(--color-bg-skeleton-base)', borderRadius: 4, width: '40%' }} />
          </div>
        </div>
      ))}
    </div>
  );
}
