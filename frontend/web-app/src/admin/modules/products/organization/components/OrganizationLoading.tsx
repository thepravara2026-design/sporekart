const skPulse = `@keyframes sk-pulse { 0%, 100% { opacity: 0.4; } 50% { opacity: 0.8; } }`;

function TreeSkeleton() {
  const widths = [220, 180, 240, 160, 200, 180, 220, 160, 200, 180];
  return (
    <>
      <style>{skPulse}</style>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 4, padding: 'var(--space-component-gap)' }}>
        {widths.map((w, i) => (
          <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 8, paddingLeft: i > 2 ? 24 : i > 0 ? 12 : 0 }}>
            <div style={{ width: 16, height: 16, borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)', animation: 'sk-pulse 1.5s ease-in-out infinite' }} />
            <div style={{ width: w, height: 20, borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)', animation: 'sk-pulse 1.5s ease-in-out infinite' }} />
          </div>
        ))}
      </div>
    </>
  );
}

function TableSkeleton({ rows = 6, cols = 4 }: { rows?: number; cols?: number }) {
  return (
    <>
      <style>{skPulse}</style>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
        {Array.from({ length: rows }).map((_, r) => (
          <div key={r} style={{ display: 'flex', gap: 'var(--space-inline-sm)', padding: '10px 12px' }}>
            {Array.from({ length: cols }).map((_, c) => (
              <div key={c} style={{ flex: 1, height: 16, borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)', animation: 'sk-pulse 1.5s ease-in-out infinite' }} />
            ))}
          </div>
        ))}
      </div>
    </>
  );
}

function CardSkeleton({ count = 6 }: { count?: number }) {
  return (
    <>
      <style>{skPulse}</style>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(220px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {Array.from({ length: count }).map((_, i) => (
          <div key={i} style={{ borderRadius: 'var(--radius-card)', overflow: 'hidden', border: '1px solid var(--color-border-default)' }}>
            <div style={{ height: 120, background: 'var(--color-bg-surface-raised)', animation: 'sk-pulse 1.5s ease-in-out infinite' }} />
            <div style={{ padding: 12, display: 'flex', flexDirection: 'column', gap: 8 }}>
              <div style={{ height: 14, width: '70%', borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)', animation: 'sk-pulse 1.5s ease-in-out infinite' }} />
              <div style={{ height: 12, width: '50%', borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)', animation: 'sk-pulse 1.5s ease-in-out infinite' }} />
            </div>
          </div>
        ))}
      </div>
    </>
  );
}

export const OrganizationLoading = {
  Tree: TreeSkeleton,
  Table: TableSkeleton,
  Card: CardSkeleton,
};

export default OrganizationLoading;
