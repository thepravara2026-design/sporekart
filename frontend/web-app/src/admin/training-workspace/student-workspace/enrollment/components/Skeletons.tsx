export function EnrollmentTableSkeleton() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
      {Array.from({ length: 5 }).map((_, i) => (
        <div key={i} style={{ display: 'flex', gap: 12, padding: '12px', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-sm)' }}>
          <div style={{ width: '15%', height: 14, background: 'var(--color-bg-skeleton-base)', borderRadius: 4 }} />
          <div style={{ width: '25%', height: 14, background: 'var(--color-bg-skeleton-base)', borderRadius: 4 }} />
          <div style={{ width: '30%', height: 14, background: 'var(--color-bg-skeleton-base)', borderRadius: 4 }} />
          <div style={{ width: '15%', height: 14, background: 'var(--color-bg-skeleton-base)', borderRadius: 4 }} />
          <div style={{ flex: 1, height: 14, background: 'var(--color-bg-skeleton-base)', borderRadius: 4 }} />
        </div>
      ))}
    </div>
  );
}

export function EnrollmentDashboardSkeleton() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)' }}>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {Array.from({ length: 6 }).map((_, i) => (
          <div key={i} style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', height: 80 }} />
        ))}
      </div>
      <div style={{ height: 200, background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)' }} />
    </div>
  );
}

export function BatchCardSkeleton() {
  return (
    <div style={{ padding: 'var(--space-3)', background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', display: 'flex', flexDirection: 'column', gap: 8 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <div style={{ width: '50%', height: 14, background: 'var(--color-bg-skeleton-base)', borderRadius: 4 }} />
        <div style={{ width: 60, height: 20, background: 'var(--color-bg-skeleton-base)', borderRadius: 10 }} />
      </div>
      <div style={{ width: '70%', height: 14, background: 'var(--color-bg-skeleton-base)', borderRadius: 4 }} />
      <div style={{ width: '100%', height: 6, background: 'var(--color-bg-skeleton-base)', borderRadius: 3 }} />
    </div>
  );
}
