import React from 'react';
import type { ProductAnalytics, ProductPerformance } from './types/bi';

interface ProductDashboardProps { data: ProductAnalytics; }

function ProductTable({ products, title, color }: { products: ProductPerformance[]; title: string; color: string }) {
  return (
    <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 16 }}>
      <h3 style={{ fontSize: 14, fontWeight: 600, color: '#111827', margin: '0 0 12px' }}>{title}</h3>
      <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 13 }}>
        <thead><tr style={{ borderBottom: '1px solid #e5e7eb' }}>
          <th style={{ textAlign: 'left', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Product</th>
          <th style={{ textAlign: 'left', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Category</th>
          <th style={{ textAlign: 'right', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Revenue</th>
          <th style={{ textAlign: 'right', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Units</th>
          <th style={{ textAlign: 'right', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Growth</th>
          <th style={{ textAlign: 'right', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Margin</th>
        </tr></thead>
        <tbody>
          {products.map(p => (
            <tr key={p.productId} style={{ borderBottom: '1px solid #f3f4f6' }}>
              <td style={{ padding: '8px 12px', color: '#374151', fontWeight: 500 }}>{p.name}</td>
              <td style={{ padding: '8px 12px', color: '#6b7280' }}>{p.category}</td>
              <td style={{ padding: '8px 12px', textAlign: 'right', color: '#111827', fontWeight: 600 }}>${p.revenue.toLocaleString()}</td>
              <td style={{ padding: '8px 12px', textAlign: 'right', color: '#6b7280' }}>{p.unitsSold.toLocaleString()}</td>
              <td style={{ padding: '8px 12px', textAlign: 'right', color: p.growth >= 0 ? '#22c55e' : '#ef4444', fontWeight: 600 }}>{p.growth >= 0 ? '+' : ''}{p.growth.toFixed(1)}%</td>
              <td style={{ padding: '8px 12px', textAlign: 'right', color: '#6b7280' }}>{p.profitMargin.toFixed(1)}%</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function CategoryBar({ data }: { data: Record<string, number> }) {
  const entries = Object.entries(data); const max = Math.max(...entries.map(([, v]) => v), 1);
  const colors = ['#3b82f6', '#22c55e', '#f97316', '#8b5cf6', '#ef4444', '#14b8a6', '#eab308', '#ec4899'];
  return (
    <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 16 }}>
      <h3 style={{ fontSize: 14, fontWeight: 600, color: '#111827', margin: '0 0 12px' }}>Category Performance</h3>
      <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
        {entries.map(([k, v], i) => (
          <div key={k} style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <span style={{ width: 120, fontSize: 12, color: '#374151' }}>{k}</span>
            <div style={{ flex: 1, height: 24, background: '#e5e7eb', borderRadius: 4, overflow: 'hidden' }}>
              <div style={{ width: `${(v / max) * 100}%`, height: '100%', background: colors[i % colors.length], borderRadius: 4 }} />
            </div>
            <span style={{ width: 80, fontSize: 12, color: '#6b7280', textAlign: 'right' }}>${v.toLocaleString()}</span>
          </div>
        ))}
      </div>
    </div>
  );
}

export default function ProductDashboard({ data }: ProductDashboardProps) {
  const { topProducts, worstProducts, fastMovers, categoryPerformance } = data;
  const sortedByMargin = [...topProducts].sort((a, b) => b.profitMargin - a.profitMargin);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 24 }}>
        <ProductTable products={topProducts} title="Top Products" color="#22c55e" />
        <ProductTable products={worstProducts} title="Worst Products" color="#ef4444" />
      </div>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 24 }}>
        <ProductTable products={fastMovers} title="Fast Movers" color="#3b82f6" />
        <CategoryBar data={categoryPerformance} />
      </div>
      <div style={{ background: '#fff', border: '1px solid #e5e7eb', borderRadius: 8, padding: 16 }}>
        <h3 style={{ fontSize: 14, fontWeight: 600, color: '#111827', margin: '0 0 12px' }}>Profitability Ranking</h3>
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 13 }}>
          <thead><tr style={{ borderBottom: '1px solid #e5e7eb' }}>
            <th style={{ textAlign: 'left', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>#</th>
            <th style={{ textAlign: 'left', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Product</th>
            <th style={{ textAlign: 'left', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Category</th>
            <th style={{ textAlign: 'right', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Revenue</th>
            <th style={{ textAlign: 'right', padding: '8px 12px', color: '#6b7280', fontWeight: 500 }}>Margin</th>
          </tr></thead>
          <tbody>
            {sortedByMargin.map((p, i) => (
              <tr key={p.productId} style={{ borderBottom: '1px solid #f3f4f6' }}>
                <td style={{ padding: '8px 12px', color: '#9ca3af', fontWeight: 500 }}>{i + 1}</td>
                <td style={{ padding: '8px 12px', color: '#374151', fontWeight: 500 }}>{p.name}</td>
                <td style={{ padding: '8px 12px', color: '#6b7280' }}>{p.category}</td>
                <td style={{ padding: '8px 12px', textAlign: 'right', color: '#111827' }}>${p.revenue.toLocaleString()}</td>
                <td style={{ padding: '8px 12px', textAlign: 'right', color: p.profitMargin >= 20 ? '#22c55e' : '#f97316', fontWeight: 600 }}>{p.profitMargin.toFixed(1)}%</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
