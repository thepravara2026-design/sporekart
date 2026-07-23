import React, { useState } from 'react';
import type { DashboardData, BusinessInsight, AnomalyAlert, RevenueMetrics, CustomerAnalytics } from './types/bi';
import { BiKpiCard } from './BiKpiCard';
import { BiRevenueChart } from './BiRevenueChart';
import { BiCustomerChart } from './BiCustomerChart';
import { BiInsightCard } from './BiInsightCard';
import { BiAnomalyAlert } from './BiAnomalyAlert';

type DashboardType = 'Executive' | 'Operations' | 'Marketing';

interface BiDashboardPanelProps {
  dashboard?: DashboardData | null;
  revenueMetrics?: RevenueMetrics | null;
  customerAnalytics?: CustomerAnalytics | null;
  insights?: BusinessInsight[];
  anomalies?: AnomalyAlert[];
  onRefresh?: () => void;
  onDashboardChange?: (type: DashboardType) => void;
  loading?: boolean;
}

const DASHBOARD_TYPES: DashboardType[] = ['Executive', 'Operations', 'Marketing'];

export const BiDashboardPanel: React.FC<BiDashboardPanelProps> = ({
  dashboard,
  revenueMetrics,
  customerAnalytics,
  insights = [],
  anomalies = [],
  onRefresh,
  onDashboardChange,
  loading = false,
}) => {
  const [selectedDashboard, setSelectedDashboard] = useState<DashboardType>('Executive');

  const handleDashboardChange = (type: DashboardType) => {
    setSelectedDashboard(type);
    onDashboardChange?.(type);
  };

  return (
    <div>
      {/* Header */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 20 }}>
        <div style={{ display: 'flex', gap: 8 }}>
          {DASHBOARD_TYPES.map(type => (
            <button
              key={type}
              onClick={() => handleDashboardChange(type)}
              style={{
                padding: '6px 14px',
                borderRadius: 6,
                border: '1px solid #e5e7eb',
                background: selectedDashboard === type ? '#3b82f6' : '#fff',
                color: selectedDashboard === type ? '#fff' : '#374151',
                fontSize: 13,
                fontWeight: 500,
                cursor: 'pointer',
              }}
            >
              {type}
            </button>
          ))}
        </div>
        <button
          onClick={onRefresh}
          disabled={loading}
          style={{
            padding: '6px 14px',
            borderRadius: 6,
            border: '1px solid #e5e7eb',
            background: '#fff',
            color: '#374151',
            fontSize: 13,
            cursor: 'pointer',
            opacity: loading ? 0.6 : 1,
          }}
        >
          {loading ? 'Refreshing...' : 'Refresh'}
        </button>
      </div>

      {/* KPI Cards */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: 12, marginBottom: 20 }}>
        {revenueMetrics && (
          <>
            <BiKpiCard title="Total Revenue" value={`$${revenueMetrics.totalRevenue.toLocaleString()}`} changePercent={revenueMetrics.growthRate} period="vs last period" />
            <BiKpiCard title="Total Orders" value={revenueMetrics.totalOrders.toLocaleString()} />
            <BiKpiCard title="Avg Order Value" value={`$${revenueMetrics.averageOrderValue.toFixed(2)}`} />
          </>
        )}
        {customerAnalytics && (
          <>
            <BiKpiCard title="Total Customers" value={customerAnalytics.totalCustomers.toLocaleString()} changePercent={customerAnalytics.retentionRate} period="retention" />
            <BiKpiCard title="New Customers" value={customerAnalytics.newCustomers.toLocaleString()} />
            <BiKpiCard title="Churn Rate" value={`${(customerAnalytics.churnRate * 100).toFixed(1)}%`} />
          </>
        )}
        {dashboard?.summaryData && Object.entries(dashboard.summaryData).slice(0, 4).map(([key, val]) => (
          <BiKpiCard key={key} title={key.replace(/([A-Z])/g, ' $1').trim()} value={String(val)} />
        ))}
      </div>

      {/* Charts Row */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 16, marginBottom: 20 }}>
        <BiRevenueChart data={[]} />
        <BiCustomerChart />
      </div>

      {/* Insights */}
      {insights.length > 0 && (
        <div style={{ marginBottom: 20 }}>
          <h3 style={{ fontSize: 15, fontWeight: 600, color: '#111827', marginBottom: 10 }}>Key Insights</h3>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 12 }}>
            {insights.slice(0, 4).map(insight => (
              <BiInsightCard key={insight.insightId} insight={insight} />
            ))}
          </div>
        </div>
      )}

      {/* Anomaly Alerts */}
      {anomalies.length > 0 && (
        <div>
          <h3 style={{ fontSize: 15, fontWeight: 600, color: '#111827', marginBottom: 10 }}>Anomaly Alerts</h3>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: 12 }}>
            {anomalies.slice(0, 3).map(alert => (
              <BiAnomalyAlert key={alert.anomalyId} alert={alert} />
            ))}
          </div>
        </div>
      )}
    </div>
  );
};
