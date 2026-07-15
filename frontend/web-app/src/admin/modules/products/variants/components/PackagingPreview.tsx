import React from 'react';
import type { PackagingInfo } from '../types';

interface PackagingPreviewProps {
  packaging: PackagingInfo[];
  selectedId: string | null;
  onSelect: (id: string) => void;
}

const section: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };
const grid: React.CSSProperties = { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: 12 };
const base: React.CSSProperties = { padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)', cursor: 'pointer', transition: 'border-color 0.15s' };
const sel: React.CSSProperties = { ...base, borderColor: 'var(--color-accent-blue)', boxShadow: '0 0 0 2px var(--color-accent-blue)' };

export const PackagingPreview: React.FC<PackagingPreviewProps> = React.memo(({ packaging, selectedId, onSelect }) => {
  return (
    <div style={section}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Packaging Preview</h2>
      <div style={grid}>
        {packaging.map((p) => (
          <div key={p.id} style={p.id === selectedId ? sel : base} onClick={() => onSelect(p.id)} role="button" tabIndex={0}
            onKeyDown={(e) => { if (e.key === 'Enter') onSelect(p.id); }}>
            <div style={{ display: 'flex', justifyContent: 'center', marginBottom: 12 }}>
              <div style={{
                width: 120, height: 100, borderRadius: 'var(--radius-sm)', border: '2px solid var(--color-border)',
                display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center',
                background: 'var(--color-bg-surface-raised)', position: 'relative',
              }}>
                <span style={{ fontSize: 24, opacity: 0.4 }}>📦</span>
                <span style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', marginTop: 4 }}>{p.type}</span>
                <span style={{ position: 'absolute', bottom: 4, right: 4, fontSize: 10, color: 'var(--color-text-tertiary)' }}>
                  {p.length}×{p.width}×{p.height} {p.dimensionUnit}
                </span>
              </div>
            </div>
            <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)', textAlign: 'center' }}>{p.name}</div>
            <div style={{ textAlign: 'center', fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)', marginTop: 2 }}>
              {p.weight} {p.weightUnit} · {p.material ?? 'N/A'}
            </div>
            <div style={{ display: 'flex', justifyContent: 'center', gap: 12, marginTop: 8, fontSize: 'var(--text-body-xs)' }}>
              {p.storageInstructions && <span style={{ color: 'var(--color-text-tertiary)', maxWidth: 200, textAlign: 'center' }}>{p.storageInstructions}</span>}
            </div>
            <div style={{ display: 'flex', justifyContent: 'center', gap: 4, marginTop: 8 }}>
              {p.isPrimary && <span style={{ padding: '2px 6px', borderRadius: 6, fontSize: 10, fontWeight: 600, background: 'var(--color-accent-blue)20', color: 'var(--color-accent-blue)' }}>Primary</span>}
              {p.isMasterCarton && <span style={{ padding: '2px 6px', borderRadius: 6, fontSize: 10, fontWeight: 600, background: 'var(--color-accent-purple)20', color: 'var(--color-accent-purple)' }}>Master Carton</span>}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
});
