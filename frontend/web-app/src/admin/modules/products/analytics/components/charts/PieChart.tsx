import React, { memo, useMemo } from 'react';

export interface PieChartProps {
  data: { label: string; value: number; color?: string }[];
  size?: number;
  title?: string;
  showLegend?: boolean;
}

const PALETTE = ['var(--color-chart-1)', 'var(--color-chart-2)', 'var(--color-chart-3)', 'var(--color-chart-4)', 'var(--color-chart-5)', 'var(--color-chart-6)', 'var(--color-chart-7)'];

function polarToCartesian(cx: number, cy: number, r: number, angleDeg: number) {
  const rad = (angleDeg - 90) * Math.PI / 180;
  return { x: cx + r * Math.cos(rad), y: cy + r * Math.sin(rad) };
}

function describeArc(cx: number, cy: number, r: number, startDeg: number, endDeg: number) {
  const start = polarToCartesian(cx, cy, r, endDeg);
  const end = polarToCartesian(cx, cy, r, startDeg);
  const large = endDeg - startDeg > 180 ? 1 : 0;
  return `M ${cx} ${cy} L ${start.x} ${start.y} A ${r} ${r} 0 ${large} 0 ${end.x} ${end.y} Z`;
}

export const PieChart: React.FC<PieChartProps> = memo(({ data, size = 200, title, showLegend = true }) => {
  const total = useMemo(() => data.reduce((s, d) => s + d.value, 0), [data]);
  const cx = size / 2;
  const cy = size / 2;
  const radius = size / 2 - 10;

  const segments = useMemo(() => {
    let currentAngle = 0;
    return data.map((d, i) => {
      const sliceAngle = total > 0 ? (d.value / total) * 360 : 0;
      const startAngle = currentAngle;
      const endAngle = currentAngle + sliceAngle;
      currentAngle = endAngle;
      const midAngle = startAngle + sliceAngle / 2;
      const color = d.color || PALETTE[i % PALETTE.length];
      const pct = total > 0 ? (d.value / total) * 100 : 0;
      const labelPos = polarToCartesian(cx, cy, radius * 0.65, midAngle);
      return {
        ...d,
        color,
        path: describeArc(cx, cy, radius, startAngle, endAngle),
        midAngle,
        pct,
        labelPos,
        labelVisible: pct >= 5,
      };
    });
  }, [data, total, size]);

  return (
    <div role="img" aria-label={title || 'Pie chart'} style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', padding: 16, width: '100%', overflow: 'hidden' }}>
      {title && <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', marginBottom: 12 }}>{title}</div>}
      <svg width="100%" height={size} viewBox={`0 0 ${size} ${size}`} style={{ display: 'block' }}>
        {segments.map((seg, i) => (
          <g key={i}>
            <path d={seg.path} fill={seg.color} stroke="var(--color-bg-surface-default)" strokeWidth={2} />
            {seg.labelVisible && (
              <text x={seg.labelPos.x} y={seg.labelPos.y} textAnchor="middle" dominantBaseline="central" fill="var(--color-text-inverse)" fontSize="var(--text-body-xs)" fontWeight="var(--weight-semibold)">
                {Math.round(seg.pct)}%
              </text>
            )}
          </g>
        ))}
      </svg>
      {showLegend && (
        <div style={{ display: 'flex', gap: 8, justifyContent: 'center', marginTop: 8, flexWrap: 'wrap' }}>
          {segments.map((seg, i) => (
            <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 6, fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>
              <span style={{ width: 8, height: 8, borderRadius: 'var(--radius-full)', background: seg.color, flexShrink: 0 }} />
              <span>{seg.label} ({Math.round(seg.pct)}%)</span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
});

PieChart.displayName = 'PieChart';
export default PieChart;
