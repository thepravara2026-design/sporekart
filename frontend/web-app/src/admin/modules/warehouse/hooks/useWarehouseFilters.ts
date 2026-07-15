import { useState, useMemo, useCallback } from 'react';
import type { Warehouse, WarehouseFilterState } from '../types';
import { filterWarehouses } from '../utils';
import { DEFAULT_WAREHOUSE_FILTER_STATE } from '../constants';

function cloneState(state: WarehouseFilterState): WarehouseFilterState {
  return {
    ...state,
    type: [...state.type],
    location: [...state.location],
    status: [...state.status],
    temperatureType: [...state.temperatureType],
    zone: [...state.zone],
    capacity: [...state.capacity],
  };
}

export function useWarehouseFilters(items: Warehouse[]) {
  const [state, setState] = useState<WarehouseFilterState>(() => cloneState(DEFAULT_WAREHOUSE_FILTER_STATE));

  const results = useMemo(() => filterWarehouses(items, state), [items, state]);

  const toggle = useCallback((key: keyof WarehouseFilterState, value: string) => {
    setState((prev) => {
      const next = cloneState(prev);
      const list = (next[key] as string[]) ?? [];
      (next as unknown as Record<string, string[]>)[key as string] = list.includes(value) ? list.filter((v) => v !== value) : [...list, value];
      return next;
    });
  }, []);

  const clear = useCallback(() => setState(cloneState(DEFAULT_WAREHOUSE_FILTER_STATE)), []);
  const activeCount = useMemo(
    () => state.type.length + state.status.length + state.location.length + state.temperatureType.length + state.capacity.length,
    [state],
  );

  return { state, toggle, clear, results, activeCount };
}


