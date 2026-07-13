import { useState } from 'react';

function RadioGroupDemo({
  label,
  direction,
  disabledOptions,
  error,
}: {
  label: string;
  direction: 'horizontal' | 'vertical';
  disabledOptions?: string[];
  error?: boolean;
}) {
  const options = ['Option A', 'Option B', 'Option C'];
  const [selected, setSelected] = useState('Option A');

  return (
    <div style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)', display: 'flex', flexDirection: 'column', gap: '8px' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: error ? 'var(--color-text-danger, #dc2626)' : 'var(--color-text-secondary)' }}>{label}</span>
      <div style={{ display: 'flex', flexDirection: direction === 'horizontal' ? 'row' : 'column', gap: '12px', flexWrap: 'wrap' }}>
        {options.map((opt) => {
          const isDisabled = disabledOptions?.includes(opt);
          const isSelected = selected === opt;
          return (
            <label
              key={opt}
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: '8px',
                cursor: isDisabled ? 'not-allowed' : 'pointer',
                opacity: isDisabled ? 0.5 : 1,
              }}
            >
              <div
                onClick={() => { if (!isDisabled) setSelected(opt); }}
                style={{
                  width: 20,
                  height: 20,
                  flexShrink: 0,
                  borderRadius: '50%',
                  border: `2px solid ${isSelected ? 'var(--color-bg-primary-default, #2563eb)' : error ? 'var(--color-border-danger, #dc2626)' : 'var(--color-border-default)'}`,
                  background: isSelected ? 'var(--color-bg-primary-default, #2563eb)' : 'transparent',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  transition: 'all 0.15s',
                }}
              >
                {isSelected && <div style={{ width: 8, height: 8, borderRadius: '50%', background: '#fff' }} />}
              </div>
              <span style={{ fontSize: 'var(--text-base)', color: 'var(--color-text-primary)' }}>{opt}</span>
            </label>
          );
        })}
      </div>
      {error && <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-danger, #dc2626)' }}>Please select an option</span>}
    </div>
  );
}

export default function RadioPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Radio Group</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Radio groups with all layout options and states</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Layouts</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '16px' }}>
          <RadioGroupDemo label="Vertical Layout" direction="vertical" />
          <RadioGroupDemo label="Horizontal Layout" direction="horizontal" />
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>States</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '16px' }}>
          <RadioGroupDemo label="With Selection (Option A selected)" direction="vertical" />
          <RadioGroupDemo label="With Disabled Options" direction="vertical" disabledOptions={['Option B']} />
          <RadioGroupDemo label="Error State" direction="vertical" error />
        </div>
      </section>
    </div>
  );
}
