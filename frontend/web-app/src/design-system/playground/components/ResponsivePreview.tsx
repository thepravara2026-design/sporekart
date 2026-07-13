import React, { useState } from 'react';

export interface ResponsivePreviewProps {
  children: React.ReactNode;
  defaultViewport?: 'desktop' | 'laptop' | 'tablet' | 'mobile';
}

interface ViewportDef {
  label: string;
  value: string;
  width: number;
  icon: string;
}

const viewports: ViewportDef[] = [
  { label: 'Desktop', value: 'desktop', width: 1280, icon: '🖥' },
  { label: 'Laptop', value: 'laptop', width: 1024, icon: '💻' },
  { label: 'Tablet', value: 'tablet', width: 768, icon: '📱' },
  { label: 'Mobile', value: 'mobile', width: 375, icon: '📲' },
];

const wrapperStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--space-section-gap, 16px)',
  fontFamily: 'var(--font-family-base, sans-serif)',
};

const toolbarStyle: React.CSSProperties = {
  display: 'flex',
  gap: '4px',
  padding: '8px',
  background: 'var(--color-bg-surface-raised, #f8fafc)',
  borderRadius: 'var(--radius-card, 8px)',
  border: '1px solid var(--color-border-default, #e2e8f0)',
  flexWrap: 'wrap',
};

const btnBaseStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: '6px',
  padding: '6px 14px',
  fontSize: 'var(--text-body, 14px)',
  borderRadius: 'var(--radius-sm, 4px)',
  border: '1px solid var(--color-border-default, #e2e8f0)',
  background: 'var(--color-bg-input, #ffffff)',
  color: 'var(--color-text-primary)',
  cursor: 'pointer',
  outline: 'none',
  transition: 'all 0.15s ease',
};

const activeBtnStyle: React.CSSProperties = {
  ...btnBaseStyle,
  background: 'var(--color-bg-brand-subtle, #eff6ff)',
  borderColor: 'var(--color-border-brand, #3b82f6)',
  color: 'var(--color-text-brand, #2563eb)',
  fontWeight: 'var(--weight-semibold, 600)',
};

const dimensionStyle: React.CSSProperties = {
  marginLeft: 'auto',
  fontSize: 'var(--text-caption, 12px)',
  color: 'var(--color-text-tertiary, #94a3b8)',
  display: 'flex',
  alignItems: 'center',
  gap: '4px',
};

const containerOuterStyle: React.CSSProperties = {
  overflow: 'auto',
  display: 'flex',
  justifyContent: 'center',
  padding: 'var(--space-card-padding, 16px) 0',
  background: 'var(--color-bg-surface-muted, #f1f5f9)',
  borderRadius: 'var(--radius-card, 8px)',
  border: '1px solid var(--color-border-default, #e2e8f0)',
  minHeight: '200px',
};

const getContainerInnerStyle = (width: number): React.CSSProperties => ({
  width: `${width}px`,
  maxWidth: '100%',
  background: 'var(--color-bg-surface-default, #ffffff)',
  borderRadius: 'var(--radius-sm, 4px)',
  boxShadow: 'var(--shadow-1, 0 1px 3px rgba(0,0,0,0.1))',
  overflow: 'hidden',
  transition: 'width 0.2s ease',
});

export function ResponsivePreview({
  children,
  defaultViewport = 'desktop',
}: ResponsivePreviewProps) {
  const [selected, setSelected] = useState(
    viewports.find((v) => v.value === defaultViewport) || viewports[0]
  );

  return (
    <div style={wrapperStyle}>
      <div style={toolbarStyle}>
        {viewports.map((vp) => (
          <button
            key={vp.value}
            onClick={() => setSelected(vp)}
            style={selected.value === vp.value ? activeBtnStyle : btnBaseStyle}
          >
            <span>{vp.icon}</span>
            <span>{vp.label}</span>
          </button>
        ))}
        <span style={dimensionStyle}>
          {selected.width}px
        </span>
      </div>
      <div style={containerOuterStyle}>
        <div style={getContainerInnerStyle(selected.width)}>
          {children}
        </div>
      </div>
    </div>
  );
}

export default ResponsivePreview;
