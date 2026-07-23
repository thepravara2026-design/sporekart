import React from 'react';
import type { CustomerAnalytics } from './types/bi';

interface CustomerDashboardProps { data: CustomerAnalytics; }

function KpiCard({ label, value, format = 'number' }: { label: string; value: number; format?: string }) {
  const formatted = format === 'currency' ? `$${value.toLocaleString()}` : format === 'percent' ? `${value.toFixed(1)}%` : value.toLocaleString();
  return (
    <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: '16px 20px', display: 'flex', flexDirection: 'column', gap: 4 }}>
      <span style={{ fontSize: 12, color: '#6b7280', fontWeight: 500 }}>{label}</span>
      <span style={{ fontSize: 24, fontWeight: 700, color: '#111827' }}>{formatted}</span>
    </div>
  );
}

function SegmentBar({ data }: { data: Record<string, number> }) {
  const entries = Object.entries(data); const total = entries.reduce((s, [, v]) => s + v, 0) || 1;
  const colors = ['#3b82f6', '#22c55e', '#f97316', '#8b5cf6', '#ef4444', '#14b8a6'];
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
      {entries.map(([k, v], i) => (
        <div key={k} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <span style={{ width: 120, fontSize: 12, color: '#374151' }}>{k}</span>
          <div style={{ flex: 1, height: 20, background: '#e5e7eb', borderRadius: 4, overflow: 'hidden' }}>
            <div style={{ width: `${(v / total) * 100}%`, height: '100%', background: colors[i % colors.length], borderRadius: 4 }} />
          </div>
          <span style={{ width: 60, fontSize: 12, color: '#6b7280', textAlign: 'right' }}>{((v / total) * 100).toFixed(1)}%</span>
          <span style={{ width: 60, fontSize: 12, color: '#9ca3af', textAlign: 'right' }}>{v.toLocaleString()}</span>
        </div>
      ))}
    </div>
  );
}

export default function CustomerDashboard({ data }: CustomerDashboardProps) {
  const { totalCustomers, newCustomers, returningCustomers, churnedCustomers, retentionRate, churnRate, customerLifetimeValue, topCustomers, bySegment } = data;
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 12 }}>
        <KpiCard label="Total Customers" value={totalCustomers} />
        <KpiCard label="New Customers" value={newCustomers} />
        <KpiCard label="Returning" value={returningCustomers} />
        <KpiCard label="Churned" value={churnedCustomers} />
        <KpiCard label="Retention Rate" value={retentionRate} format="percent" />
        <KpiCard label="Churn Rate" value={churnRate} format="percent" />
        <KpiCard label="Customer LTV" value={customerLifetimeValue} format="currency" />
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 24 }}>
        <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 16 }}>
          <h3 style={{ fontSize: 14, fontWeight: 600, color: '#111827', margin: '0 0 12px' }}>Customer Segments</h3>
          <SegmentBar data={bySegment} />
        </div>
        <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 16 }}>
          <h3 style={{ fontSize: 14, fontWeight: 600, color: '#111827', margin: '0 0 12px' }}>Top Customers</h3>
          <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 13 }}>
            <thead><tr style={{ borderBottom: '1px solid #e5e7eb' }}>
              <th style={{ textAlign: 'left', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Name</th>
              <th style={{ textAlign: 'right', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Spent</th>
              <th style={{ textAlign: 'right', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Orders</th>
            </tr></thead>
            <tbody>
              {topCustomers.map(c => (
                <tr key={c.customerId} style={{ borderBottom: '1px solid #f3f4f6' }}>
                  <td style={{ padding: '8px 12px', color: '#374151' }}>{c.name}</td>
                  <td style={{ padding: '8px 12px', textAlign: 'right', color: '#111827', fontWeight: 600 }}>${c.totalSpent.toLocaleString()}</td>
                  <td style={{ padding: '8px 12px', textAlign: 'right', color: '#6b7280' }}>{c.orderCount}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
