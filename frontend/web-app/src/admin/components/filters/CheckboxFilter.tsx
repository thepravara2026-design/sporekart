import { useCallback, memo } from 'react';

interface CheckboxFilterProps {
  label: string;
  value: string[];
  options: { label: string; value: string }[];
  onChange: (value: string[]) => void;
}

export const CheckboxFilter = memo(function CheckboxFilter({ label, value = [], options, onChange }: CheckboxFilterProps) {
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
    <div style={{ minWidth: 160 }}>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginBottom: 4, fontWeight: 500 }}>
        {label}
      </div>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
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
              type="checkbox"
              checked={value.includes(opt.value)}
              onChange={() => toggle(opt.value)}
              style={{ accentColor: 'var(--color-primary)' }}
            />
            {opt.label}
          </label>
        ))}
      </div>
      {value.length > 0 && (
        <button
          onClick={() => onChange([])}
          style={{
            marginTop: 4,
            padding: '2px 6px',
            border: 'none',
            background: 'none',
            cursor: 'pointer',
            color: 'var(--color-primary)',
            fontSize: 'var(--text-caption)',
          }}
        >
          Clear
        </button>
      )}
    </div>
  );
});
