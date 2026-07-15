import React from 'react';

const pulse = `@keyframes vp { 0%,100% { opacity: 0.4; } 50% { opacity: 0.8; } }`;

function Bar({ w, h = 16 }: { w: number; h?: number }) {
  return <div style={{ width: w, height: h, borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)', animation: 'vp 1.5s ease-in-out infinite' }} />;
}

export const TableSkeleton: React.FC = () => (
  <>
    <style>{pulse}</style>
    <div style={{ borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', overflow: 'hidden' }}>
      {[1, 2, 3, 4, 5, 6].map((i) => (
        <div key={i} style={{ display: 'flex', gap: 12, padding: '12px 16px', borderBottom: '1px solid var(--color-border)' }}>
          <Bar w={180} /><Bar w={100} /><Bar w={120} /><Bar w={80} /><Bar w={60} />
        </div>
      ))}
    </div>
  </>
);

export const CardSkeleton: React.FC = () => (
  <>
    <style>{pulse}</style>
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 12 }}>
      {[1, 2, 3, 4, 5, 6].map((i) => (
        <div key={i} style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <Bar w={160} h={18} /><div style={{ height: 8 }} /><Bar w={120} h={14} /><div style={{ height: 8 }} /><Bar w={200} h={14} />
        </div>
      ))}
    </div>
  </>
);

export const MatrixSkeleton: React.FC = () => (
  <>
    <style>{pulse}</style>
    <div style={{ borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', overflow: 'hidden' }}>
      <div style={{ display: 'flex', gap: 12, padding: '12px 16px', borderBottom: '2px solid var(--color-border)', background: 'var(--color-bg-surface-raised)' }}>
        <Bar w={200} h={14} /><Bar w={100} h={14} /><Bar w={80} h={14} /><Bar w={80} h={14} /><Bar w={60} h={14} /><Bar w={60} h={14} />
      </div>
      {[1, 2, 3, 4, 5, 6].map((i) => (
        <div key={i} style={{ display: 'flex', gap: 12, padding: '12px 16px', borderBottom: '1px solid var(--color-border)' }}>
          <Bar w={200} /><Bar w={100} /><Bar w={80} /><Bar w={80} /><Bar w={60} /><Bar w={60} />
        </div>
      ))}
    </div>
  </>
);
