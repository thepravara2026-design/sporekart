import React, { useState } from 'react';

export interface ThemePreviewProps {
  children: React.ReactNode;
  defaultTheme?: 'light' | 'dark' | 'high-contrast';
}

interface ThemeDef {
  label: string;
  value: string;
  icon: string;
}

const themes: ThemeDef[] = [
  { label: 'Light', value: 'light', icon: '☀️' },
  { label: 'Dark', value: 'dark', icon: '🌙' },
  { label: 'High Contrast', value: 'high-contrast', icon: '♿' },
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

const activeBtnStyle = (themeValue: string): React.CSSProperties => ({
  ...btnBaseStyle,
  background: themeValue === 'dark'
    ? 'var(--color-bg-brand-subtle, #1e293b)'
    : 'var(--color-bg-brand-subtle, #eff6ff)',
  borderColor: 'var(--color-border-brand, #3b82f6)',
  color: 'var(--color-text-brand, #2563eb)',
  fontWeight: 'var(--weight-semibold, 600)',
});

const contentWrapperStyle: React.CSSProperties = {
  border: '1px solid var(--color-border-default, #e2e8f0)',
  borderRadius: 'var(--radius-card, 8px)',
  padding: 'var(--space-panel-padding, 24px)',
  background: 'var(--color-bg-surface-default, #ffffff)',
  transition: 'background 0.2s ease, color 0.2s ease',
  overflow: 'auto',
};

export function ThemePreview({
  children,
  defaultTheme = 'light',
}: ThemePreviewProps) {
  const [theme, setTheme] = useState(defaultTheme);

  return (
    <div style={wrapperStyle}>
      <div style={toolbarStyle}>
        {themes.map((t) => (
          <button
            key={t.value}
            onClick={() => setTheme(t.value as typeof defaultTheme)}
            style={theme === t.value ? activeBtnStyle(t.value) : btnBaseStyle}
          >
            <span>{t.icon}</span>
            <span>{t.label}</span>
          </button>
        ))}
      </div>
      <div data-theme={theme} style={contentWrapperStyle}>
        {children}
      </div>
    </div>
  );
}

export default ThemePreview;
