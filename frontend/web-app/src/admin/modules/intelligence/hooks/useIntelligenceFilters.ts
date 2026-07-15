import { useMemo } from 'react';
import { useFilters } from '../../../hooks';
import type { IntelligenceFilterState } from '../types';
import { DEFAULT_FILTER_STATE } from '../constants';

export function useIntelligenceFilters() {
  const { filters, setFilter, clearFilters, setFilters } = useFilters<IntelligenceFilterState>(DEFAULT_FILTER_STATE);

  const activeFilterCount = useMemo(() => {
    let count = 0;
    if (filters.warehouse.length) count++;
    if (filters.category.length) count++;
    if (filters.status.length) count++;
    if (filters.dateRange.start || filters.dateRange.end) count++;
    return count;
  }, [filters]);

  return { filters, setFilter, setFilters, resetFilters: clearFilters, activeFilterCount };
}
