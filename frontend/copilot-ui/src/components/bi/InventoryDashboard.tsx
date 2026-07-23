import React from 'react';
import type { InventoryAnalytics } from './types/bi';

interface InventoryDashboardProps { data: InventoryAnalytics; }

function KpiCard({ label, value, format = 'number' }: { label: string; value: number; format?: string }) {
  const formatted = format === 'percent' ? `${value.toFixed(1)}%` : value.toLocaleString();
  return (
    <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: '16px 20px', display: 'flex', flexDirection: 'column', gap: 4 }}>
      <span style={{ fontSize: 12, color: '#6b7280', fontWeight: 500 }}>{label}</span>
      <span style={{ fontSize: 24, fontWeight: 700, color: '#111827' }}>{formatted}</span>
    </div>
  );
}

function UrgencyBadge({ urgency }: { urgency: string }) {
  const colors: Record<string, { bg: string; text: string }> = {
    critical: { bg: '#fef2f2', text: '#ef4444' },
    high: { bg: '#fff7ed', text: '#f97316' },
    medium: { bg: '#fefce8', text: '#eab308' },
    low: { bg: '#f0fdf4', text: '#22c55e' },
  };
  const c = colors[urgency.toLowerCase()] || { bg: '#f3f4f6', text: '#6b7280' };
  return <span style={{ background: c.bg, color: c.text, padding: '2px 8px', borderRadius: 4, fontSize: 11, fontWeight: 600 }}>{urgency}</span>;
}

function TurnoverGauge({ rate }: { rate: number }) {
  const size = 140; const cx = size / 2; const cy = size / 2; const r = (size - 16) / 2; const sw = 10;
  const circ = 2 * Math.PI * r;
  const normalized = Math.min(rate / 10, 1) * 100;
  const offset = circ - (normalized / 100) * circ;
  const color = rate >= 6 ? '#22c55e' : rate >= 3 ? '#f97316' : '#ef4444';
  return (
    <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 16, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 8 }}>
      <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`}>
        <circle cx={cx} cy={cy} r={r} fill="none" stroke="#e5e7eb" strokeWidth={sw} />
        <circle cx={cx} cy={cy} r={r} fill="none" stroke={color} strokeWidth={sw} strokeDasharray={circ} strokeDashoffset={offset} strokeLinecap="round" transform={`rotate(-90 ${cx} ${cy})`} />
        <text x={cx} y={cy} textAnchor="middle" dominantBaseline="central" fontSize={24} fontWeight={700} fill={color}>{rate.toFixed(1)}x</text>
      </svg>
      <span style={{ fontSize: 12, color: '#6b7280', fontWeight: 500 }}>Turnover Rate</span>
    </div>
  );
}

export default function InventoryDashboard({ data }: InventoryDashboardProps) {
  const { totalStock, lowStockItems, deadStockItems, turnoverRate, inventoryRisk, restockingPriority } = data;
  const riskColor = inventoryRisk >= 70 ? '#ef4444' : inventoryRisk >= 40 ? '#f97316' : '#22c55e';

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 12 }}>
        <KpiCard label="Total Stock" value={totalStock} />
        <KpiCard label="Low Stock Items" value={lowStockItems} />
        <KpiCard label="Dead Stock Items" value={deadStockItems} />
        <KpiCard label="Inventory Risk" value={inventoryRisk} format="percent" />
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 2fr', gap: 24 }}>
        <TurnoverGauge rate={turnoverRate} />
        <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 16 }}>
          <h3 style={{ fontSize: 14, fontWeight: 600, color: '#111827', margin: '0 0 12px' }}>Restocking Priority</h3>
          <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 13 }}>
            <thead><tr style={{ borderBottom: '1px solid #e5e7eb' }}>
              <th style={{ textAlign: 'left', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Product</th>
              <th style={{ textAlign: 'right', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Stock</th>
              <th style={{ textAlign: 'right', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Rec. Order</th>
              <th style={{ textAlign: 'center', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Urgency</th>
            </tr></thead>
            <tbody>
              {restockingPriority.map(item => (
                <tr key={item.productId} style={{ borderBottom: '1px solid #f3f4f6' }}>
                  <td style={{ padding: '8px 12px', color: '#374151', fontWeight: 500 }}>{item.name}</td>
                  <td style={{ padding: '8px 12px', textAlign: 'right', color: '#6b7280' }}>{item.currentStock}</td>
                  <td style={{ padding: '8px 12px', textAlign: 'right', color: '#111827', fontWeight: 600 }}>{item.recommendedOrder}</td>
                  <td style={{ padding: '8px 12px', textAlign: 'center' }}><UrgencyBadge urgency={item.urgency} /></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
