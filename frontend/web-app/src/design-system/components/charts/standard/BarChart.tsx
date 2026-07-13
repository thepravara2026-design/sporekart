import React from 'react';

export interface BarChartSeries {
  name: string;
  color?: string;
}

export interface BarChartDataPoint {
  label: string;
  values: number[];
}

export interface BarChartProps {
  data: BarChartDataPoint[];
  series: BarChartSeries[];
  variant?: 'vertical' | 'horizontal' | 'grouped' | 'stacked' | 'comparison';
  width?: number;
  height?: number;
  showGrid?: boolean;
  showTooltip?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

const PAD = { t: 20, r: 20, b: 50, l: 60 };
const VIZ = [
  'var(--color-data-viz-1)', 'var(--color-data-viz-2)', 'var(--color-data-viz-3)',
  'var(--color-data-viz-4)', 'var(--color-data-viz-5)', 'var(--color-data-viz-6)',
  'var(--color-data-viz-7)', 'var(--color-data-viz-8)',
];

function c(idx: number, custom?: string): string { return custom ?? VIZ[idx % VIZ.length]; }

function getRange(data: BarChartDataPoint[]) {
  let mx = -Infinity;
  for (const d of data) for (const v of d.values) if (v > mx) mx = v;
  if (mx <= 0) mx = 1;
  return mx * 1.15;
}

function fmt(v: number): string {
  if (Math.abs(v) >= 1_000_000) return `${(v / 1_000_000).toFixed(1)}M`;
  if (Math.abs(v) >= 1_000) return `${(v / 1_000).toFixed(1)}K`;
  return v % 1 === 0 ? v.toFixed(0) : v.toFixed(1);
}

export const BarChart: React.FC<BarChartProps> = ({
  data, series, width = 600, height = 350,
  variant = 'vertical', showGrid = true, showTooltip = false,
  className = '', style,
}) => {
  if (!data.length || !series.length) return null;

  const maxVal = getRange(data);
  const cw = width - PAD.l - PAD.r;
  const ch = height - PAD.t - PAD.b;
  const bottom = PAD.t + ch;

  const yTicks = 5;
  const yTV = Array.from({ length: yTicks + 1 }, (_, i) => (maxVal * i) / yTicks);

  const gap = 0.2;
  const catW = variant === 'horizontal' ? cw / series.length : cw / data.length;
  const barW = variant === 'grouped'
    ? (catW * (1 - gap * 2)) / series.length
    : variant === 'comparison'
      ? (catW * (1 - gap * 2)) / series.length
      : catW * (1 - gap * 2);

  const descText = data.map(d =>
    `${d.label}: ${d.values.map((v, vi) => `${series[vi]?.name || ''}: ${v.toFixed(1)}`).join(', ')}`
  ).join('; ');

  const hAxisLabelOff = 16;
  const vAxisLabelOff = PAD.l - 8;

  const isVertical = variant === 'vertical' || variant === 'grouped' || variant === 'stacked' || variant === 'comparison';

  return (
    <svg
      className={className}
      style={{ width, height, overflow: 'visible', ...style }}
      role="img"
      aria-label={`Bar chart (${variant}): ${series.map(s => s.name).join(', ')}`}
      viewBox={`0 0 ${width} ${height}`}
    >
      <desc>{descText}</desc>

      {showGrid && (
        <g aria-label="Grid">
          {yTV.map((t, i) => (
            <line key={`g${i}`} x1={PAD.l} y1={bottom - (t / maxVal) * ch} x2={width - PAD.r} y2={bottom - (t / maxVal) * ch} stroke="var(--color-neutral-200)" strokeWidth="1" />
          ))}
        </g>
      )}

      <g aria-label="Axes">
        <line x1={PAD.l} y1={PAD.t} x2={PAD.l} y2={bottom} stroke="var(--color-neutral-300)" strokeWidth="1" />
        <line x1={PAD.l} y1={bottom} x2={width - PAD.r} y2={bottom} stroke="var(--color-neutral-300)" strokeWidth="1" />
      </g>

      {(isVertical) && (
        <g aria-label="Y axis" fontFamily="var(--font-family-sans)" fontSize="var(--text-caption)" fill="var(--color-text-secondary)" textAnchor="end">
          {yTV.map((t, i) => (
            <text key={`yt${i}`} x={vAxisLabelOff} y={bottom - (t / maxVal) * ch + 4}>{fmt(t)}</text>
          ))}
        </g>
      )}

      {/* VERTICAL */}
      {variant === 'vertical' && data.map((d, di) => {
        const x = PAD.l + di * catW + catW * gap;
        const h = (d.values[0] / maxVal) * ch;
        return (
          <rect
            key={`v${di}`}
            x={x} y={bottom - h} width={barW} height={h}
            fill={c(0, series[0]?.color)}
            rx="2"
            role="graphics-symbol"
            aria-label={`${d.label}: ${d.values[0]}`}
          >
            {showTooltip && <title>{`${series[0]?.name}: ${d.label} = ${d.values[0]}`}</title>}
          </rect>
        );
      })}

      {/* HORIZONTAL */}
      {variant === 'horizontal' && data.map((d, di) => {
        const y = PAD.t + di * catW + catW * gap;
        const w = (d.values[0] / maxVal) * cw;
        return (
          <rect
            key={`h${di}`}
            x={PAD.l} y={y} width={w} height={barW}
            fill={c(0, series[0]?.color)}
            rx="2"
            role="graphics-symbol"
            aria-label={`${d.label}: ${d.values[0]}`}
          >
            {showTooltip && <title>{`${series[0]?.name}: ${d.label} = ${d.values[0]}`}</title>}
          </rect>
        );
      })}

      {/* GROUPED */}
      {variant === 'grouped' && data.map((d, di) =>
        series.map((s, si) => {
          const x = PAD.l + di * catW + catW * gap + si * barW;
          const h = (d.values[si] / maxVal) * ch;
          return (
            <rect
              key={`grp${di}-${si}`}
              x={x} y={bottom - h} width={barW * 0.85} height={Math.max(h, 0)}
              fill={c(si, s.color)}
              rx="2"
              role="graphics-symbol"
              aria-label={`${s.name}, ${d.label}: ${d.values[si]}`}
            >
              {showTooltip && <title>{`${s.name}: ${d.label} = ${d.values[si]}`}</title>}
            </rect>
          );
        })
      )}

      {/* STACKED */}
      {variant === 'stacked' && data.map((d, di) => {
        let accH = 0;
        return series.map((s, si) => {
          const h = (d.values[si] / maxVal) * ch;
          const x = PAD.l + di * catW + catW * gap;
          const y = bottom - accH - h;
          const el = (
            <rect
              key={`stk${di}-${si}`}
              x={x} y={y} width={barW} height={Math.max(h, 0)}
              fill={c(si, s.color)}
              rx="2"
              role="graphics-symbol"
              aria-label={`${s.name}, ${d.label}: ${d.values[si]}`}
            >
              {showTooltip && <title>{`${s.name}: ${d.label} = ${d.values[si]}`}</title>}
            </rect>
          );
          accH += h;
          return el;
        });
      })}

      {/* COMPARISON */}
      {variant === 'comparison' && data.map((d, di) =>
        series.map((s, si) => {
          const x = PAD.l + di * catW + catW * gap + si * barW;
          const h = (d.values[si] / maxVal) * ch;
          return (
            <rect
              key={`cmp${di}-${si}`}
              x={x} y={bottom - h} width={barW * 0.85} height={Math.max(h, 0)}
              fill={c(si, s.color)}
              opacity={si === 0 ? 0.85 : 0.45}
              rx="2"
              role="graphics-symbol"
              aria-label={`${s.name}, ${d.label}: ${d.values[si]}`}
            >
              {showTooltip && <title>{`${s.name}: ${d.label} = ${d.values[si]}`}</title>}
            </rect>
          );
        })
      )}

      {/* X axis labels */}
      {isVertical && (
        <g aria-label="X axis" fontFamily="var(--font-family-sans)" fontSize="var(--text-caption)" fill="var(--color-text-secondary)" textAnchor="middle">
          {data.map((d, i) => (
            <text key={`xl${i}`} x={PAD.l + i * catW + catW / 2} y={bottom + hAxisLabelOff}>{d.label}</text>
          ))}
        </g>
      )}

      {variant === 'horizontal' && (
        <g aria-label="Y axis" fontFamily="var(--font-family-sans)" fontSize="var(--text-caption)" fill="var(--color-text-secondary)" textAnchor="end">
          {data.map((d, i) => (
            <text key={`yl${i}`} x={PAD.l - 8} y={PAD.t + i * catW + catW / 2 + 4}>{d.label}</text>
          ))}
        </g>
      )}
    </svg>
  );
};

BarChart.displayName = 'BarChart';
export default BarChart;
