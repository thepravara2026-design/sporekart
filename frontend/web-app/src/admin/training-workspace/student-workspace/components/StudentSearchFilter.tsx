import { memo, useCallback, useState } from 'react';
import { Icon } from '../../../../design-system/icons/Icon';
import { Chip } from '../../../../design-system/components/display/Chip';
import type { StudentFilters, StudentStatus, LearningMode } from '../types';
import { STUDENT_STATUS_LABELS } from '../types';

interface StudentSearchFilterProps {
  searchQuery: string;
  onSearchChange: (query: string) => void;
  filters: StudentFilters;
  onFilterChange: <K extends keyof StudentFilters>(key: K, value: StudentFilters[K]) => void;
  onClearFilters: () => void;
  availableCourses: string[];
  availableLanguages: string[];
  availableCategories: string[];
  loading?: boolean;
}

const STATUS_OPTIONS: StudentStatus[] = [
  'prospective', 'applied', 'pending-approval', 'approved', 'enrolled',
  'active', 'inactive', 'completed', 'certified', 'alumni', 'archived',
];

const MODE_OPTIONS: LearningMode[] = ['online', 'offline', 'hybrid'];

export const StudentSearchBar = memo(function StudentSearchBar({
  searchQuery,
  onSearchChange,
  loading = false,
}: {
  searchQuery: string;
  onSearchChange: (query: string) => void;
  loading?: boolean;
}) {
  const handleChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    onSearchChange(e.target.value);
  }, [onSearchChange]);

  const handleClear = useCallback(() => {
    onSearchChange('');
  }, [onSearchChange]);

  return (
    <div
      style={{
        display: 'flex', alignItems: 'center', gap: 8,
        background: 'var(--color-bg-surface-default)',
        border: '1px solid var(--color-border-default)',
        borderRadius: 'var(--radius-md)',
        padding: '0 12px',
        height: 36,
        minWidth: 240,
        maxWidth: 400,
        transition: 'border-color var(--duration-fast) var(--easing-standard)',
      }}
      role="search"
    >
      <Icon name="search" size={14} color="var(--color-text-tertiary)" />
      <input
        type="search"
        value={searchQuery}
        onChange={handleChange}
        placeholder="Search by name, ID, email, course..."
        aria-label="Search students"
        disabled={loading}
        style={{
          flex: 1, border: 'none', outline: 'none',
          background: 'transparent',
          fontSize: 'var(--text-body-sm)',
          color: 'var(--color-text-primary)',
          fontFamily: 'inherit',
        }}
      />
      {searchQuery && (
        <button
          onClick={handleClear}
          aria-label="Clear search"
          style={{
            background: 'none', border: 'none', cursor: 'pointer', padding: 4,
            color: 'var(--color-text-tertiary)',
          }}
        >
          <Icon name="x" size={14} color="currentColor" />
        </button>
      )}
    </div>
  );
});

