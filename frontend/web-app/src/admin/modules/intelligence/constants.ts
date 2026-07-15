import type { IntelligenceSection, KpiMetric, HealthMetric, InsightData, AlertData, WarehouseAnalytics, ProductAnalytics, StockAnalytics, BatchAnalytics, MovementAnalytics, ExpiryAnalytics, ExecutiveKpiData, ForecastData, IntelligenceFilterState, EmptyStateConfig, SettingsSection } from './types';

export const INTELLIGENCE_SECTIONS: IntelligenceSection[] = [
  { id: 'overview', label: 'Overview', icon: 'layout', description: 'Executive Operations Center.' },
  { id: 'executive-kpis', label: 'Executive KPIs', icon: 'bar-chart', description: 'Executive KPI dashboard.' },
  { id: 'inventory-health', label: 'Inventory Health', icon: 'archive', description: 'Inventory health and coverage.' },
  { id: 'warehouse-health', label: 'Warehouse Health', icon: 'home', description: 'Warehouse analytics and utilization.' },
  { id: 'stock-health', label: 'Stock Health', icon: 'database', description: 'Stock analytics and health.' },
  { id: 'batch-health', label: 'Batch Health', icon: 'layers', description: 'Batch analytics and quality.' },
  { id: 'movement-analytics', label: 'Movement Analytics', icon: 'activity', description: 'Movement and transfer analytics.' },
  { id: 'receiving-analytics', label: 'Receiving Analytics', icon: 'arrow-down', description: 'Goods receipt analytics.' },
  { id: 'forecasting', label: 'Forecasting', icon: 'trending-up', description: 'Forecast workspace.' },
  { id: 'insights', label: 'Insights', icon: 'lightbulb', description: 'AI-driven insights and recommendations.' },
  { id: 'reports', label: 'Reports', icon: 'file-text', description: 'Executive reports center.' },
  { id: 'alerts', label: 'Alerts', icon: 'bell', description: 'Executive alerts and notifications.' },
  { id: 'settings', label: 'Settings', icon: 'settings', description: 'Intelligence preferences.' },
  { id: 'help', label: 'Help', icon: 'help-circle', description: 'Intelligence documentation.' },
];

export const WORKSPACE_SECTIONS = INTELLIGENCE_SECTIONS;

export const MOCK_EXECUTIVE_KPIS: ExecutiveKpiData = {
  inventoryItems: 5842, warehouses: 5, stockRecords: 12430, inventoryValue: '$8.4M', goodsReceipts: 245, transfers: 187, batchCount: 450, nearExpiry: 38, expiredCount: 12, rejectedGoods: 23, pendingInspection: 31, warehouseCapacity: 78,
};

export const MOCK_KPI_METRICS: KpiMetric[] = [
  { label: 'Inventory Items', value: '5,842', trend: 'up', variant: 'success', icon: 'package', subtitle: '+12% vs last month' },
  { label: 'Warehouses', value: '5', trend: 'neutral', variant: 'info', icon: 'home', subtitle: '3 active, 2 standby' },
  { label: 'Stock Records', value: '12,430', trend: 'up', variant: 'success', icon: 'database', subtitle: '+8% this quarter' },
  { label: 'Inventory Value', value: '$8.4M', trend: 'up', variant: 'info', icon: 'dollar-sign', subtitle: 'Estimated value' },
  { label: 'Near Expiry', value: '38', trend: 'up', variant: 'warning', icon: 'clock', subtitle: 'Within 30 days' },
  { label: 'Expired', value: '12', trend: 'down', variant: 'danger', icon: 'x-circle', subtitle: '-3 from last week' },
  { label: 'Batch Count', value: '450', trend: 'up', variant: 'success', icon: 'layers', subtitle: 'Across all products' },
  { label: 'Warehouse Capacity', value: '78%', trend: 'up', variant: 'warning', icon: 'home', subtitle: '22% remaining' },
];

export const MOCK_HEALTH_METRICS: HealthMetric[] = [
  { label: 'Inventory Health', score: 87, maxScore: 100, variant: 'success', trend: 'up' },
  { label: 'Stock Health', score: 82, maxScore: 100, variant: 'success', trend: 'up' },
  { label: 'Batch Health', score: 74, maxScore: 100, variant: 'warning', trend: 'neutral' },
  { label: 'Warehouse Health', score: 91, maxScore: 100, variant: 'success', trend: 'up' },
  { label: 'Movement Health', score: 79, maxScore: 100, variant: 'warning', trend: 'down' },
  { label: 'Receiving Health', score: 85, maxScore: 100, variant: 'success', trend: 'up' },
];

