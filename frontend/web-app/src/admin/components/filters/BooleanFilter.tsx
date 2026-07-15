import { memo } from 'react';

interface BooleanFilterProps {
  label: string;
  value: boolean | null;
  onChange: (value: boolean | null) => void;
}

export const BooleanFilter = memo(function BooleanFilter({ label, value, onChange }: BooleanFilterProps) {
  return (
    <div style={{ minWidth: 160 }}>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginBottom: 4, fontWeight: 500 }}>
        {label}
      </div>
      <div role="radiogroup" style={{ display: 'flex', gap: 4 }}>
        {[
          { l: 'All', v: null },
          { l: 'Yes', v: true },
          { l: 'No', v: false },
        ].map((opt) => (
          <button
            key={String(opt.v)}
            role="radio"
            aria-checked={value === opt.v}
            onClick={() => onChange(opt.v)}
            style={{
              padding: '4px 12px',
              border: `1px solid ${value === opt.v ? 'var(--color-primary)' : 'var(--color-border)'}`,
              borderRadius: 'var(--radius-md)',
              background: value === opt.v ? 'var(--color-primary-alpha)' : 'var(--color-surface)',
              color: value === opt.v ? 'var(--color-primary)' : 'var(--color-text-secondary)',
              fontSize: 'var(--text-body)',
              cursor: 'pointer',
              fontWeight: value === opt.v ? 500 : 400,
            }}
          >
            {opt.l}
          </button>
        ))}
      </div>
    </div>
  );
});