export const StudentFilterPanel = memo(function StudentFilterPanel({
  filters,
  onFilterChange,
  onClearFilters,
  availableCourses,
  availableLanguages,
  availableCategories,
  loading = false,
}: StudentSearchFilterProps) {
  const [expanded, setExpanded] = useState(false);

  const activeFilterCount = ([
    filters.status?.length,
    filters.course?.length,
    filters.language?.length,
    filters.learningMode?.length,
    filters.category?.length,
  ] as number[]).reduce((sum, count) => sum + count, 0);

  const toggleStatus = useCallback((status: StudentStatus) => {
    const current = filters.status || [];
    const next = current.includes(status)
      ? current.filter((s) => s !== status)
      : [...current, status];
    onFilterChange('status', next.length > 0 ? next : undefined);
  }, [filters.status, onFilterChange]);

  const toggleArrayFilter = useCallback(<T extends string>(
    key: keyof StudentFilters,
    value: T,
    current?: T[]
  ) => {
    const arr = current || [];
    const next = arr.includes(value)
      ? arr.filter((v) => v !== value)
      : [...arr, value];
    onFilterChange(key as any, next.length > 0 ? next : undefined);
  }, [onFilterChange]);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: 8, flexWrap: 'wrap' }}>
        <button
          type="button"
          onClick={() => setExpanded(!expanded)}
          style={{
            display: 'inline-flex', alignItems: 'center', gap: 6,
            padding: '4px 10px', height: 28,
            background: activeFilterCount > 0
              ? 'var(--color-bg-primary-subtle)'
              : 'var(--color-bg-surface-default)',
            border: '1px solid var(--color-border-default)',
            borderRadius: 'var(--radius-md)',
            cursor: 'pointer',
            fontSize: 'var(--text-caption)',
            color: activeFilterCount > 0 ? 'var(--color-primary)' : 'var(--color-text-secondary)',
            fontWeight: activeFilterCount > 0 ? 'var(--weight-semibold)' : 'var(--weight-medium)',
          }}
          aria-expanded={expanded}
          aria-label="Toggle filters"
        >
          <Icon name="filter" size={12} color="currentColor" />
          <span>Filters</span>
          {activeFilterCount > 0 && (
            <span style={{
              display: 'inline-flex', alignItems: 'center', justifyContent: 'center',
              width: 16, height: 16, borderRadius: 'var(--radius-full)',
              background: 'var(--color-primary)',
              color: '#fff', fontSize: 10, fontWeight: 'var(--weight-bold)',
            }}>
              {activeFilterCount}
            </span>
          )}
        </button>

        {activeFilterCount > 0 && (
          <button
            type="button"
            onClick={onClearFilters}
            style={{
              display: 'inline-flex', alignItems: 'center', gap: 4,
              padding: '4px 8px', height: 28,
              background: 'none', border: 'none',
              cursor: 'pointer', fontSize: 'var(--text-caption)',
              color: 'var(--color-text-tertiary)',
            }}
            aria-label="Clear all filters"
          >
            <Icon name="x" size={12} color="currentColor" />
            <span>Clear all</span>
          </button>
        )}

        <div style={{ flex: 1 }} />

        <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-tertiary)' }}>
          {loading ? 'Loading...' : ''}
        </span>
      </div>

      {expanded && (
        <div
          style={{
            display: 'flex', flexDirection: 'column', gap: 12,
            padding: 12,
            background: 'var(--color-bg-surface-default)',
            border: '1px solid var(--color-border-default)',
            borderRadius: 'var(--radius-md)',
          }}
          role="region"
          aria-label="Filter options"
        >
          <div>
            <div style={{ fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-tertiary)', marginBottom: 6 }}>
              Status
            </div>
            <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
              {STATUS_OPTIONS.map((status) => (
                <Chip
                  key={status}
                  variant="default"
                  size="sm"
                  type="filter"
                  selected={filters.status?.includes(status) || false}
                  onSelect={() => toggleStatus(status)}
                >
                  {STUDENT_STATUS_LABELS[status]}
                </Chip>
              ))}
            </div>
          </div>

          <div>
            <div style={{ fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-tertiary)', marginBottom: 6 }}>
              Learning Mode
            </div>
            <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
              {MODE_OPTIONS.map((mode) => (
                <Chip
                  key={mode}
                  variant="default"
                  size="sm"
                  type="filter"
                  selected={filters.learningMode?.includes(mode) || false}
                  onSelect={() => toggleArrayFilter('learningMode', mode, filters.learningMode)}
                >
                  {mode.charAt(0).toUpperCase() + mode.slice(1)}
                </Chip>
              ))}
            </div>
          </div>

          <div>
            <div style={{ fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-tertiary)', marginBottom: 6 }}>
              Course
            </div>
            <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
              {availableCourses.slice(0, 10).map((course) => (
                <Chip
                  key={course}
                  variant="default"
                  size="sm"
                  type="filter"
                  selected={filters.course?.includes(course) || false}
                  onSelect={() => toggleArrayFilter('course', course, filters.course)}
                >
                  {course}
                </Chip>
              ))}
            </div>
          </div>

          <div>
            <div style={{ fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-tertiary)', marginBottom: 6 }}>
              Language
            </div>
            <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
              {availableLanguages.map((lang) => (
                <Chip
                  key={lang}
                  variant="default"
                  size="sm"
                  type="filter"
                  selected={filters.language?.includes(lang) || false}
                  onSelect={() => toggleArrayFilter('language', lang, filters.language)}
                >
                  {lang}
                </Chip>
              ))}
            </div>
          </div>

          <div>
            <div style={{ fontSize: 'var(--text-caption)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-tertiary)', marginBottom: 6 }}>
              Category
            </div>
            <div style={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
              {availableCategories.map((cat) => (
                <Chip
                  key={cat}
                  variant="default"
                  size="sm"
                  type="filter"
                  selected={filters.category?.includes(cat) || false}
                  onSelect={() => toggleArrayFilter('category', cat, filters.category)}
                >
                  {cat}
                </Chip>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
});