export const MOCK_WAREHOUSE_ANALYTICS: WarehouseAnalytics = {
  totalWarehouses: 5, totalZones: 22, totalBins: 480, utilizationRate: 78, capacityUsed: 12430, capacityTotal: 15936,
  topWarehouses: [
    { name: 'Main Warehouse - A', utilization: 85, items: 4520 },
    { name: 'Cold Storage - B', utilization: 72, items: 2890 },
    { name: 'Dry Storage - C', utilization: 68, items: 2150 },
    { name: 'Distribution Center - D', utilization: 91, items: 1870 },
    { name: 'Processing Facility - E', utilization: 74, items: 1000 },
  ],
};

export const MOCK_PRODUCT_ANALYTICS: ProductAnalytics = {
  totalProducts: 48, totalVariants: 96, totalCategories: 8, totalBrands: 12, totalSkus: 384, mappingCoverage: 94,
  topProducts: [
    { name: 'White Button Mushroom', skus: 8, stock: 12500 },
    { name: 'Shiitake Mushroom', skus: 6, stock: 8400 },
    { name: 'Oyster Mushroom', skus: 5, stock: 6200 },
    { name: 'Enoki Mushroom', skus: 4, stock: 4800 },
    { name: 'King Oyster Mushroom', skus: 4, stock: 3500 },
  ],
};

export const MOCK_STOCK_ANALYTICS: StockAnalytics = {
  available: 8450, reserved: 1280, incoming: 3400, blocked: 420, damaged: 180, expired: 95, lowStock: 23, criticalStock: 8, stockHealth: 82,
};

export const MOCK_BATCH_ANALYTICS: BatchAnalytics = {
  totalBatches: 450, totalLots: 1200, nearExpiry: 38, expired: 12, qualityApproved: 380, qualityRejected: 25, qualityPending: 45, traceabilityCoverage: 96,
};

export const MOCK_MOVEMENT_ANALYTICS: MovementAnalytics = {
  totalTransfers: 187, totalReceipts: 245, totalIssues: 198, totalAdjustments: 56, totalReturns: 34, dailyAvg: 18, monthlyTotal: 540,
  byWarehouse: [
    { warehouse: 'Main Warehouse - A', count: 320 },
    { warehouse: 'Cold Storage - B', count: 180 },
    { warehouse: 'Dry Storage - C', count: 140 },
    { warehouse: 'Distribution Center - D', count: 260 },
    { warehouse: 'Processing Facility - E', count: 95 },
  ],
};

export const MOCK_EXPIRY_ANALYTICS: ExpiryAnalytics = {
  fresh: 320, healthy: 85, monitor: 42, nearExpiry: 38, critical: 15, expired: 12, blocked: 8, disposed: 5,
};

export const MOCK_FORECAST: ForecastData = {
  demandForecast: [4500, 4800, 5200, 4900, 5400, 5800, 5600, 6000, 6200, 5900, 6300, 6600],
  stockForecast: [12400, 12800, 13200, 12600, 13400, 14000, 13800, 14200, 14600, 14000, 14800, 15200],
  purchaseForecast: [3000, 3200, 3500, 3400, 3600, 3800, 3700, 4000, 4200, 3900, 4100, 4400],
  capacityForecast: [78, 79, 80, 78, 81, 83, 82, 84, 85, 83, 86, 87],
  expiryForecast: [12, 15, 18, 20, 22, 25, 28, 30, 32, 35, 38, 40],
  reorderForecast: [8, 10, 12, 11, 13, 15, 14, 16, 18, 15, 17, 19],
  months: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
};

export const MOCK_INSIGHTS: InsightData[] = [
  { id: 'i1', type: 'warning', title: 'Low Stock Alert', description: 'White Button Mushroom SKU-001 is below minimum threshold. Reorder recommended.', timestamp: new Date(Date.now() - 1800000).toISOString(), actionLabel: 'View Stock' },
  { id: 'i2', type: 'alert', title: 'Warehouse Near Capacity', description: 'Distribution Center - D is at 91% capacity. Consider redistribution.', timestamp: new Date(Date.now() - 3600000).toISOString(), actionLabel: 'View Warehouse' },
  { id: 'i3', type: 'warning', title: 'High Damaged Inventory', description: 'Cold Storage - B reports 180 units damaged. Investigate storage conditions.', timestamp: new Date(Date.now() - 7200000).toISOString(), actionLabel: 'Investigate' },
  { id: 'i4', type: 'info', title: 'Large Pending Inspection', description: '31 receipts awaiting quality inspection. Average wait: 4.2 hours.', timestamp: new Date(Date.now() - 10800000).toISOString(), actionLabel: 'View Queue' },
  { id: 'i5', type: 'alert', title: 'High Expiry Risk', description: '38 batches nearing expiry within 30 days. Plan clearance sale.', timestamp: new Date(Date.now() - 14400000).toISOString(), actionLabel: 'View Expiry' },
  { id: 'i6', type: 'success', title: 'Inventory Trend Positive', description: 'Overall inventory health improved 5% this month. Stock accuracy at 96%.', timestamp: new Date(Date.now() - 18000000).toISOString() },
];

