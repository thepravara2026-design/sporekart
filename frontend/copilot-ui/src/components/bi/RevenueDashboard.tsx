import React from 'react';
import type { RevenueAnalytics } from './types/bi';

interface RevenueDashboardProps { data: RevenueAnalytics; }

function KpiCard({ label, value, format = 'currency' }: { label: string; value: number; format?: string }) {
  const formatted = format === 'currency' ? `$${value.toLocaleString()}` : format === 'percent' ? `${value.toFixed(1)}%` : value.toLocaleString();
  return (
    <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: '16px 20px', display: 'flex', flexDirection: 'column', gap: 4 }}>
      <span style={{ fontSize: 12, color: '#6b7280', fontWeight: 500 }}>{label}</span>
      <span style={{ fontSize: 24, fontWeight: 700, color: '#111827' }}>{formatted}</span>
    </div>
  );
}

function StackedBar({ data }: { data: Record<string, number> }) {
  const entries = Object.entries(data); const max = Math.max(...entries.map(([, v]) => v), 1);
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
      {entries.map(([k, v]) => (
        <div key={k} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
          <span style={{ width: 100, fontSize: 12, color: '#374151', textAlign: 'right' }}>{k}</span>
          <div style={{ flex: 1, height: 20, background: '#e5e7eb', borderRadius: 4, overflow: 'hidden' }}>
            <div style={{ width: `${(v / max) * 100}%`, height: '100%', background: '#3b82f6', borderRadius: 4 }} />
          </div>
          <span style={{ width: 80, fontSize: 12, color: '#6b7280', textAlign: 'right' }}>${v.toLocaleString()}</span>
        </div>
      ))}
    </div>
  );
}

function PieChart({ data }: { data: Record<string, number> }) {
  const entries = Object.entries(data); const total = entries.reduce((s, [, v]) => s + v, 0) || 1;
  const colors = ['#3b82f6', '#22c55e', '#f97316', '#8b5cf6', '#ef4444', '#14b8a6', '#eab308', '#ec4899'];
  let cumul = 0;
  const slices = entries.map(([k, v], i) => {
    const pct = (v / total) * 100;
    const startAngle = (cumul / total) * 360;
    cumul += v;
    const endAngle = (cumul / total) * 360;
    return { label: k, value: v, pct, color: colors[i % colors.length], startAngle, endAngle };
  });
  const cx = 120; const cy = 120; const r = 100;
  const toRad = (d: number) => (d - 90) * (Math.PI / 180);
  const paths = slices.map(s => {
    const x1 = cx + r * Math.cos(toRad(s.startAngle));
    const y1 = cy + r * Math.sin(toRad(s.startAngle));
    const x2 = cx + r * Math.cos(toRad(s.endAngle));
    const y2 = cy + r * Math.sin(toRad(s.endAngle));
    const large = s.pct > 50 ? 1 : 0;
    return { ...s, d: `M ${cx} ${cy} L ${x1} ${y1} A ${r} ${r} 0 ${large} 1 ${x2} ${y2} Z` };
  });

  return (
    <div style={{ display: 'flex', alignItems: 'center', gap: 24 }}>
      <svg width={240} height={240} viewBox="0 0 240 240">
        {paths.map(p => <path key={p.label} d={p.d} fill={p.color} stroke="#fff" strokeWidth={2} />)}
      </svg>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
        {slices.map(s => (
          <div key={s.label} style={{ display: 'flex', alignItems: 'center', gap: 8, fontSize: 12 }}>
            <div style={{ width: 10, height: 10, borderRadius: 2, background: s.color }} />
            <span style={{ color: '#374151' }}>{s.label}</span>
            <span style={{ color: '#9ca3af' }}>{s.pct.toFixed(1)}%</span>
          </div>
        ))}
      </div>
    </div>
  );
}

export default function RevenueDashboard({ data }: RevenueDashboardProps) {
  const { grossRevenue, netRevenue, averageOrderValue, revenueGrowth, byCategory, byRegion, byChannel, orderCount } = data;
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 12 }}>
        <KpiCard label="Gross Revenue" value={grossRevenue} />
        <KpiCard label="Net Revenue" value={netRevenue} />
        <KpiCard label="Average Order Value" value={averageOrderValue} />
        <KpiCard label="Revenue Growth" value={revenueGrowth} format="percent" />
        <KpiCard label="Total Orders" value={orderCount} format="number" />
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 24 }}>
        <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 16 }}>
          <h3 style={{ fontSize: 14, fontWeight: 600, color: '#111827', margin: '0 0 12px' }}>Revenue by Category</h3>
          <StackedBar data={byCategory} />
        </div>
        <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 16 }}>
          <h3 style={{ fontSize: 14, fontWeight: 600, color: '#111827', margin: '0 0 12px' }}>Revenue by Channel</h3>
          <PieChart data={byChannel} />
        </div>
      </div>
      <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 16 }}>
        <h3 style={{ fontSize: 14, fontWeight: 600, color: '#111827', margin: '0 0 12px' }}>Revenue by Region</h3>
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 13 }}>
          <thead><tr style={{ borderBottom: '1px solid #e5e7eb' }}>
            <th style={{ textAlign: 'left', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Region</th>
            <th style={{ textAlign: 'right', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Revenue</th>
          </tr></thead>
          <tbody>
            {Object.entries(byRegion).map(([region, rev]) => (
              <tr key={region} style={{ borderBottom: '1px solid #f3f4f6' }}>
                <td style={{ padding: '8px 12px', color: '#374151' }}>{region}</td>
                <td style={{ padding: '8px 12px', textAlign: 'right', color: '#111827', fontWeight: 600 }}>${rev.toLocaleString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
