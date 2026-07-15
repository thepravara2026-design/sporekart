import React from 'react';

const kf = `@keyframes skp { 0%,100% { opacity: 0.4; } 50% { opacity: 0.8; } }`;
function B({ w, h = 16 }: { w: number; h?: number }) {
  return <div style={{ width: w, height: h, borderRadius: 'var(--radius-xs)', background: 'var(--color-bg-surface-raised)', animation: 'skp 1.5s ease-in-out infinite' }} />;
}

export const PreviewSkeleton: React.FC = () => (
  <><style>{kf}</style><div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', maxWidth: 600 }}><B w={300} h={20} /><div style={{ height: 8 }} /><B w={400} h={14} /><div style={{ height: 8 }} /><B w={350} h={14} /><div style={{ height: 8 }} /><B w={200} h={14} /></div></>
);

export const CardSkeleton: React.FC = () => (
  <><style>{kf}</style><div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 12 }}>
    {[1, 2, 3, 4].map((i) => <div key={i} style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}><B w={180} h={18} /><div style={{ height: 8 }} /><B w={240} h={14} /><div style={{ height: 8 }} /><B w={160} h={14} /></div>)}
  </div></>
);

export const ReportSkeleton: React.FC = () => (
  <><style>{kf}</style><div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)' }}>
    {[1, 2, 3, 4, 5].map((i) => <div key={i} style={{ display: 'flex', gap: 12, padding: '10px 0', borderBottom: '1px solid var(--color-border)' }}><B w={200} /><B w={100} /><B w={80} /><div style={{ flex: 1 }}><B w={60} /></div></div>)}
  </div></>
);
