import React from 'react';
import type { PackagingInfo } from '../types';

interface PackagingManagerProps {
  packaging: PackagingInfo[];
}

const section: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };
const grid: React.CSSProperties = { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 12 };
const card: React.CSSProperties = { padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' };
const row: React.CSSProperties = { display: 'flex', justifyContent: 'space-between', padding: '4px 0', fontSize: 'var(--text-body-xs)', borderBottom: '1px solid var(--color-border)' };

const pkgIcons: Record<string, string> = { pouch: '🛍️', bag: '👜', box: '📦', tray: '🍱', jar: '🫙', bottle: '🧴', carton: '📋', crate: '📦', bundle: '🎁', other: '📦' };

export const PackagingManager: React.FC<PackagingManagerProps> = React.memo(({ packaging }) => {
  return (
    <div style={section}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>Packaging Management</h2>
        <div style={{ display: 'flex', gap: 12, fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>
          <span>{packaging.filter((p) => p.isPrimary).length} primary</span>
          <span>{packaging.filter((p) => p.isMasterCarton).length} master cartons</span>
        </div>
      </div>
      <div style={grid}>
        {packaging.map((p) => (
          <div key={p.id} style={card}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 8 }}>
              <span style={{ fontSize: 24 }} aria-hidden="true">{pkgIcons[p.type] ?? '📦'}</span>
              <div>
                <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>{p.name}</div>
                <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>{p.type} · {p.material ?? 'N/A'}</div>
              </div>
              <div style={{ marginLeft: 'auto', display: 'flex', gap: 4 }}>
                {p.isMasterCarton && <span style={{ padding: '1px 6px', borderRadius: 6, fontSize: 10, fontWeight: 600, background: 'var(--color-accent-purple)20', color: 'var(--color-accent-purple)' }}>Master</span>}
                {p.isPrimary && <span style={{ padding: '1px 6px', borderRadius: 6, fontSize: 10, fontWeight: 600, background: 'var(--color-accent-blue)20', color: 'var(--color-accent-blue)' }}>Primary</span>}
              </div>
            </div>
            <div style={row}><span style={{ color: 'var(--color-text-tertiary)' }}>Weight</span><span style={{ fontWeight: 500 }}>{p.weight} {p.weightUnit}</span></div>
            <div style={row}><span style={{ color: 'var(--color-text-tertiary)' }}>Dimensions</span><span style={{ fontWeight: 500 }}>{p.length} × {p.width} × {p.height} {p.dimensionUnit}</span></div>
            {p.volume && <div style={row}><span style={{ color: 'var(--color-text-tertiary)' }}>Volume</span><span style={{ fontWeight: 500 }}>{p.volume} {p.volumeUnit}</span></div>}
            {p.storageInstructions && <div style={{ ...row, borderBottom: 'none' }}><span style={{ color: 'var(--color-text-tertiary)' }}>Storage</span><span style={{ fontSize: 'var(--text-body-xs)' }}>{p.storageInstructions}</span></div>}
          </div>
        ))}
      </div>
    </div>
  );
});
