import React from 'react';

export interface PropDef {
  name: string;
  type: string;
  required: boolean;
  default?: string;
  description: string;
}

export interface PropsTableProps {
  props: PropDef[];
  title?: string;
}

const wrapperStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: '12px',
  fontFamily: 'var(--font-family-base, sans-serif)',
};

const titleStyle: React.CSSProperties = {
  fontSize: 'var(--text-h4, 18px)',
  fontWeight: 'var(--weight-semibold, 600)',
  color: 'var(--color-text-primary)',
  margin: 0,
};

const tableStyle: React.CSSProperties = {
  width: '100%',
  borderCollapse: 'collapse',
  fontSize: 'var(--text-body, 14px)',
  color: 'var(--color-text-primary)',
};

const thStyle: React.CSSProperties = {
  textAlign: 'left',
  padding: '10px 12px',
  fontWeight: 'var(--weight-semibold, 600)',
  fontSize: 'var(--text-caption, 12px)',
  textTransform: 'uppercase',
  letterSpacing: '0.05em',
  color: 'var(--color-text-tertiary, #94a3b8)',
  borderBottom: '2px solid var(--color-border-default, #e2e8f0)',
  background: 'var(--color-bg-surface-raised, #f8fafc)',
};

const tdStyle: React.CSSProperties = {
  padding: '10px 12px',
  borderBottom: '1px solid var(--color-border-subtle, #f1f5f9)',
  verticalAlign: 'top',
  lineHeight: 1.5,
};

const codeStyle: React.CSSProperties = {
  fontFamily: 'var(--font-family-mono, "SF Mono", Monaco, monospace)',
  fontSize: 'var(--text-code, 13px)',
  background: 'var(--color-bg-code, #f1f5f9)',
  padding: '2px 6px',
  borderRadius: 'var(--radius-sm, 3px)',
  color: 'var(--color-text-code, #1e293b)',
  wordBreak: 'break-all',
};

const requiredBadge: React.CSSProperties = {
  display: 'inline-block',
  padding: '2px 8px',
  fontSize: 'var(--text-caption, 11px)',
  fontWeight: 'var(--weight-semibold, 600)',
  borderRadius: 'var(--radius-sm, 3px)',
  background: 'var(--color-bg-danger-subtle, #fef2f2)',
  color: 'var(--color-text-danger, #dc2626)',
  textTransform: 'uppercase',
  letterSpacing: '0.03em',
};

const optionalBadge: React.CSSProperties = {
  display: 'inline-block',
  padding: '2px 8px',
  fontSize: 'var(--text-caption, 11px)',
  fontWeight: 'var(--weight-medium, 500)',
  borderRadius: 'var(--radius-sm, 3px)',
  background: 'var(--color-bg-surface-raised, #f1f5f9)',
  color: 'var(--color-text-tertiary, #64748b)',
  textTransform: 'uppercase',
  letterSpacing: '0.03em',
};

export function PropsTable({ props, title }: PropsTableProps) {
  return (
    <div style={wrapperStyle}>
      {title && <h3 style={titleStyle}>{title}</h3>}
      <div style={{ overflowX: 'auto' }}>
        <table style={tableStyle}>
          <thead>
            <tr>
              <th style={thStyle}>Name</th>
              <th style={thStyle}>Type</th>
              <th style={thStyle}>Required</th>
              <th style={thStyle}>Default</th>
              <th style={thStyle}>Description</th>
            </tr>
          </thead>
          <tbody>
            {props.map((prop) => (
              <tr key={prop.name}>
                <td style={tdStyle}>
                  <code style={codeStyle}>{prop.name}</code>
                </td>
                <td style={tdStyle}>
                  <code style={codeStyle}>{prop.type}</code>
                </td>
                <td style={tdStyle}>
                  <span style={prop.required ? requiredBadge : optionalBadge}>
                    {prop.required ? 'Required' : 'Optional'}
                  </span>
                </td>
                <td style={tdStyle}>
                  {prop.default !== undefined ? (
                    <code style={codeStyle}>{prop.default}</code>
                  ) : (
                    <span style={{ color: 'var(--color-text-tertiary, #94a3b8)' }}>—</span>
                  )}
                </td>
                <td style={tdStyle}>
                  <span style={{ color: 'var(--color-text-secondary)' }}>{prop.description}</span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default PropsTable;
