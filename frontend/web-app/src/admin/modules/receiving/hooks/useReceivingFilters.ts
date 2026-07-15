import { useMemo } from 'react';
import { useFilters } from '../../../hooks';
import type { ReceivingFilterState } from '../types';
import { DEFAULT_FILTER_STATE } from '../constants';

export function useReceivingFilters() {
  const { filters, setFilter, clearFilters, setFilters } = useFilters<ReceivingFilterState>(DEFAULT_FILTER_STATE);

  const activeFilterCount = useMemo(() => {
    let count = 0;
    if (filters.warehouse.length > 0) count++;
    if (filters.receiptStatus.length > 0) count++;
    if (filters.inspectionStatus.length > 0) count++;
    if (filters.acceptanceStatus.length > 0) count++;
    if (filters.product.length > 0) count++;
    if (filters.dateRange.start || filters.dateRange.end) count++;
    return count;
  }, [filters]);

  return { filters, setFilter, setFilters, resetFilters: clearFilters, activeFilterCount };
}
