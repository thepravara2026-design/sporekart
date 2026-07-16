import { useCallback, useMemo, useState } from 'react';
import { MOCK_COURSES, type Course } from '../../courses/data/courseMockData';
import {
  DEFAULT_ANALYTICS_FILTERS,
  type AnalyticsFilters,
} from '../data/analyticsOptions';
import { computeExecutiveKpis } from '../data/analyticsMockData';

export interface UseAnalyticsState {
  filters: AnalyticsFilters;
  setFilter: <K extends keyof AnalyticsFilters>(key: K, value: AnalyticsFilters[K]) => void;
  resetFilters: () => void;
  activeFilterCount: number;
  filteredCourses: Course[];
  kpis: ReturnType<typeof computeExecutiveKpis>;
}

export function useAnalyticsState(): UseAnalyticsState {
  const [filters, setFilters] = useState<AnalyticsFilters>(DEFAULT_ANALYTICS_FILTERS);

  const setFilter = useCallback(
    <K extends keyof AnalyticsFilters>(key: K, value: AnalyticsFilters[K]) => {
      setFilters((prev) => ({ ...prev, [key]: value }));
    },
    [],
  );

  const resetFilters = useCallback(() => setFilters(DEFAULT_ANALYTICS_FILTERS), []);

  const filteredCourses = useMemo(() => {
    const q = filters.search.trim().toLowerCase();
    return MOCK_COURSES.filter((c) => {
      if (q && !`${c.name} ${c.code} ${c.shortDescription}`.toLowerCase().includes(q)) return false;
      if (filters.category && c.category !== filters.category) return false;
      if (filters.level && c.level !== filters.level) return false;
      if (filters.language && c.language !== filters.language) return false;
      if (filters.deliveryMode && c.deliveryMode !== filters.deliveryMode) return false;
      return true;
    });
  }, [filters]);

  const activeFilterCount = useMemo(() => {
    let n = 0;
    if (filters.search.trim()) n++;
    if (filters.category) n++;
    if (filters.level) n++;
    if (filters.language) n++;
    if (filters.deliveryMode) n++;
    if (filters.trainingType) n++;
    if (filters.trainer) n++;
    if (filters.student) n++;
    if (filters.dateRange !== DEFAULT_ANALYTICS_FILTERS.dateRange) n++;
    return n;
  }, [filters]);

  const kpis = useMemo(() => computeExecutiveKpis(filteredCourses), [filteredCourses]);

  return { filters, setFilter, resetFilters, activeFilterCount, filteredCourses, kpis };
}
