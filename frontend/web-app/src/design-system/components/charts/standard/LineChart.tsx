import React from 'react';

export interface LineChartSeries {
  name: string;
  color?: string;
}

export interface LineChartDataPoint {
  label: string;
  values: number[];
}

export interface LineChartProps {
  data: LineChartDataPoint[];
  series: LineChartSeries[];
  width?: number;
  height?: number;
  variant?: 'straight' | 'smooth' | 'stepped';
  showArea?: boolean;
  showPoints?: boolean;
  showGrid?: boolean;
  showTooltip?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

const PAD = { t: 20, r: 20, b: 40, l: 50 };
const VIZ_COLORS = [
  'var(--color-data-viz-1)',
  'var(--color-data-viz-2)',
  'var(--color-data-viz-3)',
  'var(--color-data-viz-4)',
  'var(--color-data-viz-5)',
  'var(--color-data-viz-6)',
  'var(--color-data-viz-7)',
  'var(--color-data-viz-8)',
];

function getColor(idx: number, custom?: string): string {
  return custom ?? VIZ_COLORS[idx % VIZ_COLORS.length];
}

function getRange(data: LineChartDataPoint[]) {
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

function linePath(pts: { x: number; y: number }[], v: 'straight' | 'smooth' | 'stepped'): string {
  if (!pts.length) return '';
  if (pts.length === 1) return `M ${pts[0].x} ${pts[0].y}`;
  switch (v) {
    case 'stepped': {
      let d = `M ${pts[0].x} ${pts[0].y}`;
      for (let i = 1; i < pts.length; i++) d += ` L ${pts[i].x} ${pts[i - 1].y} L ${pts[i].x} ${pts[i].y}`;
      return d;
    }
    case 'smooth': {
      let d = `M ${pts[0].x} ${pts[0].y}`;
      for (let i = 1; i < pts.length; i++) {
        const p = pts[i - 1], c = pts[i];
        const cpx1 = p.x + (c.x - p.x) / 3;
        const cpx2 = p.x + (c.x - p.x) * 2 / 3;
        d += ` C ${cpx1} ${p.y} ${cpx2} ${c.y} ${c.x} ${c.y}`;
      }
      return d;
    }
    default: {
      let d = `M ${pts[0].x} ${pts[0].y}`;
      for (let i = 1; i < pts.length; i++) d += ` L ${pts[i].x} ${pts[i].y}`;
      return d;
    }
  }
}

function areaPath(pts: { x: number; y: number }[], v: 'straight' | 'smooth' | 'stepped', by: number): string {
  if (!pts.length) return '';
  const lp = linePath(pts, v);
  const last = pts[pts.length - 1];
  return `${lp} L ${last.x} ${by} L ${pts[0].x} ${by} Z`;
}

export const LineChart: React.FC<LineChartProps> = ({
  data, series, width = 600, height = 350,
  variant = 'straight', showArea = false, showPoints = false,
  showGrid = true, showTooltip = false,
  className = '', style,
}) => {
  if (!data.length || !series.length) return null;

  const { min, max } = getRange(data);
  const range = max - min;
  const cw = width - PAD.l - PAD.r;
  const ch = height - PAD.t - PAD.b;

  const xS = (i: number) => PAD.l + (i / Math.max(data.length - 1, 1)) * cw;
  const yS = (v: number) => PAD.t + ch - ((v - min) / range) * ch;
  const bottom = PAD.t + ch;

  const yTicks = 5;
  const yTV = Array.from({ length: yTicks + 1 }, (_, i) => min + (range * i) / yTicks);

  const sPaths = series.map((s, si) => {
    const pts = data.map((d, i) => ({ x: xS(i), y: yS(d.values[si]) })).filter(p => !isNaN(p.y));
    return { name: s.name, color: getColor(si, s.color), points: pts };
  });

  const descText = data.map(d =>
    `${d.label}: ${d.values.map((v, vi) => `${series[vi]?.name || ''}: ${(v).toFixed(1)}`).join(', ')}`
  ).join('; ');

  return (
    <svg
      className={className}
      style={{ width, height, overflow: 'visible', ...style }}
      role="img"
      aria-label={`Line chart: ${series.map(s => s.name).join(', ')}`}
      viewBox={`0 0 ${width} ${height}`}
    >
      <desc>{descText}</desc>

      <defs>
        {sPaths.map((_, i) => (
          <linearGradient key={`ag${i}`} id={`lc-ag-${i}`} x1="0" y1="0" x2="0" y2="1">
            <stop offset="0%" stopColor={sPaths[i].color} stopOpacity="0.3" />
            <stop offset="100%" stopColor={sPaths[i].color} stopOpacity="0.02" />
          </linearGradient>
        ))}
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

      {showArea && sPaths.map((sp, i) => (
        <path key={`ar${i}`} d={areaPath(sp.points, variant, bottom)} fill={`url(#lc-ag-${i})`} stroke="none" />
      ))}

      {sPaths.map((sp, i) => (
        <path
          key={`ln${i}`}
          d={linePath(sp.points, variant)}
          fill="none"
          stroke={sp.color}
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
          style={{
            strokeDasharray: 'var(--ll,3000)',
            animation: 'lc-draw 1.5s var(--easing-ease-out) forwards',
          }}
        />
      ))}

      {showPoints && sPaths.map((sp, si) =>
        sp.points.map((p, pi) => (
          <circle
            key={`pt${si}-${pi}`}
            cx={p.x} cy={p.y} r="4"
            fill={sp.color} stroke="#fff" strokeWidth="2"
            role="graphics-symbol"
            aria-label={`${sp.name}: ${data[pi]?.label} = ${data[pi]?.values[si]}`}
          >
            {showTooltip && <title>{`${sp.name}: ${data[pi]?.label} = ${data[pi]?.values[si]}`}</title>}
          </circle>
        ))
      )}

      <style>{`@keyframes lc-draw{from{stroke-dashoffset:var(--ll,3000)}to{stroke-dashoffset:0}}`}</style>
    </svg>
  );
};

LineChart.displayName = 'LineChart';
export default LineChart;
