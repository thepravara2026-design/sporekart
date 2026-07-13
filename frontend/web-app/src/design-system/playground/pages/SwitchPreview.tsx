import { useState } from 'react';

const sizes = ['sm', 'md', 'lg'] as const;

const sizeDimensions: Record<string, { width: number; height: number; thumb: number }> = {
  sm: { width: 32, height: 18, thumb: 14 },
  md: { width: 44, height: 24, thumb: 20 },
  lg: { width: 56, height: 30, thumb: 26 },
};

interface SwitchDemoProps {
  label: string;
  description?: string;
  defaultOn?: boolean;
  disabled?: boolean;
  loading?: boolean;
  error?: boolean;
  success?: boolean;
  size?: 'sm' | 'md' | 'lg';
}

function SwitchDemo({ label, description, defaultOn = false, disabled = false, loading = false, error, success, size = 'md' }: SwitchDemoProps) {
  const [on, setOn] = useState(defaultOn);
  const dim = sizeDimensions[size];

  const trackColor = on
    ? error
      ? 'var(--color-bg-danger-default, #dc2626)'
      : success
        ? 'var(--color-bg-success-default, #16a34a)'
        : 'var(--color-bg-primary-default, #2563eb)'
    : 'var(--color-bg-disabled, #d1d5db)';

  const handleToggle = () => {
    if (!disabled && !loading) setOn((v) => !v);
  };

  return (
    <div style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)', display: 'flex', alignItems: 'center', gap: '12px' }}>
      <div style={{ display: 'flex', flexDirection: 'column', flex: 1, gap: '2px' }}>
        <span style={{ fontSize: 'var(--text-base)', color: 'var(--color-text-primary)' }}>{label}</span>
        {description && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>{description}</span>}
      </div>
      <button
        onClick={handleToggle}
        disabled={disabled || loading}
        style={{
          position: 'relative',
          width: dim.width,
          height: dim.height,
          borderRadius: dim.height / 2,
          background: trackColor,
          border: 'none',
          cursor: disabled || loading ? 'not-allowed' : 'pointer',
          padding: 0,
          transition: 'background 0.2s',
          flexShrink: 0,
        }}
      >
        {loading ? (
          <span
            style={{
              position: 'absolute',
              top: '50%',
              left: on ? dim.width - dim.thumb - 2 : 2,
              transform: 'translateY(-50%)',
              width: dim.thumb,
              height: dim.thumb,
              borderRadius: '50%',
              background: '#fff',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
            }}
          >
            <span style={{ display: 'inline-block', width: dim.thumb * 0.5, height: dim.thumb * 0.5, border: '2px solid var(--color-text-secondary)', borderTopColor: 'transparent', borderRadius: '50%', animation: 'sk-spin 0.6s linear infinite' }} />
          </span>
        ) : (
          <span
            style={{
              position: 'absolute',
              top: '50%',
              left: on ? dim.width - dim.thumb - 2 : 2,
              transform: 'translateY(-50%)',
              width: dim.thumb,
              height: dim.thumb,
              borderRadius: '50%',
              background: '#fff',
              transition: 'left 0.2s',
              boxShadow: '0 1px 3px rgba(0,0,0,0.2)',
            }}
          />
        )}
      </button>
    </div>
  );
}

export default function SwitchPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Toggle Switch</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Toggle switch with all states and sizes</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>States</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '16px' }}>
          <SwitchDemo label="Off" />
          <SwitchDemo label="On" defaultOn />
          <SwitchDemo label="Disabled - Off" disabled />
          <SwitchDemo label="Disabled - On" disabled defaultOn />
          <SwitchDemo label="Loading" loading />
          <SwitchDemo label="With Label" description="This is a description for the switch" />
          <SwitchDemo label="Error" error defaultOn />
          <SwitchDemo label="Success" success defaultOn />
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Sizes</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '16px' }}>
          {sizes.map((size) => (
            <SwitchDemo key={size} label={`Size: ${size}`} size={size} />
          ))}
          {sizes.map((size) => (
            <SwitchDemo key={`${size}-on`} label={`Size: ${size} (On)`} size={size} defaultOn />
          ))}
        </div>
      </section>
    </div>
  );
}
