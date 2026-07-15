import { useCallback } from 'react';
import { useFilters } from '../../../hooks';
import type { StockFilterState } from '../types';
import { DEFAULT_STOCK_FILTER_STATE } from '../constants';

export function useStockFilters(initialState?: Partial<StockFilterState>) {
  const { filters, setFilter, hasActiveFilters, setFilters } = useFilters<StockFilterState>({ ...DEFAULT_STOCK_FILTER_STATE, ...initialState });

  const clearFilters = useCallback(() => setFilters(DEFAULT_STOCK_FILTER_STATE), [setFilters]);

  return { filters, setFilter, clearFilters, hasActiveFilters };
}
