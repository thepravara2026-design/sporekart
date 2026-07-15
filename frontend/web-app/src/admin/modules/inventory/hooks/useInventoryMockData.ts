import { useState, useEffect, useCallback, useRef } from 'react';
import type { InventoryItem, Warehouse, InventoryMetric, HealthMetric, RecentActivity } from '../types';
import { inventoryMockService } from '../services/inventoryMockService';

interface AsyncState<T> {
  data: T | null;
  loading: boolean;
  error: string | null;
  reload: () => void;
}

export function useInventoryMockData<T>(
  fetcher: () => Promise<T>,
): AsyncState<T> {
  const fetcherRef = useRef(fetcher);
  fetcherRef.current = fetcher;

  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [tick, setTick] = useState(0);

  useEffect(() => {
    let active = true;
    setLoading(true);
    setError(null);
    fetcherRef.current()
      .then((res) => { if (active) setData(res); })
      .catch(() => { if (active) setError('mock_data_missing'); })
      .finally(() => { if (active) setLoading(false); });
    return () => { active = false; };
  }, [tick]);

  const reload = useCallback(() => setTick((t) => t + 1), []);
  return { data, loading, error, reload };
}

export function useInventoryItems() {
  return useInventoryMockData<InventoryItem[]>(() => inventoryMockService.fetchInventoryItems());
}

export function useInventoryWarehouses() {
  return useInventoryMockData<Warehouse[]>(() => inventoryMockService.fetchWarehouses());
}

export function useInventoryDashboard() {
  return useInventoryMockData<{ metrics: InventoryMetric[]; health: HealthMetric[] }>(
    () => inventoryMockService.fetchDashboardMetrics(),
  );
}

export function useInventoryActivities() {
  return useInventoryMockData<RecentActivity[]>(() => inventoryMockService.fetchRecentActivities());
}
