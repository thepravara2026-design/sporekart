export interface KPIData {
  id: string;
  title: string;
  value: string;
  unit?: string;
  trend: 'up' | 'down' | 'neutral';
  percentage: number;
  comparison: string;
  icon: string;
  color: string;
  loading?: boolean;
}

export interface WidgetConfig {
  id: string;
  title: string;
  type: string;
  icon: string;
  width: 1 | 2 | 3 | 4;
  height: 1 | 2;
  pinned?: boolean;
  visible: boolean;
}

export interface ActivityItemData {
  id: string;
  user: string;
  avatar?: string;
  action: string;
  target: string;
  type: 'create' | 'update' | 'delete' | 'system' | 'warning' | 'success';
  timestamp: string;
  status?: 'completed' | 'pending' | 'failed';
  details?: string;
}

export interface AnnouncementData {
  id: string;
  title: string;
  message: string;
  priority: 'low' | 'medium' | 'high' | 'critical';
  category: 'system' | 'update' | 'maintenance' | 'feature' | 'alert';
  read: boolean;
  timestamp: string;
  author?: string;
}

export interface SystemStatusData {
  id: string;
  label: string;
  status: 'operational' | 'degraded' | 'down' | 'maintenance';
  uptime: string;
  icon: string;
}

export interface QuickActionData {
  id: string;
  label: string;
  icon: string;
  path: string;
  color: string;
}

export interface DashboardLayout {
  widgets: WidgetConfig[];
  pinnedWidgetIds: string[];
  kpiOrder: string[];
}
