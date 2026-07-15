import { useMemo } from 'react';
import { useFilters } from '../../../hooks';
import type { MovementFilterState } from '../types';
import { DEFAULT_MOVEMENT_FILTER_STATE } from '../constants';

export function useMovementFilters() {
  const { filters, setFilter, clearFilters, setFilters } = useFilters<MovementFilterState>(DEFAULT_MOVEMENT_FILTER_STATE);

  const activeFilterCount = useMemo(() => {
    let count = 0;
    if (filters.movementType.length > 0) count++;
    if (filters.warehouse.length > 0) count++;
    if (filters.status.length > 0) count++;
    if (filters.product.length > 0) count++;
    if (filters.dateRange.start || filters.dateRange.end) count++;
    return count;
  }, [filters]);

  return { filters, setFilter, setFilters, resetFilters: clearFilters, activeFilterCount };
}
