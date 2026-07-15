export type IntelligenceSection = {
  id: string; label: string; icon: string; description: string;
};

export type MetricTrend = 'up' | 'down' | 'neutral';
export type MetricVariant = 'success' | 'warning' | 'danger' | 'info' | 'neutral';

export interface KpiMetric {
  label: string; value: string; trend: MetricTrend; variant: MetricVariant; icon?: string; subtitle?: string;
}

export interface HealthMetric {
  label: string; score: number; maxScore: number; variant: MetricVariant; trend: MetricTrend;
}

export interface ChartSeries {
  name: string; value: number; color?: string;
}

export interface ChartData {
  labels: string[]; series: ChartSeries[][]; title?: string;
}

export interface InsightData {
  id: string; type: 'alert' | 'warning' | 'info' | 'success'; title: string; description: string; timestamp: string; actionLabel?: string;
}

export interface AlertData {
  id: string; severity: 'critical' | 'high' | 'medium' | 'low'; title: string; message: string; timestamp: string; category: string; acknowledged: boolean;
}

export interface WarehouseAnalytics {
  totalWarehouses: number; totalZones: number; totalBins: number; utilizationRate: number; capacityUsed: number; capacityTotal: number; topWarehouses: { name: string; utilization: number; items: number }[];
}

export interface ProductAnalytics {
  totalProducts: number; totalVariants: number; totalCategories: number; totalBrands: number; totalSkus: number; mappingCoverage: number; topProducts: { name: string; skus: number; stock: number }[];
}

export interface StockAnalytics {
  available: number; reserved: number; incoming: number; blocked: number; damaged: number; expired: number; lowStock: number; criticalStock: number; stockHealth: number;
}

export interface BatchAnalytics {
  totalBatches: number; totalLots: number; nearExpiry: number; expired: number; qualityApproved: number; qualityRejected: number; qualityPending: number; traceabilityCoverage: number;
}

export interface MovementAnalytics {
  totalTransfers: number; totalReceipts: number; totalIssues: number; totalAdjustments: number; totalReturns: number; dailyAvg: number; monthlyTotal: number; byWarehouse: { warehouse: string; count: number }[];
}

export interface ExpiryAnalytics {
  fresh: number; healthy: number; monitor: number; nearExpiry: number; critical: number; expired: number; blocked: number; disposed: number;
}

export interface ForecastData {
  demandForecast: number[]; stockForecast: number[]; purchaseForecast: number[]; capacityForecast: number[]; expiryForecast: number[]; reorderForecast: number[]; months: string[];
}

export interface ExecutiveKpiData {
  inventoryItems: number; warehouses: number; stockRecords: number; inventoryValue: string; goodsReceipts: number; transfers: number; batchCount: number; nearExpiry: number; expiredCount: number; rejectedGoods: number; pendingInspection: number; warehouseCapacity: number;
}

export interface IntelligenceFilterState {
  dateRange: { start: string; end: string }; warehouse: string[]; category: string[]; status: string[];
}

export interface EmptyStateConfig {
  title: string; message: string; icon: string; actionLabel?: string;
}

export interface SettingsSection {
  id: string; label: string; icon: string; description: string;
}
