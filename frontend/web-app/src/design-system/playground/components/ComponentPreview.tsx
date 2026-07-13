import React, { useState, useCallback } from 'react';
import { Copy } from '../../icons/registry';

export interface ComponentPreviewProps {
  title: string;
  description: string;
  component: React.ReactNode;
  controls?: React.ReactNode;
  defaultVariant?: string;
  variants?: { label: string; value: string }[];
  sizes?: { label: string; value: string }[];
  showThemeToggle?: boolean;
  showWidthToggle?: boolean;
}

const widthOptions: { label: string; value: string }[] = [
  { label: 'Fluid', value: 'fluid' },
  { label: 'Narrow', value: 'narrow' },
  { label: 'Medium', value: 'medium' },
  { label: 'Wide', value: 'wide' },
];

const stateOptions: { label: string; value: string }[] = [
  { label: 'Normal', value: 'normal' },
  { label: 'Hover', value: 'hover' },
  { label: 'Active', value: 'active' },
  { label: 'Disabled', value: 'disabled' },
  { label: 'Focus', value: 'focus' },
  { label: 'Loading', value: 'loading' },
];

const themeOptions: { label: string; value: string }[] = [
  { label: 'Light', value: 'light' },
  { label: 'Dark', value: 'dark' },
];

const widthMaxValues: Record<string, string> = {
  fluid: '100%',
  narrow: '480px',
  medium: '768px',
  wide: '1024px',
};

const SIZES: { label: string; value: string }[] = [
  { label: 'Small', value: 'small' },
  { label: 'Medium', value: 'medium' },
  { label: 'Large', value: 'large' },
];

const containerStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: 'var(--space-section-gap, 24px)',
  fontFamily: 'var(--font-family-base, sans-serif)',
};

const headerStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: '4px',
};

const titleStyle: React.CSSProperties = {
  fontSize: 'var(--text-h3, 20px)',
  fontWeight: 'var(--weight-semibold, 600)',
  color: 'var(--color-text-primary)',
  margin: 0,
};

const descStyle: React.CSSProperties = {
  fontSize: 'var(--text-body, 14px)',
  color: 'var(--color-text-secondary)',
  margin: 0,
};

const previewWrapperStyle = (width: string): React.CSSProperties => ({
  border: '1px solid var(--color-border-default, #e2e8f0)',
  borderRadius: 'var(--radius-card, 8px)',
  background: 'var(--color-bg-surface-default, #ffffff)',
  overflow: 'auto',
  padding: 'var(--space-panel-padding, 24px)',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  maxWidth: widthMaxValues[width],
  transition: 'max-width 0.2s ease, background 0.2s ease',
  minHeight: '120px',
});

const controlsPanelStyle: React.CSSProperties = {
  display: 'flex',
  flexWrap: 'wrap',
  gap: '12px',
  padding: 'var(--space-card-padding, 16px)',
  background: 'var(--color-bg-surface-raised, #f8fafc)',
  borderRadius: 'var(--radius-card, 8px)',
  border: '1px solid var(--color-border-default, #e2e8f0)',
  alignItems: 'flex-end',
};

const controlGroupStyle: React.CSSProperties = {
  display: 'flex',
  flexDirection: 'column',
  gap: '4px',
  minWidth: '120px',
};

const labelStyle: React.CSSProperties = {
  fontSize: 'var(--text-caption, 12px)',
  fontWeight: 'var(--weight-medium, 500)',
  color: 'var(--color-text-tertiary, #94a3b8)',
  textTransform: 'uppercase',
  letterSpacing: '0.05em',
};

const selectStyle: React.CSSProperties = {
  padding: '6px 10px',
  fontSize: 'var(--text-body, 14px)',
  borderRadius: 'var(--radius-sm, 4px)',
  border: '1px solid var(--color-border-default, #e2e8f0)',
  background: 'var(--color-bg-input, #ffffff)',
  color: 'var(--color-text-primary)',
  cursor: 'pointer',
  outline: 'none',
};

const toggleBtnStyle = (active: boolean): React.CSSProperties => ({
  padding: '6px 12px',
  fontSize: 'var(--text-body, 14px)',
  borderRadius: 'var(--radius-sm, 4px)',
  border: active ? '1px solid var(--color-border-brand, #3b82f6)' : '1px solid var(--color-border-default, #e2e8f0)',
  background: active ? 'var(--color-bg-brand-subtle, #eff6ff)' : 'var(--color-bg-input, #ffffff)',
  color: active ? 'var(--color-text-brand, #2563eb)' : 'var(--color-text-primary)',
  cursor: 'pointer',
  fontWeight: active ? 'var(--weight-semibold, 600)' : 'var(--weight-normal, 400)',
  outline: 'none',
});

const controlRowStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'flex-end',
  gap: '12px',
  flexWrap: 'wrap',
};

const checkboxLabelStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  gap: '6px',
  fontSize: 'var(--text-body, 14px)',
  color: 'var(--color-text-primary)',
  cursor: 'pointer',
  padding: '6px 0',
};

export function ComponentPreview({
  title,
  description,
  component,
  controls,
  defaultVariant,
  variants,
  sizes,
  showThemeToggle,
  showWidthToggle,
}: ComponentPreviewProps) {
  const [selectedVariant, setSelectedVariant] = useState(defaultVariant || (variants && variants[0]?.value) || '');
  const [selectedSize, setSelectedSize] = useState((sizes && sizes[0]?.value) || (SIZES[0]?.value) || '');
  const [selectedState, setSelectedState] = useState('normal');
  const [theme, setTheme] = useState('light');
  const [width, setWidth] = useState('fluid');
  const [loading, setLoading] = useState(false);
  const [disabled, setDisabled] = useState(false);

  const handleCopy = useCallback(() => {
    const code = `<${title.replace(/\s+/g, '')} ${selectedVariant ? `variant="${selectedVariant}" ` : ''}${selectedSize ? `size="${selectedSize}" ` : ''}${disabled ? 'disabled ' : ''}${loading ? 'loading ' : ''}/>`;
    navigator.clipboard.writeText(code).catch(() => {});
  }, [title, selectedVariant, selectedSize, disabled, loading]);

  return (
    <div style={containerStyle}>
      <div style={headerStyle}>
        <h2 style={titleStyle}>{title}</h2>
        <p style={descStyle}>{description}</p>
      </div>

      <div
        data-theme={theme}
        style={previewWrapperStyle(width)}
      >
        {component}
      </div>

      <div style={controlsPanelStyle}>
        <div style={controlRowStyle}>
          {variants && variants.length > 0 && (
            <div style={controlGroupStyle}>
              <label style={labelStyle}>Variant</label>
              <select
                value={selectedVariant}
                onChange={(e) => setSelectedVariant(e.target.value)}
                style={selectStyle}
              >
                {variants.map((v) => (
                  <option key={v.value} value={v.value}>{v.label}</option>
                ))}
              </select>
            </div>
          )}

          {sizes && sizes.length > 0 && (
            <div style={controlGroupStyle}>
              <label style={labelStyle}>Size</label>
              <select
                value={selectedSize}
                onChange={(e) => setSelectedSize(e.target.value)}
                style={selectStyle}
              >
                {sizes.map((s) => (
                  <option key={s.value} value={s.value}>{s.label}</option>
                ))}
              </select>
            </div>
          )}

          <div style={controlGroupStyle}>
            <label style={labelStyle}>State</label>
            <select
              value={selectedState}
              onChange={(e) => setSelectedState(e.target.value)}
              style={selectStyle}
            >
              {stateOptions.map((s) => (
                <option key={s.value} value={s.value}>{s.label}</option>
              ))}
            </select>
          </div>

          {showThemeToggle && (
            <div style={controlGroupStyle}>
              <label style={labelStyle}>Theme</label>
              <div style={{ display: 'flex', gap: '4px' }}>
                {themeOptions.map((t) => (
                  <button
                    key={t.value}
                    onClick={() => setTheme(t.value)}
                    style={toggleBtnStyle(theme === t.value)}
                  >
                    {t.label}
                  </button>
                ))}
              </div>
            </div>
          )}

          {showWidthToggle && (
            <div style={controlGroupStyle}>
              <label style={labelStyle}>Width</label>
              <div style={{ display: 'flex', gap: '4px' }}>
                {widthOptions.map((w) => (
                  <button
                    key={w.value}
                    onClick={() => setWidth(w.value)}
                    style={toggleBtnStyle(width === w.value)}
                  >
                    {w.label}
                  </button>
                ))}
              </div>
            </div>
          )}
        </div>

        <div style={controlRowStyle}>
          <label style={checkboxLabelStyle}>
            <input
              type="checkbox"
              checked={loading}
              onChange={(e) => setLoading(e.target.checked)}
            />
            Loading
          </label>

          <label style={checkboxLabelStyle}>
            <input
              type="checkbox"
              checked={disabled}
              onChange={(e) => setDisabled(e.target.checked)}
            />
            Disabled
          </label>

          <button
            onClick={handleCopy}
            style={{
              ...toggleBtnStyle(false),
              display: 'flex',
              alignItems: 'center',
              gap: '4px',
              marginLeft: 'auto',
            }}
            title="Copy component code"
          >
            <Copy size={14} />
            Copy Code
          </button>
        </div>

        {controls && (
          <div style={{ width: '100%' }}>
            {controls}
          </div>
        )}
      </div>
    </div>
  );
}

export default ComponentPreview;
