import { useState, useMemo, useCallback } from 'react';

export function useFilters<T>(defaultState: T) {
  const [filters, setFilters] = useState<T>(defaultState);

  const setFilter = useCallback(<K extends keyof T>(key: K, value: T[K]) => {
    setFilters((prev) => ({ ...prev, [key]: value }));
  }, []);

  const clearFilters = useCallback(() => setFilters(defaultState), [defaultState]);

  const hasActiveFilters = useMemo(
    () => (Object.values(filters as Record<string, unknown>)).some((v) => Array.isArray(v) && v.length > 0),
    [filters],
  );

  const activeCount = useMemo(
    () => (Object.values(filters as Record<string, unknown>)).reduce<number>((sum, v) => sum + (Array.isArray(v) ? v.length : 0), 0),
    [filters],
  );

  return { filters, setFilter, clearFilters, hasActiveFilters, activeCount, setFilters };
}
