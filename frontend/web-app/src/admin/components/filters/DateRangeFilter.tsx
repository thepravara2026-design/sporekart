import React, { useCallback, memo } from 'react';

interface DateRangeFilterProps {
  label: string;
  value: [string, string] | null;
  onChange: (value: [string, string] | null) => void;
  onClear: () => void;
}

export const DateRangeFilter = memo(function DateRangeFilter({ label, value, onChange, onClear }: DateRangeFilterProps) {
  const from = value?.[0] ?? '';
  const to = value?.[1] ?? '';

  const handleFromChange = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      const newFrom = e.target.value;
      if (newFrom && to) onChange([newFrom, to]);
      else if (newFrom) onChange([newFrom, '']);
      else onClear();
    },
    [to, onChange, onClear]
  );

  const handleToChange = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      const newTo = e.target.value;
      if (from && newTo) onChange([from, newTo]);
      else if (newTo) onChange(['', newTo]);
      else onClear();
    },
    [from, onChange, onClear]
  );

  return (
    <div style={{ minWidth: 220 }}>
      <div style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)', marginBottom: 4, fontWeight: 500 }}>
        {label}
      </div>
      <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
        <input
          type="date"
          value={from}
          onChange={handleFromChange}
          aria-label={`${label} from`}
          style={{
            padding: '5px 8px',
            border: '1px solid var(--color-border)',
            borderRadius: 'var(--radius-md)',
            background: 'var(--color-surface)',
            color: 'var(--color-text-primary)',
            fontSize: 'var(--text-body)',
            outline: 'none',
            flex: 1,
          }}
        />
        <span style={{ color: 'var(--color-text-tertiary)' }}>&ndash;</span>
        <input
          type="date"
          value={to}
          onChange={handleToChange}
          aria-label={`${label} to`}
          style={{
            padding: '5px 8px',
            border: '1px solid var(--color-border)',
            borderRadius: 'var(--radius-md)',
            background: 'var(--color-surface)',
            color: 'var(--color-text-primary)',
            fontSize: 'var(--text-body)',
            outline: 'none',
            flex: 1,
          }}
        />
      </div>
    </div>
  );
});
