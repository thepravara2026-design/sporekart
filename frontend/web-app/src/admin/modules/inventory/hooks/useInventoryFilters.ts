import { useState, useMemo, useCallback } from 'react';
import type { InventoryItem, InventoryFilterState } from '../types';
import { filterInventoryItems } from '../utils';
import { DEFAULT_FILTER_STATE } from '../constants';

function cloneState(state: InventoryFilterState): InventoryFilterState {
  return {
    ...state,
    warehouse: [...state.warehouse],
    stockStatus: [...state.stockStatus],
    inventoryStatus: [...state.inventoryStatus],
    category: [...state.category],
    brand: [...state.brand],
    supplier: [...state.supplier],
    location: [...state.location],
    batchStatus: [...state.batchStatus],
    savedFilters: [...state.savedFilters],
  };
}

export function useInventoryFilters(items: InventoryItem[]) {
  const [state, setState] = useState<InventoryFilterState>(() => cloneState(DEFAULT_FILTER_STATE as InventoryFilterState));

  const results = useMemo(() => filterInventoryItems(items, state), [items, state]);

  const toggle = useCallback((key: keyof InventoryFilterState, value: string) => {
    setState((prev) => {
      const next = cloneState(prev);
      const list = (next[key] as string[]) ?? [];
      (next as unknown as Record<string, string[]>)[key] = list.includes(value) ? list.filter((v) => v !== value) : [...list, value];
      return next;
    });
  }, []);

  const setSingle = useCallback((key: keyof InventoryFilterState, value: string) => {
    setState((prev) => {
      const next = cloneState(prev);
      (next as unknown as Record<string, string[]>)[key] = [value];
      return next;
    });
  }, []);

  const clear = useCallback(() => setState(cloneState(DEFAULT_FILTER_STATE as InventoryFilterState)), []);
  const activeCount = useMemo(
    () =>
      state.warehouse.length + state.stockStatus.length + state.inventoryStatus.length +
      state.category.length + state.brand.length + state.location.length + state.batchStatus.length,
    [state],
  );

  return { state, toggle, setSingle, clear, results, activeCount };
}

