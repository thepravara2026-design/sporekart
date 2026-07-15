import { useCallback, memo } from 'react';

interface TagFilterProps {
  label: string;
  value: string[];
  options: { label: string; value: string }[];
  onChange: (value: string[]) => void;
}

export const TagFilter = memo(function TagFilter({ label, value = [], options, onChange }: TagFilterProps) {
  const toggle = useCallback(
    (optValue: string) => {
      if (value.includes(optValue)) {
        onChange(value.filter((v) => v !== optValue));
      } else {
        onChange([...value, optValue]);
      }
    },
    [value, onChange]
  );

  return (
    <div style={{ minWidth: 200 }}>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginBottom: 4, fontWeight: 500 }}>
        {label}
      </div>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: 4 }}>
        {options.map((opt) => {
          const active = value.includes(opt.value);
          return (
            <button
              key={opt.value}
              aria-pressed={active}
              onClick={() => toggle(opt.value)}
              style={{
                padding: '3px 10px',
                border: `1px solid ${active ? 'var(--color-primary)' : 'var(--color-border)'}`,
                borderRadius: 'var(--radius-full)',
                background: active ? 'var(--color-primary-alpha)' : 'var(--color-surface)',
                color: active ? 'var(--color-primary)' : 'var(--color-text-secondary)',
                fontSize: 'var(--text-caption)',
                cursor: 'pointer',
                fontWeight: active ? 500 : 400,
              }}
            >
              {opt.label}
            </button>
          );
        })}
      </div>
    </div>
  );
});
