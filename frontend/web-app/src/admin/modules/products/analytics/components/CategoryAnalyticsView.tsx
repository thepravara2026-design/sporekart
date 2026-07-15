import React from 'react';
import type { CategoryAnalytic, ChartConfig } from '../types';
import { BarChart } from './charts/BarChart';

interface CategoryAnalyticsViewProps {
  categories: CategoryAnalytic[];
  chartConfig: ChartConfig;
}

export const CategoryAnalyticsView: React.FC<CategoryAnalyticsViewProps> = React.memo(({ categories, chartConfig }) => {
  const sorted = [...categories].sort((a, b) => b.productCount - a.productCount);
  const largest = sorted[0];
  const unused = categories.filter((c) => c.productCount === 0);
  const fastestGrowers = [...categories].sort((a, b) => b.health - a.health).slice(0, 3);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-component-gap)' }} role="region" aria-label="Category analytics">
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(180px, 1fr))', gap: 'var(--space-component-gap)' }}>
        {largest && (
          <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
            <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Largest Category</span>
            <p style={{ margin: '4px 0 0', fontSize: 'var(--text-h5)', color: 'var(--color-text-primary)', fontWeight: 600 }}>{largest.name}</p>
            <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{largest.productCount} products</span>
          </div>
        )}
        <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Unused Categories</span>
          <p style={{ margin: '4px 0 0', fontSize: 'var(--text-h5)', color: unused.length > 0 ? 'var(--color-accent-red)' : 'var(--color-accent-green)', fontWeight: 600 }}>{unused.length}</p>
          <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{unused.length === 0 ? 'All categories in use' : 'categories with 0 products'}</span>
        </div>
        <div style={{ padding: 16, borderRadius: 'var(--radius-md)', border: '1px solid var(--color-border)', background: 'var(--color-bg-surface-default)' }}>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-secondary)' }}>Fastest Growing</span>
          <p style={{ margin: '4px 0 0', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-primary)', fontWeight: 500 }}>{fastestGrowers.map((c) => c.name).join(', ')}</p>
        </div>
      </div>
      <BarChart config={chartConfig} />
      <div style={{ overflowX: 'auto' }}>
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 'var(--text-body-sm)' }} aria-label="Category table">
          <thead>
            <tr style={{ borderBottom: '1px solid var(--color-border)', color: 'var(--color-text-secondary)' }}>
              <th style={{ textAlign: 'left', padding: '10px 12px', fontWeight: 500 }}>Name</th>
              <th style={{ textAlign: 'right', padding: '10px 12px', fontWeight: 500 }}>Products</th>
              <th style={{ textAlign: 'right', padding: '10px 12px', fontWeight: 500 }}>Completion</th>
              <th style={{ textAlign: 'right', padding: '10px 12px', fontWeight: 500 }}>Health</th>
              <th style={{ textAlign: 'right', padding: '10px 12px', fontWeight: 500 }}>Depth</th>
            </tr>
          </thead>
          <tbody>
            {categories.map((c) => (
              <tr key={c.id} style={{ borderBottom: '1px solid var(--color-border)' }}>
                <td style={{ padding: '10px 12px', color: 'var(--color-text-primary)', fontWeight: 500 }}>{c.name}</td>
                <td style={{ padding: '10px 12px', textAlign: 'right', color: 'var(--color-text-primary)' }}>{c.productCount}</td>
                <td style={{ padding: '10px 12px', textAlign: 'right', color: c.completion >= 80 ? 'var(--color-accent-green)' : c.completion >= 60 ? 'var(--color-accent-yellow)' : 'var(--color-accent-red)' }}>{c.completion}%</td>
                <td style={{ padding: '10px 12px', textAlign: 'right', color: 'var(--color-text-primary)' }}>{c.health}</td>
                <td style={{ padding: '10px 12px', textAlign: 'right', color: 'var(--color-text-secondary)' }}>{c.depth}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
});

export default CategoryAnalyticsView;
