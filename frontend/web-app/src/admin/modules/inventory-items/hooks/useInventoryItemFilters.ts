import { useCallback } from 'react';
import { useFilters } from '../../../hooks';
import type { InventoryItemFilterState } from '../types';
import { DEFAULT_INVENTORY_ITEM_FILTER_STATE } from '../constants';

export function useInventoryItemFilters(initialState?: Partial<InventoryItemFilterState>) {
  const { filters, setFilter, hasActiveFilters, setFilters } = useFilters<InventoryItemFilterState>({ ...DEFAULT_INVENTORY_ITEM_FILTER_STATE, ...initialState });

  const clearFilters = useCallback(() => setFilters(DEFAULT_INVENTORY_ITEM_FILTER_STATE), [setFilters]);

  return { filters, setFilter, clearFilters, hasActiveFilters };
}
