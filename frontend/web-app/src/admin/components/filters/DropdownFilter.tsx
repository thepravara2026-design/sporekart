import { useState, useRef, useEffect, memo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';

interface DropdownFilterProps {
  label: string;
  value: string | null;
  options: { label: string; value: string }[];
  onChange: (value: string | null) => void;
  onClear: () => void;
}

export const DropdownFilter = memo(function DropdownFilter({ label, value, options, onChange, onClear }: DropdownFilterProps) {
  const [open, setOpen] = useState(false);
  const ref = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (ref.current && !ref.current.contains(e.target as Node)) setOpen(false);
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const selected = options.find((o) => o.value === value);

  return (
    <div ref={ref} style={{ position: 'relative', minWidth: 160 }}>
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
          border: `1px solid ${value ? 'var(--color-primary)' : 'var(--color-border)'}`,
          borderRadius: 'var(--radius-md)',
          background: 'var(--color-surface)',
          color: 'var(--color-text-primary)',
          fontSize: 'var(--text-body)',
          cursor: 'pointer',
        }}
      >
        <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
          <span style={{ color: 'var(--color-text-tertiary)', fontSize: 'var(--text-caption)' }}>{label}:</span>
          <span>{selected ? selected.label : 'All'}</span>
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
          }}
        >
          <button
            role="option"
            aria-selected={value === null}
            onClick={() => { onChange(null); onClear(); setOpen(false); }}
            style={{
              width: '100%',
              padding: '6px 10px',
              border: 'none',
              background: value === null ? 'var(--color-surface-hover)' : 'transparent',
              cursor: 'pointer',
              textAlign: 'left',
              color: 'var(--color-text-secondary)',
              fontSize: 'var(--text-body)',
            }}
          >
            All
          </button>
          {options.map((opt) => (
            <button
              key={opt.value}
              role="option"
              aria-selected={value === opt.value}
              onClick={() => { onChange(opt.value); setOpen(false); }}
              style={{
                width: '100%',
                padding: '6px 10px',
                border: 'none',
                background: value === opt.value ? 'var(--color-primary-alpha)' : 'transparent',
                cursor: 'pointer',
                textAlign: 'left',
                color: 'var(--color-text-primary)',
                fontSize: 'var(--text-body)',
              }}
            >
              {opt.label}
            </button>
          ))}
        </div>
      )}
    </div>
  );
});
