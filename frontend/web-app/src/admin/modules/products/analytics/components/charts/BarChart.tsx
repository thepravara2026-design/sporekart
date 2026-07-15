import React, { memo, useMemo } from 'react';
import { ChartConfig } from '../../types';

export interface BarChartProps {
  config: ChartConfig;
  height?: number;
  title?: string;
  horizontal?: boolean;
}

const PADDING = { top: 20, right: 20, bottom: 50, left: 60 };
const DEFAULT_COLORS = ['var(--color-chart-1)', 'var(--color-chart-2)', 'var(--color-chart-3)', 'var(--color-chart-4)', 'var(--color-chart-5)'];
const GROUP_GAP = 0.2;

export const BarChart: React.FC<BarChartProps> = memo(({ config, height = 300, title, horizontal }) => {
  const { labels, series } = config;
  const chartWidth = 600;
  const innerWidth = chartWidth - PADDING.left - PADDING.right;
  const innerHeight = height - PADDING.top - PADDING.bottom;

  const allValues = useMemo(() => series.flatMap(s => s.data), [series]);
  const maxVal = Math.max(...allValues, 1);
  const minVal = Math.min(...allValues, 0);
  const range = maxVal - minVal || 1;

  const bandWidth = innerWidth / Math.max(labels.length, 1);
  const barWidth = (bandWidth / series.length) * (1 - GROUP_GAP);

  const yScale = (v: number) => innerHeight - ((v - minVal) / range) * innerHeight;
  const xScale = (i: number) => i * bandWidth + bandWidth * GROUP_GAP / 2;

  const yScaleH = (v: number) => ((v - minVal) / range) * innerWidth;
  const xScaleH = (i: number) => i * bandWidth + bandWidth * GROUP_GAP / 2;

  const yTicks = useMemo(() => {
    const ticks: number[] = [];
    const step = range / 5;
    for (let i = 0; i <= 5; i++) ticks.push(minVal + step * i);
    return ticks;
  }, [minVal, range]);

  const bars = useMemo(() => {
    if (horizontal) {
      return series.map((s, si) => ({
        name: s.name,
        color: s.color || DEFAULT_COLORS[si % DEFAULT_COLORS.length],
        bars: s.data.map((v, i) => ({
          x: PADDING.left,
          y: xScaleH(i) + si * barWidth,
          width: yScaleH(v),
          height: barWidth,
          label: labels[i],
        })),
      }));
    }
    return series.map((s, si) => ({
      name: s.name,
      color: s.color || DEFAULT_COLORS[si % DEFAULT_COLORS.length],
      bars: s.data.map((v, i) => ({
        x: xScale(i) + si * barWidth,
        y: yScale(v),
        width: barWidth,
        height: innerHeight - yScale(v),
        label: labels[i],
      })),
    }));
  }, [series, labels, horizontal]);

  if (horizontal) {
    return (
      <div role="img" aria-label={title || 'Bar chart'} style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', padding: 16, width: '100%', overflow: 'hidden' }}>
        {title && <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', marginBottom: 12 }}>{title}</div>}
        <svg width="100%" height={height} viewBox={`0 0 ${chartWidth} ${height}`} style={{ display: 'block' }}>
          <g transform={`translate(0,${PADDING.top})`}>
            {yTicks.map((tick, i) => (
              <g key={i}>
                <line x1={PADDING.left + yScaleH(tick)} y1={0} x2={PADDING.left + yScaleH(tick)} y2={innerHeight} stroke="var(--color-border)" strokeWidth={1} />
                <text x={PADDING.left + yScaleH(tick)} y={innerHeight + 14} textAnchor="middle" fill="var(--color-text-secondary)" fontSize="var(--text-body-xs)">{Math.round(tick)}</text>
              </g>
            ))}
            {bars.map((s, si) => s.bars.map((b, bi) => (
              <rect key={`${si}-${bi}`} x={b.x} y={b.y} width={Math.max(b.width, 0)} height={barWidth} fill={s.color} rx={2} />
            )))}
            {labels.map((label, i) => (
              <text key={i} x={PADDING.left - 8} y={xScaleH(i) + barWidth * series.length / 2 + 4} textAnchor="end" fill="var(--color-text-secondary)" fontSize="var(--text-body-xs)">{label}</text>
            ))}
          </g>
        </svg>
        {series.length > 1 && (
          <div style={{ display: 'flex', gap: 16, justifyContent: 'center', marginTop: 8, flexWrap: 'wrap' }}>
            {bars.map((s, i) => (
              <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 6, fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>
                <span style={{ width: 10, height: 10, borderRadius: 2, background: s.color, flexShrink: 0 }} />
                <span>{s.name}</span>
              </div>
            ))}
          </div>
        )}
      </div>
    );
  }

  return (
    <div role="img" aria-label={title || 'Bar chart'} style={{ background: 'var(--color-bg-surface-default)', borderRadius: 'var(--radius-md)', padding: 16, width: '100%', overflow: 'hidden' }}>
      {title && <div style={{ fontSize: 'var(--text-body-sm)', fontWeight: 'var(--weight-semibold)', color: 'var(--color-text-primary)', marginBottom: 12 }}>{title}</div>}
      <svg width="100%" height={height} viewBox={`0 0 ${chartWidth} ${height}`} style={{ display: 'block' }}>
        <g transform={`translate(${PADDING.left},${PADDING.top})`}>
          {yTicks.map((tick, i) => (
            <g key={i}>
              <line x1={0} y1={yScale(tick)} x2={innerWidth} y2={yScale(tick)} stroke="var(--color-border)" strokeWidth={1} />
              <text x={-8} y={yScale(tick) + 4} textAnchor="end" fill="var(--color-text-secondary)" fontSize="var(--text-body-xs)">{Math.round(tick)}</text>
            </g>
          ))}
          {bars.map((s, si) => s.bars.map((b, bi) => (
            <rect key={`${si}-${bi}`} x={b.x} y={b.y} width={barWidth} height={Math.max(b.height, 0)} fill={s.color} rx={2} />
          )))}
          {labels.map((label, i) => (
            <text key={i} x={xScale(i) + (bandWidth * series.length) / 2} y={innerHeight + 16} textAnchor="middle" fill="var(--color-text-secondary)" fontSize="var(--text-body-xs)">{label}</text>
          ))}
        </g>
      </svg>
      {series.length > 1 && (
        <div style={{ display: 'flex', gap: 16, justifyContent: 'center', marginTop: 8, flexWrap: 'wrap' }}>
          {bars.map((s, i) => (
            <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 6, fontSize: 'var(--text-body-xs)', color: 'var(--color-text-secondary)' }}>
              <span style={{ width: 10, height: 10, borderRadius: 2, background: s.color, flexShrink: 0 }} />
              <span>{s.name}</span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
});

BarChart.displayName = 'BarChart';
export default BarChart;
