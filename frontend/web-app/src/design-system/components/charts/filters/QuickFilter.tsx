import React, { useCallback } from 'react';

export interface QuickFilterOption {
  label: string;
  value: string;
}

export interface QuickFilterProps {
  options: QuickFilterOption[];
  selected?: string[];
  onChange: (values: string[]) => void;
  multi?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

export const QuickFilter: React.FC<QuickFilterProps> = ({
  options,
  selected = [],
  onChange,
  multi = false,
  className = '',
  style,
}) => {
  const handleToggle = useCallback(
    (value: string) => {
      if (multi) {
        const exists = selected.includes(value);
        const next = exists
          ? selected.filter((v) => v !== value)
          : [...selected, value];
        onChange(next);
      } else {
        const next = selected.includes(value) ? [] : [value];
        onChange(next);
      }
    },
    [multi, selected, onChange]
  );

  const containerStyle: React.CSSProperties = {
    display: 'flex',
    flexWrap: 'wrap',
    gap: 'var(--space-inline-sm)',
    ...style,
  };

  return (
    <div
      className={`sk-quick-filter ${className}`}
      style={containerStyle}
      role={multi ? 'group' : 'radiogroup'}
      aria-label="Quick filters"
    >
      {options.map((opt) => {
        const isSelected = selected.includes(opt.value);
        const chipStyle: React.CSSProperties = {
          display: 'inline-flex',
          alignItems: 'center',
          gap: 'var(--space-inline-xs)',
          borderRadius: 'var(--radius-tag)',
          border: 'var(--border-width-thin) solid',
          borderColor: isSelected ? 'var(--color-bg-primary-default)' : 'var(--color-border-default)',
          background: isSelected ? 'var(--color-bg-primary-weak)' : 'var(--color-bg-surface-default)',
          color: isSelected ? 'var(--color-bg-primary-default)' : 'var(--color-text-primary)',
          fontWeight: isSelected ? 'var(--weight-semibold)' : 'var(--weight-medium)',
          fontSize: 'var(--text-body-sm)',
          height: 28,
          padding: '0 var(--space-2)',
          cursor: 'pointer',
          userSelect: 'none',
          transition: 'all var(--duration-fast) var(--easing-standard)',
          fontFamily: 'var(--font-family-sans)',
        };

        return (
          <button
            key={opt.value}
            type="button"
            role={multi ? 'checkbox' : 'radio'}
            aria-checked={multi ? isSelected : isSelected}
            aria-label={opt.label}
            style={chipStyle}
            onClick={() => handleToggle(opt.value)}
          >
            {opt.label}
          </button>
        );
      })}
    </div>
  );
};

QuickFilter.displayName = 'QuickFilter';

export default QuickFilter;
