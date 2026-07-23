import React from 'react';

interface BiCustomerChartProps {
  newCustomers?: number;
  churnedCustomers?: number;
  segmentDistribution?: Record<string, number>;
  height?: number;
}

const SEGMENT_COLORS = ['#3b82f6', '#22c55e', '#f59e0b', '#ef4444', '#8b5cf6', '#ec4899'];

export const BiCustomerChart: React.FC<BiCustomerChartProps> = ({
  newCustomers,
  churnedCustomers,
  segmentDistribution,
  height = 250,
}) => {
  return (
    <div style={{ background: '#fff', borderRadius: 8, padding: 16, border: '1px solid #e5e7eb' }}>
      <h3 style={{ margin: '0 0 16px 0', fontSize: 16, fontWeight: 600, color: '#111827' }}>
        Customer Metrics
      </h3>

      {newCustomers !== undefined && churnedCustomers !== undefined && (
        <div style={{ marginBottom: 20 }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 6 }}>
            <span style={{ fontSize: 12, color: '#6b7280' }}>New Customers</span>
            <span style={{ fontSize: 12, fontWeight: 600, color: '#22c55e' }}>{newCustomers}</span>
          </div>
          <div style={{ height: 8, background: '#f3f4f6', borderRadius: 4, overflow: 'hidden' }}>
            <div style={{ height: '100%', background: '#22c55e', borderRadius: 4, transition: 'width 0.3s' }} />
          </div>
          <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: 10, marginBottom: 6 }}>
            <span style={{ fontSize: 12, color: '#6b7280' }}>Churned Customers</span>
            <span style={{ fontSize: 12, fontWeight: 600, color: '#ef4444' }}>{churnedCustomers}</span>
          </div>
          <div style={{ height: 8, background: '#f3f4f6', borderRadius: 4, overflow: 'hidden' }}>
            <div style={{ height: '100%', background: '#ef4444', borderRadius: 4, transition: 'width 0.3s' }} />
          </div>
        </div>
      )}

      {segmentDistribution && Object.keys(segmentDistribution).length > 0 && (
        <div>
          <div style={{ fontSize: 13, fontWeight: 600, color: '#374151', marginBottom: 10 }}>Segment Distribution</div>
          <div style={{ display: 'flex', height: 24, borderRadius: 6, overflow: 'hidden' }}>
            {Object.entries(segmentDistribution).map(([name, count], i) => {
              const total = Object.values(segmentDistribution).reduce((a, b) => a + b, 0);
              const pct = total ? (count / total) * 100 : 0;
              return (
                <div
                  key={name}
                  style={{
                    width: `${pct}%`,
                    background: SEGMENT_COLORS[i % SEGMENT_COLORS.length],
                    minWidth: 4,
                  }}
                  title={`${name}: ${count} (${pct.toFixed(1)}%)`}
                />
              );
            })}
          </div>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: 12, marginTop: 10 }}>
            {Object.entries(segmentDistribution).map(([name, count], i) => (
              <div key={name} style={{ display: 'flex', alignItems: 'center', gap: 4, fontSize: 11, color: '#6b7280' }}>
                <div style={{ width: 8, height: 8, borderRadius: 2, background: SEGMENT_COLORS[i % SEGMENT_COLORS.length] }} />
                <span>{name}: {count}</span>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};
