import { useState } from 'react';

const CheckIcon = () => (
  <svg width="12" height="12" viewBox="0 0 12 12" fill="none">
    <polyline points="2.5 6 5 8.5 9.5 3" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
);

const MinusIcon = () => (
  <svg width="12" height="12" viewBox="0 0 12 12" fill="none">
    <line x1="3" y1="6" x2="9" y2="6" stroke="currentColor" strokeWidth="2" strokeLinecap="round"/>
  </svg>
);

function CheckboxDemo({
  label,
  checked: initialChecked,
  indeterminate: initialIndeterminate,
  disabled,
  error,
  success,
  helperText,
}: {
  label: string;
  checked?: boolean;
  indeterminate?: boolean;
  disabled?: boolean;
  error?: boolean;
  success?: boolean;
  helperText?: string;
}) {
  const [checked, setChecked] = useState(initialChecked ?? false);
  const [indeterminate, setIndeterminate] = useState(initialIndeterminate ?? false);

  const handleChange = () => {
    if (indeterminate) {
      setIndeterminate(false);
      setChecked(false);
    } else {
      setChecked((c) => !c);
    }
  };

  const isChecked = !indeterminate && checked;
  const borderColor = error ? 'var(--color-border-danger, #dc2626)' : success ? 'var(--color-border-success, #16a34a)' : 'var(--color-border-default)';
  const bgColor = disabled ? 'var(--color-bg-disabled, #f3f4f6)' : isChecked || indeterminate ? 'var(--color-bg-primary-default, #2563eb)' : 'transparent';

  return (
    <label style={{ display: 'flex', alignItems: 'flex-start', gap: '8px', cursor: disabled ? 'not-allowed' : 'pointer', opacity: disabled ? 0.6 : 1 }}>
      <div
        onClick={disabled ? undefined : handleChange}
        style={{
          width: 18,
          height: 18,
          flexShrink: 0,
          borderRadius: 4,
          border: `2px solid ${borderColor}`,
          background: bgColor,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          color: bgColor === 'transparent' ? 'transparent' : '#fff',
          transition: 'all 0.15s',
          marginTop: 2,
        }}
      >
        {indeterminate ? <MinusIcon /> : isChecked ? <CheckIcon /> : null}
      </div>
      <div style={{ display: 'flex', flexDirection: 'column', gap: '2px' }}>
        <span style={{ fontSize: 'var(--text-base)', color: 'var(--color-text-primary)' }}>{label}</span>
        {helperText && <span style={{ fontSize: 'var(--text-caption)', color: error ? 'var(--color-text-danger, #dc2626)' : success ? 'var(--color-text-success, #16a34a)' : 'var(--color-text-secondary)' }}>{helperText}</span>}
      </div>
    </label>
  );
}

function CheckboxGroupDemo({ direction }: { direction: 'horizontal' | 'vertical' }) {
  const options = ['Option A', 'Option B', 'Option C'];
  const [selected, setSelected] = useState<Set<string>>(new Set(['Option A']));

  const toggle = (opt: string) => {
    setSelected((prev) => {
      const next = new Set(prev);
      if (next.has(opt)) next.delete(opt);
      else next.add(opt);
      return next;
    });
  };

  return (
    <div style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)', display: 'flex', flexDirection: 'column', gap: '8px' }}>
      <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Checkbox Group - {direction}</span>
      <div style={{ display: 'flex', flexDirection: direction === 'horizontal' ? 'row' : 'column', gap: '12px', flexWrap: 'wrap' }}>
        {options.map((opt) => {
          const isChecked = selected.has(opt);
          return (
            <label key={opt} style={{ display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer' }}>
              <div
                onClick={() => toggle(opt)}
                style={{
                  width: 18,
                  height: 18,
                  flexShrink: 0,
                  borderRadius: 4,
                  border: `2px solid ${isChecked ? 'var(--color-bg-primary-default, #2563eb)' : 'var(--color-border-default)'}`,
                  background: isChecked ? 'var(--color-bg-primary-default, #2563eb)' : 'transparent',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  color: '#fff',
                  transition: 'all 0.15s',
                }}
              >
                {isChecked && <CheckIcon />}
              </div>
              <span style={{ fontSize: 'var(--text-base)', color: 'var(--color-text-primary)' }}>{opt}</span>
            </label>
          );
        })}
      </div>
    </div>
  );
}

export default function CheckboxPreview() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-section-gap)', padding: 'var(--space-page-y) var(--space-page-x)' }}>
      <div>
        <h1 style={{ fontSize: 'var(--text-h1)', fontWeight: 'var(--weight-bold)', margin: 0 }}>Checkbox</h1>
        <p style={{ color: 'var(--color-text-secondary)', margin: '4px 0 0' }}>Checkbox and CheckboxGroup with all states</p>
      </div>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Checkbox States</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: '16px' }}>
          <div style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)', display: 'flex', flexDirection: 'column', gap: '12px' }}>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Unchecked</span>
            <CheckboxDemo label="Unchecked" />
          </div>
          <div style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)', display: 'flex', flexDirection: 'column', gap: '12px' }}>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Checked</span>
            <CheckboxDemo label="Checked" checked />
          </div>
          <div style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)', display: 'flex', flexDirection: 'column', gap: '12px' }}>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Indeterminate</span>
            <CheckboxDemo label="Indeterminate" indeterminate />
          </div>
          <div style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)', display: 'flex', flexDirection: 'column', gap: '12px' }}>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Disabled - Unchecked</span>
            <CheckboxDemo label="Disabled unchecked" disabled />
          </div>
          <div style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)', display: 'flex', flexDirection: 'column', gap: '12px' }}>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Disabled - Checked</span>
            <CheckboxDemo label="Disabled checked" checked disabled />
          </div>
          <div style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)', display: 'flex', flexDirection: 'column', gap: '12px' }}>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Disabled - Indeterminate</span>
            <CheckboxDemo label="Disabled indeterminate" indeterminate disabled />
          </div>
          <div style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)', display: 'flex', flexDirection: 'column', gap: '12px' }}>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Error</span>
            <CheckboxDemo label="Error state" error helperText="This selection is invalid" />
          </div>
          <div style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)', display: 'flex', flexDirection: 'column', gap: '12px' }}>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Success</span>
            <CheckboxDemo label="Success state" success helperText="Selection saved" />
          </div>
          <div style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-card)', padding: '16px', boxShadow: 'var(--shadow-1)', display: 'flex', flexDirection: 'column', gap: '12px' }}>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>With Helper Text</span>
            <CheckboxDemo label="With helper" helperText="This is helper text for the checkbox" />
          </div>
        </div>
      </section>

      <section style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <h2 style={{ fontSize: 'var(--text-h2)', fontWeight: 'var(--weight-semibold)', margin: 0 }}>Checkbox Group</h2>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '16px' }}>
          <CheckboxGroupDemo direction="vertical" />
          <CheckboxGroupDemo direction="horizontal" />
        </div>
      </section>
    </div>
  );
}
