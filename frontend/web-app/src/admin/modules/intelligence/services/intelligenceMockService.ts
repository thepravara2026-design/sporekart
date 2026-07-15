import type { ChartData } from '../types';
import { MOCK_WAREHOUSE_ANALYTICS, MOCK_STOCK_ANALYTICS, MOCK_BATCH_ANALYTICS, MOCK_MOVEMENT_ANALYTICS, MOCK_EXPIRY_ANALYTICS, MOCK_FORECAST } from '../constants';

export function generateWarehouseChart(): ChartData {
  return {
    labels: MOCK_WAREHOUSE_ANALYTICS.topWarehouses.map((w) => w.name.split('-')[0].trim()),
    series: [MOCK_WAREHOUSE_ANALYTICS.topWarehouses.map((w) => ({ name: w.name, value: w.utilization }))],
  };
}

export function generateStockDistributionChart(): ChartData {
  return {
    labels: ['Available', 'Reserved', 'Incoming', 'Blocked', 'Damaged', 'Expired'],
    series: [[
      { name: 'Available', value: MOCK_STOCK_ANALYTICS.available },
      { name: 'Reserved', value: MOCK_STOCK_ANALYTICS.reserved },
      { name: 'Incoming', value: MOCK_STOCK_ANALYTICS.incoming },
      { name: 'Blocked', value: MOCK_STOCK_ANALYTICS.blocked },
      { name: 'Damaged', value: MOCK_STOCK_ANALYTICS.damaged },
      { name: 'Expired', value: MOCK_STOCK_ANALYTICS.expired },
    ]],
  };
}

export function generateExpiryDistributionChart(): ChartData {
  return {
    labels: ['Fresh', 'Healthy', 'Monitor', 'Near Expiry', 'Critical', 'Expired', 'Blocked', 'Disposed'],
    series: [[
      { name: 'Fresh', value: MOCK_EXPIRY_ANALYTICS.fresh },
      { name: 'Healthy', value: MOCK_EXPIRY_ANALYTICS.healthy },
      { name: 'Monitor', value: MOCK_EXPIRY_ANALYTICS.monitor },
      { name: 'Near Expiry', value: MOCK_EXPIRY_ANALYTICS.nearExpiry },
      { name: 'Critical', value: MOCK_EXPIRY_ANALYTICS.critical },
      { name: 'Expired', value: MOCK_EXPIRY_ANALYTICS.expired },
      { name: 'Blocked', value: MOCK_EXPIRY_ANALYTICS.blocked },
      { name: 'Disposed', value: MOCK_EXPIRY_ANALYTICS.disposed },
    ]],
  };
}

export function generateMovementChart(): ChartData {
  return {
    labels: MOCK_MOVEMENT_ANALYTICS.byWarehouse.map((w) => w.warehouse.split('-')[0].trim()),
    series: [MOCK_MOVEMENT_ANALYTICS.byWarehouse.map((w) => ({ name: w.warehouse, value: w.count }))],
  };
}

export function generateForecastChart(): ChartData {
  return {
    labels: MOCK_FORECAST.months,
    series: [
      MOCK_FORECAST.months.map((_m, i) => ({ name: 'Demand', value: MOCK_FORECAST.demandForecast[i] })),
      MOCK_FORECAST.months.map((_n, i) => ({ name: 'Stock', value: MOCK_FORECAST.stockForecast[i] })),
    ],
  };
}

export function generateBatchQualityChart(): ChartData {
  return {
    labels: ['Approved', 'Rejected', 'Pending'],
    series: [[
      { name: 'Approved', value: MOCK_BATCH_ANALYTICS.qualityApproved },
      { name: 'Rejected', value: MOCK_BATCH_ANALYTICS.qualityRejected },
      { name: 'Pending', value: MOCK_BATCH_ANALYTICS.qualityPending },
    ]],
  };
}
