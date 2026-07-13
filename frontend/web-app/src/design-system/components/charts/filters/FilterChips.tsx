import React from 'react';

export interface FilterChipItem {
  id: string;
  label: string;
  onRemove: () => void;
}

export interface FilterChipsProps {
  filters: FilterChipItem[];
  className?: string;
  style?: React.CSSProperties;
}

export const FilterChips: React.FC<FilterChipsProps> = ({
  filters,
  className = '',
  style,
}) => {
  if (filters.length === 0) return null;

  return (
    <div
      className={`sk-filter-chips ${className}`}
      style={{
        display: 'flex',
        flexWrap: 'wrap',
        gap: 'var(--space-inline-sm)',
        alignItems: 'center',
        ...style,
      }}
      role="list"
      aria-label="Active filters"
    >
      {filters.map((filter) => (
        <span
          key={filter.id}
          role="listitem"
          className="sk-filter-chips__chip"
          style={{
            display: 'inline-flex',
            alignItems: 'center',
            gap: 'var(--space-inline-xs)',
            borderRadius: 'var(--radius-tag)',
            border: 'var(--border-width-thin) solid var(--color-bg-primary-weak)',
            background: 'var(--color-bg-primary-weak)',
            color: 'var(--color-bg-primary-default)',
            fontWeight: 'var(--weight-medium)',
            fontSize: 'var(--text-body-sm)',
            height: 28,
            padding: '0 var(--space-2)',
            userSelect: 'none',
          }}
        >
          <span className="sk-filter-chips__label">{filter.label}</span>
          <button
            className="sk-filter-chips__remove"
            style={{
              display: 'inline-flex',
              alignItems: 'center',
              justifyContent: 'center',
              width: 16,
              height: 16,
              borderRadius: 'var(--radius-full)',
              border: 'none',
              background: 'transparent',
              color: 'var(--color-text-disabled)',
              cursor: 'pointer',
              fontSize: 12,
              lineHeight: 1,
              padding: 0,
            }}
            onClick={(e) => {
              e.stopPropagation();
              filter.onRemove();
            }}
            aria-label={`Remove ${filter.label} filter`}
            type="button"
          >
            {'\u2715'}
          </button>
        </span>
      ))}
    </div>
  );
};

FilterChips.displayName = 'FilterChips';

export default FilterChips;
