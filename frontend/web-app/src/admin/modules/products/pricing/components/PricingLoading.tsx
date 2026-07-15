import React from 'react';

const skPulse = `@keyframes sk-pulse { 0%, 100% { opacity: 0.4; } 50% { opacity: 0.8; } }`;

function SkeletonRow({ rowWidths }: { rowWidths: number[] }) {
  return (
    <div style={{ display: 'flex', alignItems: 'center', gap: 12, padding: '12px 16px', borderBottom: '1px solid var(--color-border)' }}>
      {rowWidths.map((w, i) => (
        <div key={i} style={{ width: w, height: 16, borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)', animation: 'sk-pulse 1.5s ease-in-out infinite' }} />
      ))}
    </div>
  );
}

function SkeletonCard() {
  return (
    <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
      <div style={{ width: '60%', height: 18, borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)', animation: 'sk-pulse 1.5s ease-in-out infinite', marginBottom: 12 }} />
      <div style={{ width: '40%', height: 14, borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)', animation: 'sk-pulse 1.5s ease-in-out infinite', marginBottom: 8 }} />
      <div style={{ width: '80%', height: 14, borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)', animation: 'sk-pulse 1.5s ease-in-out infinite' }} />
    </div>
  );
}

export const PricingTableSkeleton: React.FC = () => (
  <>
    <style>{skPulse}</style>
    <div style={{ borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', overflow: 'hidden' }}>
      {[180, 200, 160, 140, 220, 190].map((w, i) => (
        <SkeletonRow key={i} rowWidths={[w, 100, 100, 80, 80, 60]} />
      ))}
    </div>
  </>
);

export const PricingCardSkeleton: React.FC = () => (
  <>
    <style>{skPulse}</style>
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 16 }}>
      {[1, 2, 3, 4, 5, 6].map((i) => (
        <SkeletonCard key={i} />
      ))}
    </div>
  </>
);

export const StatCardSkeleton: React.FC = () => (
  <>
    <style>{skPulse}</style>
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 16 }}>
      {[1, 2, 3, 4, 5, 6].map((i) => (
        <div key={i} style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <div style={{ width: 80, height: 12, borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)', animation: 'sk-pulse 1.5s ease-in-out infinite', marginBottom: 8 }} />
          <div style={{ width: 60, height: 28, borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)', animation: 'sk-pulse 1.5s ease-in-out infinite' }} />
        </div>
      ))}
    </div>
  </>
);
