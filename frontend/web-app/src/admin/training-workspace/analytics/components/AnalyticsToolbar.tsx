import { memo } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import {
  ANALYTICS_CATEGORY_OPTIONS,
  ANALYTICS_DATE_RANGE_OPTIONS,
  ANALYTICS_DELIVERY_OPTIONS,
  ANALYTICS_LANGUAGE_OPTIONS,
  ANALYTICS_LEVEL_OPTIONS,
  ANALYTICS_TRAINER_OPTIONS,
  ANALYTICS_TRAINING_TYPE_OPTIONS,
  type AnalyticsFilters,
} from '../data/analyticsOptions';
import ExportMenu from './ExportMenu';

export interface AnalyticsToolbarProps {
  filters: AnalyticsFilters;
  onFilterChange: <K extends keyof AnalyticsFilters>(key: K, value: AnalyticsFilters[K]) => void;
  onClearFilters: () => void;
  activeFilterCount: number;
  showCourseFilters?: boolean;
  showTrainerFilter?: boolean;
}

function FilterSelect<T extends string>({
  label,
  value,
  options,
  onChange,
}: {
  label: string;
  value: T;
  options: { value: T; label: string }[];
  onChange: (value: T) => void;
}) {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
      <label style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>{label}</label>
      <select
        value={value}
        onChange={(e) => onChange(e.target.value as T)}
        aria-label={label}
        style={{
          padding: 'var(--space-1) var(--space-2)',
          fontSize: 'var(--text-body-sm)',
          border: '1px solid var(--color-border-default)',
          borderRadius: 'var(--radius-md)',
          background: 'var(--color-bg-surface-default)',
          color: 'var(--color-text-primary)',
          minWidth: 140,
        }}
      >
        {options.map((o) => (
          <option key={o.value} value={o.value}>
            {o.label}
          </option>
        ))}
      </select>
    </div>
  );
}

const AnalyticsToolbar = memo(function AnalyticsToolbar({
  filters,
  onFilterChange,
  onClearFilters,
  activeFilterCount,
  showCourseFilters = true,
  showTrainerFilter = false,
}: AnalyticsToolbarProps) {
  return (
    <div
      role="search"
      style={{
        display: 'flex',
        flexWrap: 'wrap',
        alignItems: 'flex-end',
        gap: 'var(--space-3)',
        padding: 'var(--space-3)',
        background: 'var(--color-bg-surface-default)',
        border: '1px solid var(--color-border-default)',
        borderRadius: 'var(--radius-lg)',
      }}
    >
      <div style={{ display: 'flex', flexDirection: 'column', gap: 2, flex: '1 1 220px', minWidth: 200 }}>
        <label htmlFor="analytics-search" style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-muted)' }}>
          Search
        </label>
        <div style={{ position: 'relative' }}>
          <span style={{ position: 'absolute', left: 8, top: '50%', transform: 'translateY(-50%)', pointerEvents: 'none', color: 'var(--color-text-muted)' }}>
            <Icon name="search" size={16} />
          </span>
          <input
            id="analytics-search"
            type="search"
            value={filters.search}
            onChange={(e) => onFilterChange('search', e.target.value)}
            placeholder="Search courses, metrics..."
            style={{
              width: '100%',
              padding: 'var(--space-1) var(--space-2) var(--space-1) 30px',
              fontSize: 'var(--text-body-sm)',
              border: '1px solid var(--color-border-default)',
              borderRadius: 'var(--radius-md)',
              background: 'var(--color-bg-surface-default)',
              color: 'var(--color-text-primary)',
            }}
          />
        </div>
      </div>

      <FilterSelect
        label="Date Range"
        value={filters.dateRange}
        options={ANALYTICS_DATE_RANGE_OPTIONS}
        onChange={(v) => onFilterChange('dateRange', v)}
      />

      {showCourseFilters && (
        <>
          <FilterSelect
            label="Category"
            value={filters.category}
            options={ANALYTICS_CATEGORY_OPTIONS}
            onChange={(v) => onFilterChange('category', v)}
          />
          <FilterSelect
            label="Level"
            value={filters.level}
            options={ANALYTICS_LEVEL_OPTIONS}
            onChange={(v) => onFilterChange('level', v)}
          />
          <FilterSelect
            label="Delivery"
            value={filters.deliveryMode}
            options={ANALYTICS_DELIVERY_OPTIONS}
            onChange={(v) => onFilterChange('deliveryMode', v)}
          />
          <FilterSelect
            label="Language"
            value={filters.language}
            options={ANALYTICS_LANGUAGE_OPTIONS}
            onChange={(v) => onFilterChange('language', v)}
          />
          <FilterSelect
            label="Training Type"
            value={filters.trainingType}
            options={ANALYTICS_TRAINING_TYPE_OPTIONS}
            onChange={(v) => onFilterChange('trainingType', v)}
          />
        </>
      )}

      {showTrainerFilter && (
        <FilterSelect
          label="Trainer"
          value={filters.trainer}
          options={ANALYTICS_TRAINER_OPTIONS}
          onChange={(v) => onFilterChange('trainer', v)}
        />
      )}

      <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--space-2)', marginLeft: 'auto' }}>
        {activeFilterCount > 0 && (
          <button
            type="button"
            onClick={onClearFilters}
            style={{
              display: 'inline-flex',
              alignItems: 'center',
              gap: 4,
              padding: 'var(--space-1) var(--space-2)',
              fontSize: 'var(--text-body-sm)',
              background: 'transparent',
              border: '1px solid var(--color-border-default)',
              borderRadius: 'var(--radius-md)',
              color: 'var(--color-text-secondary)',
              cursor: 'pointer',
            }}
          >
            <Icon name="x" size={14} />
            Clear ({activeFilterCount})
          </button>
        )}
        <ExportMenu />
      </div>
    </div>
  );
});

export default AnalyticsToolbar;
