import { useState, useCallback, useMemo } from 'react';
import type { TrainingDashboardData } from '../data/mockData';
import { MOCK_DASHBOARD_DATA } from '../data/mockData';

export interface WorkspaceFilters {
  status?: string;
  trainingType?: string;
  deliveryMode?: string;
  category?: string;
  trainer?: string;
  date?: string;
  language?: string;
  difficulty?: string;
}

export interface WorkspaceState {
  data: TrainingDashboardData;
  searchQuery: string;
  filters: WorkspaceFilters;
  sidebarCollapsed: boolean;
  activeSection: string;
}

export function useWorkspaceState() {
  const [searchQuery, setSearchQuery] = useState('');
  const [filters, setFilters] = useState<WorkspaceFilters>({});
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);
  const [activeSection, setActiveSection] = useState('dashboard');

  const data = useMemo(() => MOCK_DASHBOARD_DATA, []);

  const filteredStats = useMemo(() => data.stats, [data.stats]);

  const filteredActivities = useMemo(() => {
    if (!searchQuery) return data.recentActivities;
    const q = searchQuery.toLowerCase();
    return data.recentActivities.filter(
      (a) =>
        a.message.toLowerCase().includes(q) ||
        a.user.toLowerCase().includes(q)
    );
  }, [data.recentActivities, searchQuery]);

  const filteredUpcoming = useMemo(() => {
    let items = data.upcomingTrainings;
    if (searchQuery) {
      const q = searchQuery.toLowerCase();
      items = items.filter(
        (t) =>
          t.title.toLowerCase().includes(q) ||
          t.trainer.toLowerCase().includes(q) ||
          t.batch.toLowerCase().includes(q)
      );
    }
    if (filters.status) {
      items = items.filter((t) => t.status === filters.status);
    }
    if (filters.deliveryMode) {
      items = items.filter((t) => t.mode === filters.deliveryMode);
    }
    return items;
  }, [data.upcomingTrainings, searchQuery, filters]);

  const updateFilter = useCallback((key: keyof WorkspaceFilters, value: string | undefined) => {
    setFilters((prev) => ({ ...prev, [key]: value }));
  }, []);

  const clearFilters = useCallback(() => {
    setFilters({});
  }, []);

  return {
    data,
    searchQuery,
    setSearchQuery,
    filters,
    updateFilter,
    clearFilters,
    filteredStats,
    filteredActivities,
    filteredUpcoming,
    sidebarCollapsed,
    setSidebarCollapsed,
    activeSection,
    setActiveSection,
  };
}

export type WorkspaceContextValue = ReturnType<typeof useWorkspaceState>;
