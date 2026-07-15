import React from 'react';
import type { SeoEntry } from '../types';

const sect: React.CSSProperties = { padding: 'var(--space-component-gap)', display: 'flex', flexDirection: 'column', gap: 16 };
const table: React.CSSProperties = { width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body-sm)' };
const th: React.CSSProperties = { textAlign: 'left', padding: '10px 12px', borderBottom: '2px solid var(--color-border)', color: 'var(--color-text-tertiary)', fontWeight: 600, textTransform: 'uppercase', fontSize: 'var(--text-body-xs)', letterSpacing: '0.5px' };
const td: React.CSSProperties = { padding: '10px 12px', borderBottom: '1px solid var(--color-border)', color: 'var(--color-text-primary)' };

export const SeoUrlManager: React.FC<{ entries: SeoEntry[] }> = React.memo(({ entries }) => {
  return (
    <div style={sect}>
      <h2 style={{ margin: 0, fontSize: 'var(--text-h2)', color: 'var(--color-text-primary)' }}>URL Management</h2>
      <div style={{ overflowX: 'auto', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)' }}>
        <table style={table}>
          <thead>
            <tr>
              <th style={th}>Product</th>
              <th style={th}>Slug</th>
              <th style={th}>Canonical URL</th>
              <th style={th}>Index</th>
              <th style={th}>Follow</th>
            </tr>
          </thead>
          <tbody>
            {entries.map((e) => (
              <tr key={e.id}>
                <td style={{ ...td, fontWeight: 500 }}>{e.productName}</td>
                <td style={{ ...td, fontFamily: 'monospace', fontSize: 'var(--text-body-xs)' }}>/{e.slug}</td>
                <td style={{ ...td, fontSize: 'var(--text-body-xs)', color: 'var(--color-accent-blue)', maxWidth: 300, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{e.canonicalUrl}</td>
                <td style={td}>{e.indexable ? <span style={{ color: 'var(--color-accent-green)', fontWeight: 600 }}>Index</span> : <span style={{ color: 'var(--color-accent-red)', fontWeight: 600 }}>No Index</span>}</td>
                <td style={td}>{e.followLinks ? <span style={{ color: 'var(--color-accent-green)' }}>Follow</span> : <span style={{ color: 'var(--color-accent-red)' }}>No Follow</span>}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
});
