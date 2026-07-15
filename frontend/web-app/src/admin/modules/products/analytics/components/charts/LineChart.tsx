import React, { memo, useMemo } from 'react';
import { ChartConfig } from '../../types';

export interface LineChartProps {
  config: ChartConfig;
  height?: number;
  title?: string;
}

const PADDING = { top: 20, right: 20, bottom: 40, left: 50 };
const DEFAULT_COLORS = ['var(--color-chart-1)', 'var(--color-chart-2)', 'var(--color-chart-3)', 'var(--color-chart-4)', 'var(--color-chart-5)'];

export const LineChart: React.FC<LineChartProps> = memo(({ config, height = 300, title }) => {
  const { labels, series } = config;
  const chartWidth = 600;
  const innerWidth = chartWidth - PADDING.left - PADDING.right;
  const innerHeight = height - PADDING.top - PADDING.bottom;

  const allValues = useMemo(() => series.flatMap(s => s.data), [series]);
  const maxVal = Math.max(...allValues, 1);
  const minVal = Math.min(...allValues, 0);
  const range = maxVal - minVal || 1;

  const yScale = (v: number) => innerHeight - ((v - minVal) / range) * innerHeight;
  const xScale = (_: number, i: number) => (i / Math.max(labels.length - 1, 1)) * innerWidth;

  const yTicks = useMemo(() => {
    const ticks: number[] = [];
    const step = range / 5;
    for (let i = 0; i <= 5; i++) ticks.push(minVal + step * i);
    return ticks;
  }, [minVal, range]);

  const paths = useMemo(() => series.map((s, si) => {
    const pts = s.data.map((v, i) => `${xScale(v, i)},${yScale(v)}`).join(' ');
    return { name: s.name, color: s.color || DEFAULT_COLORS[si % DEFAULT_COLORS.length], d: `M${pts}` };
  }), [series, labels]);

  return (
    <div role="img" aria-label={title || 'Line chart'} style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', padding: 16, width: '100%', overflow: 'hidden' }}>
      {title && <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', marginBottom: 12 }}>{title}</div>}
      <svg width="100%" height={height} viewBox={`0 0 ${chartWidth} ${height}`} style={{ display: 'block' }}>
        <g transform={`translate(${PADDING.left},${PADDING.top})`}>
          {yTicks.map((tick, i) => (
            <g key={i}>
              <line x1={0} y1={yScale(tick)} x2={innerWidth} y2={yScale(tick)} stroke="var(--color-border)" strokeWidth={1} />
              <text x={-8} y={yScale(tick) + 4} textAnchor="end" fill="var(--color-text-secondary)" fontSize="var(--text-body-xs)">{Math.round(tick)}</text>
            </g>
          ))}
          {labels.map((label, i) => (
            <text key={i} x={xScale(0, i)} y={innerHeight + 16} textAnchor="middle" fill="var(--color-text-secondary)" fontSize="var(--text-body-xs)">{label}</text>
          ))}
          {paths.map((p, i) => (
            <g key={i}>
              <path d={p.d} fill="none" stroke={p.color} strokeWidth={2} strokeLinejoin="round" strokeLinecap="round" />
              {series[i].data.map((v, j) => (
                <circle key={j} cx={xScale(v, j)} cy={yScale(v)} r={3} fill={p.color} stroke="var(--color-bg-surface-default)" strokeWidth={2} />
              ))}
            </g>
          ))}
        </g>
      </svg>
      {series.length > 1 && (
        <div style={{ display: 'flex', gap: 16, justifyContent: 'center', marginTop: 8, flexWrap: 'wrap' }}>
          {paths.map((p, i) => (
            <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 6, fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>
              <span style={{ width: 10, height: 3, borderRadius: 2, background: p.color, flexShrink: 0 }} />
              <span>{p.name}</span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
});

LineChart.displayName = 'LineChart';
export default LineChart;
