import { useCallback, useState, memo } from 'react';
import { Icon } from '../../../design-system/icons/Icon';
import { Button } from '../../../design-system/components/core/Button';
import { useDataGrid } from '../data-grid/DataGridProvider';
import { DropdownFilter } from './DropdownFilter';
import { CheckboxFilter } from './CheckboxFilter';
import { RadioFilter } from './RadioFilter';
import { DateRangeFilter } from './DateRangeFilter';
import { TagFilter } from './TagFilter';
import { MultiSelectFilter } from './MultiSelectFilter';
import { BooleanFilter } from './BooleanFilter';

export const FilterBar = memo(function FilterBar() {
  const { filters, setFilters, columnConfig, resetAll, filterable, searchable } = useDataGrid();
  const [expanded, setExpanded] = useState(false);

  if (!filterable) return null;

  const filterableColumns = columnConfig.filter((c) => c.filterable && c.visible);

  const hasActiveFilters = filters.some(
    (f) => f.value !== null && f.value !== '' && !(Array.isArray(f.value) && f.value.length === 0)
  );

  const activeFilterCount = filters.filter(
    (f) => f.value !== null && f.value !== '' && !(Array.isArray(f.value) && f.value.length === 0)
  ).length;

  const updateFilter = useCallback(
    (id: string, value: any) => {
      setFilters((prev) => prev.map((f) => (f.id === id ? { ...f, value } : f)));
    },
    [setFilters]
  );

  const clearFilter = useCallback(
    (id: string) => {
      setFilters((prev) => prev.map((f) => (f.id === id ? { ...f, value: null } : f)));
    },
    [setFilters]
  );

  if (filterableColumns.length === 0 && !searchable) return null;

  return (
    <div
      style={{
        border: '1px solid var(--color-border)',
        borderRadius: 'var(--radius-lg)',
        background: 'var(--color-surface)',
        overflow: 'hidden',
      }}
    >
      <div
        style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          padding: '8px 12px',
          gap: 8,
          flexWrap: 'wrap',
        }}
      >
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <Icon name="filter" size={16} />
          <span style={{ fontSize: 'var(--text-body)', fontWeight: 500 }}>Filters</span>
          {activeFilterCount > 0 && (
            <span
              style={{
                background: 'var(--color-primary)',
                color: '#fff',
                fontSize: 11,
                fontWeight: 600,
                borderRadius: 'var(--radius-full)',
                padding: '1px 6px',
                minWidth: 18,
                textAlign: 'center',
              }}
            >
              {activeFilterCount}
            </span>
          )}
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          {hasActiveFilters && (
            <Button variant="ghost" size="sm" onClick={resetAll}>
              Reset
            </Button>
          )}
          <Button variant="ghost" size="sm" onClick={() => setExpanded(!expanded)} aria-expanded={expanded} aria-controls="filter-panel">
            <Icon name={expanded ? 'chevron-up' : 'chevron-down'} size={14} />
          </Button>
        </div>
      </div>
      {expanded && (
        <div
          id="filter-panel"
          style={{
            padding: '12px',
            borderTop: '1px solid var(--color-border)',
            display: 'flex',
            flexWrap: 'wrap',
            gap: 12,
          }}
        >
          {filters.map((filter) => {
            const col = columnConfig.find((c) => c.key === filter.id);
            if (!col || !col.visible) return null;

            switch (filter.type) {
              case 'dropdown':
                return (
                  <DropdownFilter
                    key={filter.id}
                    label={filter.label}
                    value={filter.value as string | null}
                    options={filter.options ?? []}
                    onChange={(v) => updateFilter(filter.id, v)}
                    onClear={() => clearFilter(filter.id)}
                  />
                );
              case 'checkbox':
                return (
                  <CheckboxFilter
                    key={filter.id}
                    label={filter.label}
                    value={filter.value as string[]}
                    options={filter.options ?? []}
                    onChange={(v) => updateFilter(filter.id, v)}
                  />
                );
              case 'radio':
                return (
                  <RadioFilter
                    key={filter.id}
                    label={filter.label}
                    value={filter.value as string | null}
                    options={filter.options ?? []}
                    onChange={(v) => updateFilter(filter.id, v)}
                  />
                );
              case 'dateRange':
                return (
                  <DateRangeFilter
                    key={filter.id}
                    label={filter.label}
                    value={filter.value as [string, string] | null}
                    onChange={(v) => updateFilter(filter.id, v)}
                    onClear={() => clearFilter(filter.id)}
                  />
                );
              case 'tag':
                return (
                  <TagFilter
                    key={filter.id}
                    label={filter.label}
                    value={filter.value as string[]}
                    options={filter.options ?? []}
                    onChange={(v) => updateFilter(filter.id, v)}
                  />
                );
              case 'multiSelect':
                return (
                  <MultiSelectFilter
                    key={filter.id}
                    label={filter.label}
                    value={filter.value as string[]}
                    options={filter.options ?? []}
                    onChange={(v) => updateFilter(filter.id, v)}
                  />
                );
              case 'boolean':
                return (
                  <BooleanFilter
                    key={filter.id}
                    label={filter.label}
                    value={filter.value as boolean | null}
                    onChange={(v) => updateFilter(filter.id, v)}
                  />
                );
              default:
                return null;
            }
          })}
        </div>
      )}
    </div>
  );
});
