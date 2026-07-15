import React from 'react';

const pulseKeyframes = `@keyframes vpulse { 0%,100% { opacity: 0.3; } 50% { opacity: 0.7; } }`;

function Bar({ w, h = 14 }: { w: number; h?: number }) {
  return <div style={{ width: w, height: h, borderRadius: 'var(--radius-xs)', background: 'linear-gradient(90deg, var(--color-bg-surface-raised) 0%, var(--color-bg-surface-default) 50%, var(--color-bg-surface-raised) 100%)', animation: 'vpulse 1.4s ease-in-out infinite' }} />;
}

function Circle({ size = 40 }: { size?: number }) {
  return <div style={{ width: size, height: size, borderRadius: '50%', background: 'var(--color-bg-surface-raised)', animation: 'vpulse 1.4s ease-in-out infinite' }} />;
}

export const ValidationDashboardSkeleton: React.FC = () => (
  <>
    <style>{pulseKeyframes}</style>
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', gap: 12 }} aria-label="Loading dashboard">
      {[1, 2, 3, 4, 5, 6].map((i) => (
        <div key={i} style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', gap: 8 }}>
          <Bar w={60} h={12} />
          <Bar w={40} h={24} />
          <Bar w={80} h={10} />
        </div>
      ))}
    </div>
  </>
);

export const ValidationTableSkeleton: React.FC = () => (
  <>
    <style>{pulseKeyframes}</style>
    <div style={{ borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', overflow: 'hidden' }} role="progressbar" aria-label="Loading table">
      <div style={{ display: 'flex', gap: 12, padding: '12px 16px', borderBottom: '2px solid var(--color-border)', background: 'var(--color-bg-surface-raised)' }}>
        <Bar w={160} /><Bar w={100} /><Bar w={80} /><Bar w={60} /><Bar w={60} />
      </div>
      {[1, 2, 3, 4, 5, 6].map((i) => (
        <div key={i} style={{ display: 'flex', gap: 12, padding: '12px 16px', borderBottom: '1px solid var(--color-border)' }}>
          <Bar w={160} /><Bar w={100} /><Bar w={80} /><Bar w={60} /><Bar w={60} />
        </div>
      ))}
    </div>
  </>
);

export const ValidationCardSkeleton: React.FC = () => (
  <>
    <style>{pulseKeyframes}</style>
    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 12 }} aria-label="Loading cards">
      {[1, 2, 3, 4, 5, 6].map((i) => (
        <div key={i} style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', display: 'flex', flexDirection: 'column', gap: 10 }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
            <Circle size={32} />
            <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 4 }}>
              <Bar w={140} h={14} />
              <Bar w={80} h={10} />
            </div>
          </div>
          <Bar w={180} h={10} />
          <Bar w={120} h={10} />
        </div>
      ))}
    </div>
  </>
);

export const ValidationProgressSkeleton: React.FC = () => (
  <>
    <style>{pulseKeyframes}</style>
    <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }} aria-label="Loading progress bars">
      {[1, 2, 3, 4, 5].map((i) => (
        <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
          <Bar w={100} h={12} />
          <div style={{ flex: 1, height: 10, borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)', animation: 'vpulse 1.4s ease-in-out infinite' }} />
          <Bar w={30} h={12} />
        </div>
      ))}
    </div>
  </>
);

export const ValidationChecklistSkeleton: React.FC = () => (
  <>
    <style>{pulseKeyframes}</style>
    <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }} aria-label="Loading checklist">
      {[1, 2, 3, 4, 5, 6, 7].map((i) => (
        <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 12, padding: '10px 12px', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <div style={{ width: 18, height: 18, borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)', animation: 'vpulse 1.4s ease-in-out infinite' }} />
          <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 4 }}>
            <Bar w={140} h={12} />
            <Bar w={200} h={10} />
          </div>
        </div>
      ))}
    </div>
  </>
);
