import { MOCK_EXECUTIVE_KPIS, MOCK_KPI_METRICS, MOCK_HEALTH_METRICS, MOCK_WAREHOUSE_ANALYTICS, MOCK_PRODUCT_ANALYTICS, MOCK_STOCK_ANALYTICS, MOCK_BATCH_ANALYTICS, MOCK_MOVEMENT_ANALYTICS, MOCK_EXPIRY_ANALYTICS, MOCK_FORECAST, MOCK_INSIGHTS, MOCK_ALERTS } from '../constants';
import { generateWarehouseChart, generateStockDistributionChart, generateExpiryDistributionChart, generateMovementChart, generateForecastChart, generateBatchQualityChart } from '../services/intelligenceMockService';

export function useIntelligenceData() {
  const executiveKpis = MOCK_EXECUTIVE_KPIS;
  const kpiMetrics = MOCK_KPI_METRICS;
  const healthMetrics = MOCK_HEALTH_METRICS;
  const warehouse = MOCK_WAREHOUSE_ANALYTICS;
  const product = MOCK_PRODUCT_ANALYTICS;
  const stock = MOCK_STOCK_ANALYTICS;
  const batch = MOCK_BATCH_ANALYTICS;
  const movement = MOCK_MOVEMENT_ANALYTICS;
  const expiry = MOCK_EXPIRY_ANALYTICS;
  const forecast = MOCK_FORECAST;
  const insights = MOCK_INSIGHTS;
  const alerts = MOCK_ALERTS;
  const warehouseChart = generateWarehouseChart();
  const stockChart = generateStockDistributionChart();
  const expiryChart = generateExpiryDistributionChart();
  const movementChart = generateMovementChart();
  const forecastChart = generateForecastChart();
  const batchChart = generateBatchQualityChart();
  const loading = false;
  return { executiveKpis, kpiMetrics, healthMetrics, warehouse, product, stock, batch, movement, expiry, forecast, insights, alerts, warehouseChart, stockChart, expiryChart, movementChart, forecastChart, batchChart, loading };
}
