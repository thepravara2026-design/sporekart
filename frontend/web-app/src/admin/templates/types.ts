import type { DataGridColumn } from '../components/data-grid/types';
import type { KPIData } from '../dashboard/types';

export interface ModuleConfig<T = Record<string, any>> {
  id: string;
  label: string;
  description: string;
  columns: DataGridColumn<T>[];
  data: T[];
  permissionAction: 'view';
  featureKey: string;
  kpis?: KPIData[];
}
