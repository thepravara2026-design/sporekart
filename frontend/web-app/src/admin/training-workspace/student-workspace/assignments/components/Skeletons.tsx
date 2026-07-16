export function AssignmentTableSkeleton() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
      {Array.from({ length: 5 }).map((_, i) => (
        <div key={i} style={{ display: 'flex', gap: 12, padding: '12px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-sm)' }}>
          <div style={{ width: '12%', height: 14, background: 'var(--color-bg-skeleton-base)', borderRadius: 4 }} />
          <div style={{ width: '25%', height: 14, background: 'var(--color-bg-skeleton-base)', borderRadius: 4 }} />
          <div style={{ width: '18%', height: 14, background: 'var(--color-bg-skeleton-base)', borderRadius: 4 }} />
          <div style={{ width: '15%', height: 14, background: 'var(--color-bg-skeleton-base)', borderRadius: 4 }} />
          <div style={{ width: '10%', height: 14, background: 'var(--color-bg-skeleton-base)', borderRadius: 4 }} />
          <div style={{ width: '12%', height: 14, background: 'var(--color-bg-skeleton-base)', borderRadius: 4 }} />
          <div style={{ flex: 1, height: 14, background: 'var(--color-bg-skeleton-base)', borderRadius: 4 }} />
        </div>
      ))}
    </div>
  );
}

export function AssignmentDashboardSkeleton() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {Array.from({ length: 8 }).map((_, i) => (
          <div key={i} style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', height: 80 }} />
        ))}
      </div>
      <div style={{ height: 200, background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)' }} />
      <div style={{ height: 180, background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)' }} />
    </div>
  );
}
