export interface AdminDashboard {
  totalRevenue: number;
  totalOrders: number;
  totalCustomers: number;
  totalProducts: number;
  pendingOrders: number;
  lowStockItems: number;
  todaySales: number;
  growthRate: number;
}

export interface SalesSummary {
  period: string;
  totalRevenue: number;
  totalOrders: number;
  averageOrderValue: number;
  growthRate: number;
  topProducts: string[];
  revenueByCategory: Record<string, number>;
}

export interface CustomerInsight {
  totalCustomers: number;
  newCustomers: number;
  returningCustomers: number;
  churnRate: number;
  retentionRate: number;
  averageLifetimeValue: number;
  topCustomers: string[];
}

export interface InventoryInsight {
  totalProducts: number;
  totalStock: number;
  lowStockItems: Array<Record<string, unknown>>;
  outOfStockItems: Array<Record<string, unknown>>;
  fastMoving: Array<Record<string, unknown>>;
  slowMoving: Array<Record<string, unknown>>;
  inventoryValue: number;
  turnoverRate: number;
}

export interface TrainingAnalytics {
  totalCourses: number;
  activeBatches: number;
  totalEnrollments: number;
  completionRate: number;
  upcomingBatches: string[];
}

export interface BusinessInsight {
  summary: string;
  metric: string;
  currentValue: number;
  previousValue: number;
  change: number;
  trend: 'up' | 'down' | 'stable';
  severity: 'info' | 'warning' | 'critical';
  recommendation: string;
}

export interface ForecastResult {
  metric: string;
  period: string;
  forecastValues: Array<{ label: string; value: number }>;
  confidenceInterval: string;
  trend: string;
}

export interface OperationalAlert {
  id: string;
  type: 'INFO' | 'WARNING' | 'CRITICAL';
  title: string;
  description: string;
  metric: string;
  threshold: number | null;
  currentValue: number;
  timestamp: string;
  suggestedAction: string;
}

export interface PerformanceReport {
  id: string;
  title: string;
  type: string;
  generatedAt: string;
  period: string;
  metrics: Record<string, unknown>;
  downloadUrl: string;
}
