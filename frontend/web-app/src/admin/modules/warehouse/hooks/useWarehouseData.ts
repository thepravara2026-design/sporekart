import { useInventoryMockData } from '../../inventory/hooks';
import type { Warehouse, Zone, StorageNode, RecentActivity, WarehouseMetric, HealthMetric, StatusCount } from '../types';
import { warehouseMockService } from '../services/warehouseMockService';

export function useWarehouses() {
  return useInventoryMockData<Warehouse[]>(() => warehouseMockService.fetchWarehouses());
}

export function useWarehouseDashboard() {
  return useInventoryMockData<{ metrics: WarehouseMetric[]; health: HealthMetric[]; statusCounts: StatusCount[] }>(
    () => warehouseMockService.fetchWarehouseDashboard(),
  );
}

export function useWarehouseActivities() {
  return useInventoryMockData<RecentActivity[]>(() => warehouseMockService.fetchRecentActivities());
}

export function useWarehouseZones() {
  return useInventoryMockData<Zone[]>(() => warehouseMockService.fetchZones());
}

export function useWarehouseStorageNodes() {
  return useInventoryMockData<StorageNode[]>(() => warehouseMockService.fetchStorageNodes());
}

export function useWarehouseMutations() {
  return {
    createWarehouse: warehouseMockService.createWarehouse,
    updateWarehouse: warehouseMockService.updateWarehouse,
    archiveWarehouse: warehouseMockService.archiveWarehouse,
  };
}


