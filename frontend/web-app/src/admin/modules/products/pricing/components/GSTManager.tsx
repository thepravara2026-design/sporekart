import React from 'react';
import type { GSTEntry } from '../types';

interface GSTManagerProps {
  entries: GSTEntry[];
}

const sectionStyle: React.CSSProperties = {
  padding: 'var(--space-component-gap)',
  display: 'flex',
  flexDirection: 'column',
  gap: 16,
};

const h2Style: React.CSSProperties = {
  margin: 0,
  fontSize: 'var(--text-h2)',
  color: 'var(--color-text-primary)',
};

const gridStyle: React.CSSProperties = {
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))',
  gap: 12,
};

const cardStyle: React.CSSProperties = {
  padding: 16,
  borderRadius: 'var(--radius-md)',
  border: '1px solid var(--color-border)',
  background: 'var(--color-bg-surface-default)',
};

const detailRow: React.CSSProperties = {
  display: 'flex',
  justifyContent: 'space-between',
  padding: '4px 0',
  fontSize: 'var(--text-body-xs)',
  borderBottom: '1px solid var(--color-border)',
};

const pctCircle: React.CSSProperties = {
  width: 48,
  height: 48,
  borderRadius: '50%',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  fontSize: 'var(--text-h4)',
  fontWeight: 700,
  marginBottom: 8,
};

export const GSTManager: React.FC<GSTManagerProps> = React.memo(({ entries }) => {
  return (
    <div style={sectionStyle}>
      <h2 style={h2Style}>GST Configuration</h2>
      <div style={gridStyle}>
        {entries.length === 0 && (
          <div style={{ gridColumn: '1 / -1', textAlign: 'center', padding: 48, color: 'var(--color-text-tertiary)' }}>
            No GST entries configured
          </div>
        )}
        {entries.map((g) => (
          <div key={g.id} style={cardStyle}>
            <div style={{ display: 'flex', alignItems: 'center', gap: 12, marginBottom: 8 }}>
              <div style={{ ...pctCircle, background: `var(--color-accent-${g.percentage <= 5 ? 'green' : g.percentage <= 12 ? 'blue' : g.percentage <= 18 ? 'orange' : 'red'}`, color: '#fff' }}>
                {g.percentage}%
              </div>
              <div>
                <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 600, color: 'var(--color-text-primary)' }}>
                  {g.code}
                </div>
                <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>
                  {g.category}
                </div>
              </div>
            </div>
            <div style={detailRow}>
              <span style={{ color: 'var(--color-text-tertiary)' }}>CGST</span>
              <span style={{ fontWeight: 500 }}>{g.cgst}%</span>
            </div>
            <div style={detailRow}>
              <span style={{ color: 'var(--color-text-tertiary)' }}>SGST</span>
              <span style={{ fontWeight: 500 }}>{g.sgst}%</span>
            </div>
            <div style={detailRow}>
              <span style={{ color: 'var(--color-text-tertiary)' }}>IGST</span>
              <span style={{ fontWeight: 500 }}>{g.igst}%</span>
            </div>
            {g.cess && (
              <div style={detailRow}>
                <span style={{ color: 'var(--color-text-tertiary)' }}>Cess</span>
                <span style={{ fontWeight: 500 }}>{g.cess}%</span>
              </div>
            )}
            <div style={detailRow}>
              <span style={{ color: 'var(--color-text-tertiary)' }}>Type</span>
              <span>{g.type}</span>
            </div>
            <div style={detailRow}>
              <span style={{ color: 'var(--color-text-tertiary)' }}>Status</span>
              <span style={{ color: g.status === 'active' ? 'var(--color-accent-green)' : 'var(--color-accent-red)' }}>{g.status}</span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
});
