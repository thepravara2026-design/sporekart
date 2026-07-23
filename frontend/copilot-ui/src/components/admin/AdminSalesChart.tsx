import React from 'react';

interface SalesData {
  labels: string[];
  revenue: number[];
  orders: number[];
}

interface AdminSalesChartProps {
  salesData: SalesData | null;
  period: string;
  onPeriodChange: (period: string) => void;
}

const AdminSalesChart: React.FC<AdminSalesChartProps> = ({ salesData, period, onPeriodChange }) => {
  const [viewMode, setViewMode] = React.useState<'revenue' | 'orders'>('revenue');
  const [exporting, setExporting] = React.useState(false);

  const chartHeight = 240;
  const chartWidth = 600;
  const padding = { top: 20, right: 20, bottom: 30, left: 50 };

  const data = salesData || {
    labels: Array.from({ length: 7 }, (_, i) => `Day ${i + 1}`),
    revenue: [380000, 410000, 430000, 460000, 480000, 500000, 485000],
    orders: [42, 48, 53, 58, 62, 60, 55]
  };

  const values = viewMode === 'revenue' ? data.revenue : data.orders;
  const maxVal = Math.max(...values) * 1.15;
  const minVal = Math.min(...values) * 0.85;
  const range = maxVal - minVal;
  const barWidth = (chartWidth - padding.left - padding.right) / data.labels.length * 0.6;
  const gap = (chartWidth - padding.left - padding.right) / data.labels.length * 0.4;

  const formatValue = (v: number) => {
    if (viewMode === 'revenue') {
      return v >= 100000 ? `Rs. ${(v / 100000).toFixed(1)}L` : `Rs. ${v.toLocaleString()}`;
    }
    return v.toString();
  };

  const handleExport = async () => {
    setExporting(true);
    await new Promise((r) => setTimeout(r, 500));
    const csvHeader = 'Label,Revenue,Orders\n';
    const csvRows = data.labels.map((l, i) => `${l},${data.revenue[i]},${data.orders[i]}`).join('\n');
    const blob = new Blob([csvHeader + csvRows], { type: 'text/csv' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `sales_data_${period}.csv`;
    a.click();
    URL.revokeObjectURL(url);
    setExporting(false);
  };

  const periods = ['daily', 'weekly', 'monthly', 'yearly'];

  return (
    <div style={{
      backgroundColor: '#ffffff',
      borderRadius: '12px',
      padding: '20px',
      border: '1px solid #e5e7eb',
      display: 'flex',
      flexDirection: 'column',
      gap: '16px'
    }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h3 style={{ fontSize: '16px', fontWeight: 600, color: '#111827', margin: 0 }}>
          {viewMode === 'revenue' ? 'Revenue' : 'Orders'} Trend
        </h3>
        <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
          <div style={{ display: 'flex', gap: '2px', backgroundColor: '#f3f4f6', borderRadius: '6px', padding: '2px' }}>
            {(['revenue', 'orders'] as const).map((mode) => (
              <button
                key={mode}
                onClick={() => setViewMode(mode)}
                style={{
                  padding: '4px 12px',
                  borderRadius: '4px',
                  border: 'none',
                  backgroundColor: viewMode === mode ? '#ffffff' : 'transparent',
                  color: viewMode === mode ? '#111827' : '#6b7280',
                  fontWeight: 500,
                  fontSize: '12px',
                  cursor: 'pointer',
                  textTransform: 'capitalize',
                  boxShadow: viewMode === mode ? '0 1px 2px rgba(0,0,0,0.1)' : 'none'
                }}
              >
                {mode}
              </button>
            ))}
          </div>
          <button
            onClick={handleExport}
            disabled={exporting}
            style={{
              padding: '6px 14px',
              backgroundColor: exporting ? '#d1d5db' : '#f9fafb',
              color: '#374151',
              border: '1px solid #d1d5db',
              borderRadius: '6px',
              fontWeight: 500,
              fontSize: '12px',
              cursor: exporting ? 'not-allowed' : 'pointer'
            }}
          >
            {exporting ? 'Exporting...' : 'Export CSV'}
          </button>
        </div>
      </div>

      <div style={{ display: 'flex', gap: '4px', backgroundColor: '#f3f4f6', borderRadius: '6px', padding: '3px', alignSelf: 'flex-start' }}>
        {periods.map((p) => (
          <button
            key={p}
            onClick={() => onPeriodChange(p)}
            style={{
              padding: '5px 12px',
              borderRadius: '4px',
              border: 'none',
              backgroundColor: period === p ? '#ffffff' : 'transparent',
              color: period === p ? '#111827' : '#6b7280',
              fontWeight: 500,
              fontSize: '12px',
              cursor: 'pointer',
              textTransform: 'capitalize',
              boxShadow: period === p ? '0 1px 2px rgba(0,0,0,0.1)' : 'none'
            }}
          >
            {p}
          </button>
        ))}
      </div>

      <svg width={chartWidth} height={chartHeight} style={{ alignSelf: 'center' }}>
        <line x1={padding.left} y1={padding.top} x2={padding.left} y2={chartHeight - padding.bottom} stroke="#e5e7eb" />
        <line x1={padding.left} y1={chartHeight - padding.bottom} x2={chartWidth - padding.right} y2={chartHeight - padding.bottom} stroke="#e5e7eb" />

        {[0, 0.25, 0.5, 0.75, 1].map((frac) => {
          const y = chartHeight - padding.bottom - (frac * (chartHeight - padding.top - padding.bottom));
          const val = minVal + frac * range;
          return (
            <g key={frac}>
              <line x1={padding.left} y1={y} x2={chartWidth - padding.right} y2={y} stroke="#f3f4f6" strokeDasharray="4,4" />
              <text x={padding.left - 8} y={y + 4} textAnchor="end" fontSize="11" fill="#9ca3af">
                {formatValue(val)}
              </text>
            </g>
          );
        })}

        {data.labels.map((label, i) => {
          const x = padding.left + i * ((chartWidth - padding.left - padding.right) / data.labels.length) + gap / 2;
          const barH = ((values[i] - minVal) / range) * (chartHeight - padding.top - padding.bottom);
          const y = chartHeight - padding.bottom - barH;

          return (
            <g key={i}>
              <rect x={x} y={y} width={barWidth} height={barH} rx={3} fill={viewMode === 'revenue' ? '#2563eb' : '#059669'} opacity={0.85} />
              <text
                x={x + barWidth / 2}
                y={chartHeight - padding.bottom + 16}
                textAnchor="middle"
                fontSize="11"
                fill="#6b7280"
              >
                {label}
              </text>
              <text
                x={x + barWidth / 2}
                y={y - 6}
                textAnchor="middle"
                fontSize="10"
                fill="#4b5563"
                fontWeight={600}
              >
                {viewMode === 'revenue' ? `Rs. ${(values[i] / 1000).toFixed(0)}k` : values[i]}
              </text>
            </g>
          );
        })}
      </svg>
    </div>
  );
};

export default AdminSalesChart;
