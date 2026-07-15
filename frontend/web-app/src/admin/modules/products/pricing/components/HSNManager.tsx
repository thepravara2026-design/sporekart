import React from 'react';
import type { HSNEntry } from '../types';

interface HSNManagerProps {
  entries: HSNEntry[];
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

const tableStyle: React.CSSProperties = {
  width: '100%',
  borderCollapse: 'collapse',
  fontSize: 'var(--text-body-sm)',
};

const thStyle: React.CSSProperties = {
  textAlign: 'left',
  padding: '10px 12px',
  borderBottom: '2px solid var(--color-border)',
  color: 'var(--color-text-tertiary)',
  fontWeight: 600,
  textTransform: 'uppercase',
  fontSize: 'var(--text-body-xs)',
  letterSpacing: '0.5px',
};

const tdStyle: React.CSSProperties = {
  padding: '10px 12px',
  borderBottom: '1px solid var(--color-border)',
  color: 'var(--color-text-primary)',
};

export const HSNManager: React.FC<HSNManagerProps> = React.memo(({ entries }) => {
  return (
    <div style={sectionStyle}>
      <h2 style={h2Style}>HSN Code Management</h2>
      <div style={{ overflowX: 'auto', borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)' }}>
        <table style={tableStyle}>
          <thead>
            <tr>
              <th style={thStyle}>HSN Code</th>
              <th style={thStyle}>Description</th>
              <th style={thStyle}>Tax Category</th>
              <th style={thStyle}>Commodity Type</th>
              <th style={thStyle}>GST</th>
              <th style={thStyle}>Status</th>
            </tr>
          </thead>
          <tbody>
            {entries.length === 0 && (
              <tr>
                <td colSpan={6} style={{ ...tdStyle, textAlign: 'center', padding: 32, color: 'var(--color-text-tertiary)' }}>
                  No HSN entries configured
                </td>
              </tr>
            )}
            {entries.map((h) => (
              <tr key={h.id}>
                <td style={{ ...tdStyle, fontWeight: 700, fontFamily: 'monospace', fontSize: 'var(--text-body-sm)' }}>
                  {h.code}
                </td>
                <td style={tdStyle}>
                  <div>{h.description}</div>
                  <div style={{ fontSize: 'var(--text-body-xs)', color: 'var(--color-text-tertiary)' }}>
                    Updated: {new Date(h.updatedAt).toLocaleDateString()}
                  </div>
                </td>
                <td style={tdStyle}>{h.taxCategory}</td>
                <td style={tdStyle}>{h.commodityType}</td>
                <td style={{ ...tdStyle, fontWeight: 600 }}>{h.gstPercentage}%</td>
                <td style={tdStyle}>
                  <span style={{ color: h.status === 'active' ? 'var(--color-accent-green)' : 'var(--color-accent-red)', fontWeight: 600 }}>
                    {h.status}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
});