export const MOCK_ALERTS: AlertData[] = [
  { id: 'a1', severity: 'critical', title: 'Critical Stock Out', message: 'Enoki Mushroom SKU-004 is out of stock at Main Warehouse.', timestamp: new Date(Date.now() - 600000).toISOString(), category: 'Stock', acknowledged: false },
  { id: 'a2', severity: 'high', title: 'Warehouse Overflow', message: 'Distribution Center - D exceeds 90% capacity threshold.', timestamp: new Date(Date.now() - 1800000).toISOString(), category: 'Warehouse', acknowledged: false },
  { id: 'a3', severity: 'high', title: 'Batch Recall Notice', message: 'BATCH-0045 quality inspection failed. 500 units affected.', timestamp: new Date(Date.now() - 3600000).toISOString(), category: 'Quality', acknowledged: false },
  { id: 'a4', severity: 'medium', title: 'Inspection Delay', message: '5 receipts pending inspection for over 24 hours.', timestamp: new Date(Date.now() - 7200000).toISOString(), category: 'Receiving', acknowledged: false },
  { id: 'a5', severity: 'low', title: 'Discrepancy Detected', message: 'Stock count mismatch at Dry Storage - C: 12 units.', timestamp: new Date(Date.now() - 14400000).toISOString(), category: 'Stock', acknowledged: true },
];

export const DEFAULT_FILTER_STATE: IntelligenceFilterState = {
  dateRange: { start: '', end: '' }, warehouse: [], category: [], status: [],
};

export const EMPTY_STATES: Record<string, EmptyStateConfig> = {
  noAnalytics: { title: 'No Analytics Data', message: 'No analytics data available for the selected filters.', icon: 'bar-chart', actionLabel: 'Clear Filters' },
  noReports: { title: 'No Reports', message: 'No reports match your current selection.', icon: 'file-text', actionLabel: 'Clear Filters' },
  noInsights: { title: 'No Insights', message: 'No insights generated yet for this period.', icon: 'lightbulb' },
  noAlerts: { title: 'All Clear', message: 'No active alerts at this time.', icon: 'bell' },
  noResults: { title: 'No Results', message: 'Your search returned no results.', icon: 'search', actionLabel: 'Clear Search' },
  offline: { title: 'Connection Offline', message: 'Unable to load analytics data.', icon: 'wifi-off' },
  maintenance: { title: 'Under Maintenance', message: 'The analytics system is undergoing maintenance.', icon: 'tool' },
};

export const SETTINGS_SECTIONS: SettingsSection[] = [
  { id: 'general', label: 'General', icon: 'settings', description: 'General intelligence preferences.' },
  { id: 'kpis', label: 'KPI Configuration', icon: 'bar-chart', description: 'Executive KPI display settings.' },
  { id: 'alerts', label: 'Alert Rules', icon: 'bell', description: 'Alert thresholds and notification rules.' },
  { id: 'reports', label: 'Report Settings', icon: 'file-text', description: 'Report generation and export preferences.' },
];

export const INTELLIGENCE_ROLE_PERMISSIONS: Record<string, string[]> = {
  executive_viewer: ['dashboard', 'reports', 'analytics'],
  inventory_manager: ['dashboard', 'reports', 'analytics', 'forecast', 'insights'],
  warehouse_manager: ['dashboard', 'reports', 'analytics', 'insights'],
  operations_manager: ['dashboard', 'reports', 'analytics', 'forecast', 'insights', 'export'],
  administrator: ['dashboard', 'reports', 'analytics', 'forecast', 'insights', 'export', 'settings'],
};

export const INTELLIGENCE_ROLES = ['executive_viewer', 'inventory_manager', 'warehouse_manager', 'operations_manager', 'administrator'];

export const sections = INTELLIGENCE_SECTIONS;
