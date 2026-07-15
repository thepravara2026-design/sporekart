import { memo } from 'react';

interface RadioFilterProps {
  label: string;
  value: string | null;
  options: { label: string; value: string }[];
  onChange: (value: string | null) => void;
}

export const RadioFilter = memo(function RadioFilter({ label, value, options, onChange }: RadioFilterProps) {
  return (
    <div style={{ minWidth: 160 }}>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginBottom: 4, fontWeight: 500 }}>
        {label}
      </div>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
        <label
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: 6,
            padding: '3px 4px',
            borderRadius: 'var(--radius-sm)',
            cursor: 'pointer',
            fontSize: 'var(--text-body)',
            color: 'var(--color-text-secondary)',
          }}
        >
          <input
            type="radio"
            name={label}
            checked={value === null}
            onChange={() => onChange(null)}
            style={{ accentColor: 'var(--color-primary)' }}
          />
          All
        </label>
        {options.map((opt) => (
          <label
            key={opt.value}
            style={{
              display: 'flex',
              alignItems: 'center',
              gap: 6,
              padding: '3px 4px',
              borderRadius: 'var(--radius-sm)',
              cursor: 'pointer',
              fontSize: 'var(--text-body)',
              color: 'var(--color-text-primary)',
            }}
          >
            <input
              type="radio"
              name={label}
              checked={value === opt.value}
              onChange={() => onChange(opt.value)}
              style={{ accentColor: 'var(--color-primary)' }}
            />
            {opt.label}
          </label>
        ))}
      </div>
    </div>
  );
});
