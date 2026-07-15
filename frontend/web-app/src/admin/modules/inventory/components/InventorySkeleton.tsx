import { memo, type CSSProperties } from 'react';

const shimmer: CSSProperties = { background: 'var(--color-surface-hover)', borderRadius: 6, animation: 'shimmer 1.5s infinite' };

export const SkeletonMetricCards = memo(function SkeletonMetricCards({ count = 4 }: { count?: number }) {
  return (
    <div role="status" aria-label="Loading metrics" style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: 'var(--space-component-gap)' }}>
      {Array.from({ length: count }).map((_, i) => (
        <div key={i} style={{ background: 'var(--color-surface)', border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', padding: '20px 24px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 16 }}>
            <div style={{ ...shimmer, width: 80, height: 14 }} />
            <div style={{ ...shimmer, width: 36, height: 36, borderRadius: 'var(--radius-md)' }} />
          </div>
          <div style={{ ...shimmer, width: 120, height: 28, marginBottom: 8 }} />
          <div style={{ ...shimmer, width: 100, height: 12 }} />
        </div>
      ))}
    </div>
  );
});

export const SkeletonTable = memo(function SkeletonTable({ rows = 6 }: { rows?: number }) {
  return (
    <div role="status" aria-label="Loading table" style={{ border: '1px solid var(--color-border)', borderRadius: 'var(--radius-lg)', overflow: 'hidden' }}>
      <div style={{ display: 'flex', gap: 16, padding: '12px 16px', borderBottom: '1px solid var(--color-border)', background: 'var(--color-surface)' }}>
        {[15, 20, 15, 12, 18, 20].map((w, i) => <div key={i} style={{ ...shimmer, width: `${w}%`, height: 14 }} />)}
      </div>
      {Array.from({ length: rows }).map((_, r) => (
        <div key={r} style={{ display: 'flex', gap: 16, padding: '12px 16px', borderBottom: '1px solid var(--color-border)' }}>
          {[15, 20, 15, 12, 18, 20].map((w, c) => <div key={c} style={{ ...shimmer, width: `${w}%`, height: 12 }} />)}
        </div>
      ))}
    </div>
  );
});

export const SkeletonSearch = memo(function SkeletonSearch() {
  return (
    <div role="status" aria-label="Loading search" style={{ display: 'flex', gap: 12 }}>
      <div style={{ ...shimmer, flex: 1, height: 40, borderRadius: 'var(--radius-md)' }} />
      <div style={{ ...shimmer, width: 120, height: 40, borderRadius: 'var(--radius-md)' }} />
    </div>
  );
});

export const SkeletonFilters = memo(function SkeletonFilters({ count = 4 }: { count?: number }) {
  return (
    <div role="status" aria-label="Loading filters" style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
      {Array.from({ length: count }).map((_, i) => <div key={i} style={{ ...shimmer, width: 120, height: 36, borderRadius: 'var(--radius-md)' }} />)}
    </div>
  );
});

export const SkeletonDashboard = memo(function SkeletonDashboard() {
  return (
    <div role="status" aria-label="Loading dashboard" style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }}>
      <SkeletonMetricCards count={4} />
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {Array.from({ length: 4 }).map((_, i) => <div key={i} style={{ ...shimmer, height: 160, borderRadius: 'var(--radius-lg)' }} />)}
      </div>
      <SkeletonTable rows={6} />
    </div>
  );
});
