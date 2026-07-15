import { useMemo } from 'react';
import { useFilters } from '../../../hooks';
import type { BatchFilterState } from '../types';
import { DEFAULT_BATCH_FILTER_STATE } from '../constants';

export function useBatchFilters() {
  const { filters, setFilter, clearFilters, setFilters } = useFilters<BatchFilterState>(DEFAULT_BATCH_FILTER_STATE);

  const activeFilterCount = useMemo(() => {
    let count = 0;
    if (filters.warehouse.length > 0) count++;
    if (filters.product.length > 0) count++;
    if (filters.status.length > 0) count++;
    if (filters.qualityStatus.length > 0) count++;
    if (filters.expiryStatus.length > 0) count++;
    if (filters.dateRange.start || filters.dateRange.end) count++;
    if (filters.shelfLife.min > 0 || filters.shelfLife.max > 0) count++;
    return count;
  }, [filters]);

  return { filters, setFilter, setFilters, resetFilters: clearFilters, activeFilterCount };
}
