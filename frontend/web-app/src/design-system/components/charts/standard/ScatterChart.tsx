import React from 'react';

export interface ScatterChartDataItem {
  x: number;
  y: number;
  label?: string;
  color?: string;
  size?: number;
}

export interface ScatterChartProps {
  data: ScatterChartDataItem[];
  width?: number;
  height?: number;
  showGrid?: boolean;
  showTooltip?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

const PAD = { t: 20, r: 20, b: 45, l: 55 };

function fmt(v: number): string {
  if (Math.abs(v) >= 1_000_000) return `${(v / 1_000_000).toFixed(1)}M`;
  if (Math.abs(v) >= 1_000) return `${(v / 1_000).toFixed(1)}K`;
  return v % 1 === 0 ? v.toFixed(0) : v.toFixed(1);
}

function getBounds(data: ScatterChartDataItem[]) {
  let xmn = Infinity, xmx = -Infinity, ymn = Infinity, ymx = -Infinity;
  for (const d of data) {
    if (d.x < xmn) xmn = d.x;
    if (d.x > xmx) xmx = d.x;
    if (d.y < ymn) ymn = d.y;
    if (d.y > ymx) ymx = d.y;
  }
  if (xmn === xmx) { xmn -= 1; xmx += 1; }
  if (ymn === ymx) { ymn -= 1; ymx += 1; }
  const xPad = (xmx - xmn) * 0.1;
  const yPad = (ymx - ymn) * 0.1;
  return { xMin: xmn - xPad, xMax: xmx + xPad, yMin: ymn - yPad, yMax: ymx + yPad };
}

export const ScatterChart: React.FC<ScatterChartProps> = ({
  data, width = 500, height = 350,
  showGrid = true, showTooltip = false,
  className = '', style,
}) => {
  if (!data.length) return null;

  const { xMin, xMax, yMin, yMax } = getBounds(data);
  const cw = width - PAD.l - PAD.r;
  const ch = height - PAD.t - PAD.b;
  const bottom = PAD.t + ch;

  const xS = (v: number) => PAD.l + ((v - xMin) / (xMax - xMin)) * cw;
  const yS = (v: number) => PAD.t + ch - ((v - yMin) / (yMax - yMin)) * ch;

  const xTicks = 5;
  const yTicks = 5;
  const xTV = Array.from({ length: xTicks + 1 }, (_, i) => xMin + ((xMax - xMin) * i) / xTicks);
  const yTV = Array.from({ length: yTicks + 1 }, (_, i) => yMin + ((yMax - yMin) * i) / yTicks);

  const descText = data.map((d, i) => `${d.label ?? `Point ${i + 1}`}: (${d.x}, ${d.y})`).join('; ');

  return (
    <svg
      className={className}
      style={{ width, height, overflow: 'visible', ...style }}
      role="img"
      aria-label={`Scatter chart with ${data.length} points`}
      viewBox={`0 0 ${width} ${height}`}
    >
      <desc>{descText}</desc>

      {showGrid && (
        <g aria-label="Grid">
          {xTV.map((t, i) => (
            <line key={`xg${i}`} x1={xS(t)} y1={PAD.t} x2={xS(t)} y2={bottom} stroke="var(--color-neutral-200)" strokeWidth="1" />
          ))}
          {yTV.map((t, i) => (
            <line key={`yg${i}`} x1={PAD.l} y1={yS(t)} x2={width - PAD.r} y2={yS(t)} stroke="var(--color-neutral-200)" strokeWidth="1" />
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
        {xTV.map((t, i) => (
          <text key={`xt${i}`} x={xS(t)} y={bottom + 16}>{fmt(t)}</text>
        ))}
      </g>

      {data.map((d, i) => {
        const col = d.color ?? 'var(--color-data-viz-1)';
        const ptR = d.size ?? 5;
        return (
          <circle
            key={`pt${i}`}
            cx={xS(d.x)}
            cy={yS(d.y)}
            r={ptR}
            fill={col}
            opacity="0.8"
            stroke="#fff"
            strokeWidth="1"
            role="graphics-symbol"
            aria-label={`${d.label ?? `Point ${i + 1}`}: (${d.x}, ${d.y})`}
          >
            {showTooltip && <title>{`${d.label ?? `Point ${i + 1}`}: (${d.x}, ${d.y})`}</title>}
          </circle>
        );
      })}
    </svg>
  );
};

ScatterChart.displayName = 'ScatterChart';
export default ScatterChart;
