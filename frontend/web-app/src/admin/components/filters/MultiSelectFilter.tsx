import { useState, useRef, useEffect, useCallback, memo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';

interface MultiSelectFilterProps {
  label: string;
  value: string[];
  options: { label: string; value: string }[];
  onChange: (value: string[]) => void;
}

export const MultiSelectFilter = memo(function MultiSelectFilter({ label, value = [], options, onChange }: MultiSelectFilterProps) {
  const [open, setOpen] = useState(false);
  const ref = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (ref.current && !ref.current.contains(e.target as Node)) setOpen(false);
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

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
    <div ref={ref} style={{ position: 'relative', minWidth: 180 }}>
      <button
        onClick={() => setOpen(!open)}
        aria-haspopup="listbox"
        aria-expanded={open}
        style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          gap: 8,
          width: '100%',
          padding: '6px 10px',
          border: `1px solid ${value.length > 0 ? 'var(--color-primary)' : 'var(--color-border)'}`,
          borderRadius: 'var(--radius-md)',
          background: 'var(--color-surface)',
          color: 'var(--color-text-primary)',
          fontSize: 'var(--text-body)',
          cursor: 'pointer',
        }}
      >
        <span style={{ display: 'flex', alignItems: 'center', gap: 4, overflow: 'hidden' }}>
          <span style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>{label}:</span>
          <span style={{ overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
            {value.length > 0 ? `${value.length} selected` : 'All'}
          </span>
        </span>
        <Icon name="chevron-down" size={14} />
      </button>
      {open && (
        <div
          role="listbox"
          aria-label={label}
          style={{
            position: 'absolute',
            top: '100%',
            left: 0,
            right: 0,
            marginTop: 2,
            background: 'var(--color-surface)',
            border: '1px solid var(--color-border)',
            borderRadius: 'var(--radius-md)',
            boxShadow: 'var(--elevation-md)',
            zIndex: 100,
            maxHeight: 240,
            overflowY: 'auto',
            padding: 4,
          }}
        >
          {options.map((opt) => (
            <div
              key={opt.value}
              role="option"
              aria-selected={value.includes(opt.value)}
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: 6,
                padding: '5px 8px',
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
            </div>
          ))}
        </div>
      )}
    </div>
  );
});
