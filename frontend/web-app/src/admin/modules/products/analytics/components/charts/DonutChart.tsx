import React, { memo, useMemo } from 'react';
import type { ChartConfig } from '../../types';

export interface DonutChartProps {
  data?: { label: string; value: number; color?: string }[];
  config?: ChartConfig;
  size?: number;
  title?: string;
  centerLabel?: string;
  centerValue?: string;
}

const PALETTE = ['var(--color-chart-1)', 'var(--color-chart-2)', 'var(--color-chart-3)', 'var(--color-chart-4)', 'var(--color-chart-5)', 'var(--color-chart-6)', 'var(--color-chart-7)'];

function polarToCartesian(cx: number, cy: number, r: number, angleDeg: number) {
  const rad = (angleDeg - 90) * Math.PI / 180;
  return { x: cx + r * Math.cos(rad), y: cy + r * Math.sin(rad) };
}

function describeArc(cx: number, cy: number, r: number, thickness: number, startDeg: number, endDeg: number) {
  const rInner = r - thickness;
  const s1 = polarToCartesian(cx, cy, r, endDeg);
  const e1 = polarToCartesian(cx, cy, r, startDeg);
  const s2 = polarToCartesian(cx, cy, rInner, startDeg);
  const e2 = polarToCartesian(cx, cy, rInner, endDeg);
  const large = endDeg - startDeg > 180 ? 1 : 0;
  return `M ${s1.x} ${s1.y} A ${r} ${r} 0 ${large} 0 ${e1.x} ${e1.y} L ${s2.x} ${s2.y} A ${rInner} ${rInner} 0 ${large} 1 ${e2.x} ${e2.y} Z`;
}

export const DonutChart: React.FC<DonutChartProps> = memo(({ data: dataProp, config, size = 200, title, centerLabel, centerValue }) => {
  const chartData = useMemo(() => {
    if (dataProp) return dataProp;
    if (config) return config.labels.map((l, i) => ({ label: l, value: config.series[0]?.data[i] || 0, color: config.series[0]?.color }));
    return [];
  }, [dataProp, config]);
  const total = useMemo(() => chartData.reduce((s, d) => s + d.value, 0), [chartData]);
  const cx = size / 2;
  const cy = size / 2;
  const radius = size / 2 - 10;
  const thickness = radius * 0.55;

  const segments = useMemo(() => {
    let currentAngle = 0;
    return chartData.map((d, i) => {
      const sliceAngle = total > 0 ? (d.value / total) * 360 : 0;
      const startAngle = currentAngle;
      const endAngle = currentAngle + sliceAngle;
      currentAngle = endAngle;
      const color = d.color || PALETTE[i % PALETTE.length];
      const pct = total > 0 ? (d.value / total) * 100 : 0;
      return {
        ...d,
        color,
        path: describeArc(cx, cy, radius, thickness, startAngle, endAngle),
        pct,
      };
    });
  }, [chartData, total, size]);

  return (
    <div role="img" aria-label={title || 'Donut chart'} style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', padding: 16, width: '100%', overflow: 'hidden' }}>
      {title && <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', marginBottom: 12 }}>{title}</div>}
      <svg width="100%" height={size} viewBox={`0 0 ${size} ${size}`} style={{ display: 'block' }}>
        {segments.map((seg, i) => (
          <path key={i} d={seg.path} fill={seg.color} stroke="var(--color-bg-surface-default)" strokeWidth={1} />
        ))}
        {(centerLabel || centerValue) && (
          <g>
            {centerValue && (
              <text x={cx} y={cy - (centerLabel ? 6 : 0)} textAnchor="middle" dominantBaseline="central" fill="var(--color-text-primary)" fontSize="var(--text-body-lg)" fontWeight="var(--weight-bold)">
                {centerValue}
              </text>
            )}
            {centerLabel && (
              <text x={cx} y={cy + (centerValue ? 16 : 0)} textAnchor="middle" dominantBaseline="central" fill="var(--color-text-secondary)" fontSize="var(--text-body-xs)">
                {centerLabel}
              </text>
            )}
          </g>
        )}
      </svg>
      <div style={{ display: 'flex', gap: 8, justifyContent: 'center', marginTop: 8, flexWrap: 'wrap' }}>
        {segments.map((seg, i) => (
          <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 6, fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>
            <span style={{ width: 8, height: 8, borderRadius: 'var(--radius-full)', background: seg.color, flexShrink: 0 }} />
            <span>{seg.label} ({Math.round(seg.pct)}%)</span>
          </div>
        ))}
      </div>
    </div>
  );
});

DonutChart.displayName = 'DonutChart';
export default DonutChart;
