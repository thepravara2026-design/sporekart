import React from 'react';
import AdminKPICard from './AdminKPICard';
import { AdminDashboard, BusinessInsight } from '../types/admin';

interface AdminDashboardPanelProps {
  dashboard: AdminDashboard | null;
  insights: BusinessInsight[];
  onRefresh: () => void;
  onPeriodChange: (period: string) => void;
}

const AdminDashboardPanel: React.FC<AdminDashboardPanelProps> = ({
  dashboard,
  insights,
  onRefresh,
  onPeriodChange
}) => {
  const [selectedPeriod, setSelectedPeriod] = React.useState('monthly');

  const handlePeriodChange = (period: string) => {
    setSelectedPeriod(period);
    onPeriodChange(period);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px', padding: '24px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h2 style={{ fontSize: '22px', fontWeight: 700, color: '#111827', margin: 0 }}>Admin Dashboard</h2>
        <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
          <div style={{ display: 'flex', gap: '4px', backgroundColor: '#f3f4f6', borderRadius: '8px', padding: '3px' }}>
            {['daily', 'weekly', 'monthly'].map((p) => (
              <button
                key={p}
                onClick={() => handlePeriodChange(p)}
                style={{
                  padding: '6px 14px',
                  borderRadius: '6px',
                  border: 'none',
                  backgroundColor: selectedPeriod === p ? '#ffffff' : 'transparent',
                  color: selectedPeriod === p ? '#111827' : '#6b7280',
                  fontWeight: 500,
                  fontSize: '13px',
                  cursor: 'pointer',
                  boxShadow: selectedPeriod === p ? '0 1px 2px rgba(0,0,0,0.1)' : 'none'
                }}
              >
                {p.charAt(0).toUpperCase() + p.slice(1)}
              </button>
            ))}
          </div>
          <button
            onClick={onRefresh}
            style={{
              padding: '8px 16px',
              backgroundColor: '#2563eb',
              color: '#ffffff',
              border: 'none',
              borderRadius: '8px',
              fontWeight: 500,
              fontSize: '13px',
              cursor: 'pointer'
            }}
          >
            Refresh
          </button>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '16px' }}>
        <AdminKPICard
          title="Total Revenue"
          value={dashboard ? `Rs. ${(dashboard.totalRevenue / 100000).toFixed(1)}L` : '---'}
          change={dashboard ? dashboard.growthRate : 0}
          trend={dashboard && dashboard.growthRate >= 0 ? 'up' : 'down'}
          metric="REVENUE"
        />
        <AdminKPICard
          title="Total Orders"
          value={dashboard ? dashboard.totalOrders.toLocaleString() : '---'}
          change={8.3}
          trend="up"
          metric="ORDERS"
        />
        <AdminKPICard
          title="Total Customers"
          value={dashboard ? dashboard.totalCustomers.toLocaleString() : '---'}
          change={5.1}
          trend="up"
          metric="CUSTOMERS"
        />
        <AdminKPICard
          title="Growth Rate"
          value={dashboard ? `${dashboard.growthRate}%` : '---'}
          change={dashboard ? dashboard.growthRate : 0}
          trend={dashboard && dashboard.growthRate >= 0 ? 'up' : 'down'}
          metric="GROWTH"
        />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '2fr 1fr', gap: '20px' }}>
        <div style={{
          backgroundColor: '#ffffff',
          borderRadius: '12px',
          padding: '20px',
          border: '1px solid #e5e7eb'
        }}>
          <h3 style={{ fontSize: '16px', fontWeight: 600, color: '#111827', margin: '0 0 16px 0' }}>Sales Summary</h3>
          <table style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
              <tr style={{ borderBottom: '2px solid #e5e7eb' }}>
                <th style={{ textAlign: 'left', padding: '8px 4px', fontSize: '13px', color: '#6b7280', fontWeight: 600 }}>Metric</th>
                <th style={{ textAlign: 'right', padding: '8px 4px', fontSize: '13px', color: '#6b7280', fontWeight: 600 }}>Value</th>
                <th style={{ textAlign: 'right', padding: '8px 4px', fontSize: '13px', color: '#6b7280', fontWeight: 600 }}>Change</th>
              </tr>
            </thead>
            <tbody>
              <tr style={{ borderBottom: '1px solid #f3f4f6' }}>
                <td style={{ padding: '10px 4px', fontSize: '14px' }}>Average Order Value</td>
                <td style={{ padding: '10px 4px', fontSize: '14px', textAlign: 'right', fontWeight: 600 }}>Rs. 2,778</td>
                <td style={{ padding: '10px 4px', fontSize: '14px', textAlign: 'right', color: '#16a34a' }}>+6.2%</td>
              </tr>
              <tr style={{ borderBottom: '1px solid #f3f4f6' }}>
                <td style={{ padding: '10px 4px', fontSize: '14px' }}>Pending Orders</td>
                <td style={{ padding: '10px 4px', fontSize: '14px', textAlign: 'right', fontWeight: 600 }}>{dashboard?.pendingOrders ?? '---'}</td>
                <td style={{ padding: '10px 4px', fontSize: '14px', textAlign: 'right', color: '#dc2626' }}>-3.1%</td>
              </tr>
              <tr>
                <td style={{ padding: '10px 4px', fontSize: '14px' }}>Low Stock Items</td>
                <td style={{ padding: '10px 4px', fontSize: '14px', textAlign: 'right', fontWeight: 600 }}>{dashboard?.lowStockItems ?? '---'}</td>
                <td style={{ padding: '10px 4px', fontSize: '14px', textAlign: 'right', color: '#dc2626' }}>+12.5%</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div style={{
          backgroundColor: '#ffffff',
          borderRadius: '12px',
          padding: '20px',
          border: '1px solid #e5e7eb',
          display: 'flex',
          flexDirection: 'column',
          gap: '10px'
        }}>
          <h3 style={{ fontSize: '16px', fontWeight: 600, color: '#111827', margin: 0 }}>Quick Actions</h3>
          {['Business Overview', 'Sales Report', 'Inventory Check', 'Customer Insights', 'View Alerts', 'Platform Health'].map((action) => (
            <button
              key={action}
              onClick={() => {}}
              style={{
                padding: '10px 16px',
                backgroundColor: '#f9fafb',
                border: '1px solid #e5e7eb',
                borderRadius: '8px',
                textAlign: 'left',
                fontSize: '13px',
                fontWeight: 500,
                color: '#374151',
                cursor: 'pointer',
                transition: 'background-color 0.15s'
              }}
              onMouseEnter={(e) => { e.currentTarget.style.backgroundColor = '#f3f4f6'; }}
              onMouseLeave={(e) => { e.currentTarget.style.backgroundColor = '#f9fafb'; }}
            >
              {action}
            </button>
          ))}
        </div>
      </div>

      {insights.length > 0 && (
        <div style={{
          backgroundColor: '#ffffff',
          borderRadius: '12px',
          padding: '20px',
          border: '1px solid #e5e7eb'
        }}>
          <h3 style={{ fontSize: '16px', fontWeight: 600, color: '#111827', margin: '0 0 12px 0' }}>Key Insights</h3>
          {insights.slice(0, 3).map((insight, idx) => (
            <div key={idx} style={{
              padding: '10px 0',
              borderBottom: idx < 2 ? '1px solid #f3f4f6' : 'none',
              fontSize: '14px',
              color: '#4b5563',
              lineHeight: 1.5
            }}>
              <span style={{
                display: 'inline-block',
                width: '8px',
                height: '8px',
                borderRadius: '50%',
                backgroundColor: insight.severity === 'critical' ? '#dc2626' : insight.severity === 'warning' ? '#f59e0b' : '#3b82f6',
                marginRight: '8px',
                verticalAlign: 'middle'
              }} />
              {insight.summary}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default AdminDashboardPanel;
