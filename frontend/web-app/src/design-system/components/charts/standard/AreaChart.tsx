import React from 'react';

export interface AreaChartSeries {
  name: string;
  color?: string;
}

export interface AreaChartDataPoint {
  label: string;
  values: number[];
}

export interface AreaChartProps {
  data: AreaChartDataPoint[];
  series: AreaChartSeries[];
  variant?: 'single' | 'stacked';
  gradient?: boolean;
  width?: number;
  height?: number;
  showGrid?: boolean;
  showTooltip?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

const PAD = { t: 20, r: 20, b: 40, l: 50 };
const VIZ = [
  'var(--color-data-viz-1)', 'var(--color-data-viz-2)', 'var(--color-data-viz-3)',
  'var(--color-data-viz-4)', 'var(--color-data-viz-5)', 'var(--color-data-viz-6)',
  'var(--color-data-viz-7)', 'var(--color-data-viz-8)',
];

function c(idx: number, custom?: string): string { return custom ?? VIZ[idx % VIZ.length]; }

function getRange(data: AreaChartDataPoint[]) {
  let mn = Infinity, mx = -Infinity;
  for (const d of data) for (const v of d.values) { if (v < mn) mn = v; if (v > mx) mx = v; }
  if (mn === mx) { mn = 0; mx = mx === 0 ? 1 : mx * 1.5; }
  const pad = (mx - mn) * 0.1;
  return { min: mn - pad, max: mx + pad };
}

function fmt(v: number): string {
  if (Math.abs(v) >= 1_000_000) return `${(v / 1_000_000).toFixed(1)}M`;
  if (Math.abs(v) >= 1_000) return `${(v / 1_000).toFixed(1)}K`;
  return v % 1 === 0 ? v.toFixed(0) : v.toFixed(1);
}

export const AreaChart: React.FC<AreaChartProps> = ({
  data, series, width = 600, height = 350,
  variant = 'single', gradient = true,
  showGrid = true, showTooltip = false,
  className = '', style,
}) => {
  if (!data.length || !series.length) return null;

  const { min, max } = getRange(data);
  const range = max - min;
  const cw = width - PAD.l - PAD.r;
  const ch = height - PAD.t - PAD.b;
  const bottom = PAD.t + ch;

  const xS = (i: number) => PAD.l + (i / Math.max(data.length - 1, 1)) * cw;
  const yS = (v: number) => PAD.t + ch - ((v - min) / range) * ch;

  const yTicks = 5;
  const yTV = Array.from({ length: yTicks + 1 }, (_, i) => min + (range * i) / yTicks);

  const descText = data.map(d =>
    `${d.label}: ${d.values.map((v, vi) => `${series[vi]?.name || ''}: ${(v).toFixed(1)}`).join(', ')}`
  ).join('; ');

  const buildPoints = (si: number) =>
    data.map((d, i) => ({ x: xS(i), y: yS(d.values[si]) })).filter(p => !isNaN(p.y));

  const buildAreaPath = (pts: { x: number; y: number }[]) => {
    if (!pts.length) return '';
    let d = `M ${pts[0].x} ${bottom}`;
    for (const p of pts) d += ` L ${p.x} ${p.y}`;
    d += ` L ${pts[pts.length - 1].x} ${bottom} Z`;
    return d;
  };

  return (
    <svg
      className={className}
      style={{ width, height, overflow: 'visible', ...style }}
      role="img"
      aria-label={`Area chart: ${series.map(s => s.name).join(', ')}`}
      viewBox={`0 0 ${width} ${height}`}
    >
      <desc>{descText}</desc>

      <defs>
        {series.map((_, i) => {
          const col = c(i, series[i].color);
          return gradient ? (
            <linearGradient key={`ag${i}`} id={`ac-g-${i}`} x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stopColor={col} stopOpacity="0.4" />
              <stop offset="100%" stopColor={col} stopOpacity="0.05" />
            </linearGradient>
          ) : (
            <linearGradient key={`ag${i}`} id={`ac-g-${i}`} x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stopColor={col} stopOpacity={variant === 'stacked' ? '0.65' : '0.25'} />
              <stop offset="100%" stopColor={col} stopOpacity={variant === 'stacked' ? '0.65' : '0.25'} />
            </linearGradient>
          );
        })}
      </defs>

      {showGrid && (
        <g aria-label="Grid">
          {yTV.map((t, i) => (
            <line key={`g${i}`} x1={PAD.l} y1={yS(t)} x2={width - PAD.r} y2={yS(t)} stroke="var(--color-neutral-200)" strokeWidth="1" />
          ))}
        </g>
      )}

      <g aria-label="Axes">
        <line x1={PAD.l} y1={PAD.t} x2={PAD.l} y2={bottom} stroke="var(--color-neutral-300)" strokeWidth="1" />
        <line x1={PAD.l} y1={bottom} x2={width - PAD.r} y2={bottom} stroke="var(--color-neutral-300)" strokeWidth="1" />
      </g>

      <g aria-label="Y axis" fontFamily="var(--font-family-sans)" fontSize="var(--text-caption)" fill="var(--color-text-secondary)" textAnchor="end">
        {yTV.map((t, i) => (
          <text key={`yt${i}`} x={PAD.l - 8} y={yS(t) + 4}>{fmt(t)}</text>
        ))}
      </g>

      <g aria-label="X axis" fontFamily="var(--font-family-sans)" fontSize="var(--text-caption)" fill="var(--color-text-secondary)" textAnchor="middle">
        {data.map((d, i) => (
          <text key={`xt${i}`} x={xS(i)} y={bottom + 16}>{d.label}</text>
        ))}
      </g>

      {variant === 'single' && series.map((s, i) => {
        const pts = buildPoints(i);
        const col = c(i, s.color);
        return (
          <g key={`sg${i}`}>
            <path d={buildAreaPath(pts)} fill={`url(#ac-g-${i})`} stroke="none" />
            <path d={pts.map((p, j) => (j === 0 ? `M ${p.x} ${p.y}` : `L ${p.x} ${p.y}`)).join(' ')} fill="none" stroke={col} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
            {pts.map((p, j) => (
              <circle key={`pt${j}`} cx={p.x} cy={p.y} r="3" fill={col} stroke="#fff" strokeWidth="2">
                {showTooltip && <title>{`${s.name}: ${data[j]?.label} = ${data[j]?.values[i]}`}</title>}
              </circle>
            ))}
          </g>
        );
      })}

      {variant === 'stacked' && (() => {
        const layers: { x: number; baseY: number; topY: number }[][] = [];
        const seriesCount = series.length;
        for (let i = 0; i < seriesCount; i++) {
          const layer: { x: number; baseY: number; topY: number }[] = [];
          for (let j = 0; j < data.length; j++) {
            let base = 0;
            for (let k = 0; k < i; k++) base += data[j].values[k] || 0;
            const top = base + (data[j].values[i] || 0);
            layer.push({ x: xS(j), baseY: yS(0) + (range > 0 ? ((base - min) / range) * ch : 0), topY: yS(0) + (range > 0 ? ((top - min) / range) * ch : 0) });
          }
          layers.push(layer);
        }

        return layers.map((layer, i) => {
          const col = c(i, series[i].color);
          if (!layer.length) return null;
          let d = `M ${layer[0].x} ${layer[0].baseY}`;
          for (const p of layer) d += ` L ${p.x} ${p.topY}`;
          d += ` L ${layer[layer.length - 1].x} ${layer[layer.length - 1].baseY} Z`;
          return (
            <path
              key={`st${i}`}
              d={d}
              fill={`url(#ac-g-${i})`}
              stroke={col}
              strokeWidth="1"
              role="graphics-symbol"
              aria-label={`${series[i].name} area`}
            >
              {showTooltip && <title>{`${series[i].name}: range ${fmt(data[0]?.values[i] ?? 0)} to ${fmt(data[data.length - 1]?.values[i] ?? 0)}`}</title>}
            </path>
          );
        });
      })()}
    </svg>
  );
};

AreaChart.displayName = 'AreaChart';
export default AreaChart;
