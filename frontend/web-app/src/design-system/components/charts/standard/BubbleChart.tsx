import React from 'react';

export interface BubbleChartDataItem {
  x: number;
  y: number;
  radius: number;
  label?: string;
  color?: string;
}

export interface BubbleChartProps {
  data: BubbleChartDataItem[];
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

function getBounds(data: BubbleChartDataItem[]) {
  let xmn = Infinity, xmx = -Infinity, ymn = Infinity, ymx = -Infinity, maxR = 0;
  for (const d of data) {
    if (d.x - d.radius < xmn) xmn = d.x - d.radius;
    if (d.x + d.radius > xmx) xmx = d.x + d.radius;
    if (d.y - d.radius < ymn) ymn = d.y - d.radius;
    if (d.y + d.radius > ymx) ymx = d.y + d.radius;
    if (d.radius > maxR) maxR = d.radius;
  }
  if (xmn === xmx) { xmn -= 1; xmx += 1; }
  if (ymn === ymx) { ymn -= 1; ymx += 1; }
  const xPad = (xmx - xmn) * 0.1 + maxR;
  const yPad = (ymx - ymn) * 0.1 + maxR;
  return { xMin: xmn - xPad, xMax: xmx + xPad, yMin: ymn - yPad, yMax: ymx + yPad };
}

export const BubbleChart: React.FC<BubbleChartProps> = ({
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

  const descText = data.map((d, i) => `${d.label ?? `Bubble ${i + 1}`}: (${d.x}, ${d.y}, r=${d.radius})`).join('; ');

  return (
    <svg
      className={className}
      style={{ width, height, overflow: 'visible', ...style }}
      role="img"
      aria-label={`Bubble chart with ${data.length} bubbles`}
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
        return (
          <circle
            key={`b${i}`}
            cx={xS(d.x)}
            cy={yS(d.y)}
            r={Math.max(d.radius, 2)}
            fill={col}
            opacity="0.6"
            stroke={col}
            strokeWidth="1"
            role="graphics-symbol"
            aria-label={`${d.label ?? `Bubble ${i + 1}`}: (${d.x}, ${d.y}), size ${d.radius}`}
            style={{
              animation: `bc-appear 0.6s var(--easing-ease-out) ${i * 0.05}s both`,
            }}
          >
            {showTooltip && <title>{`${d.label ?? `Bubble ${i + 1}`}: (${d.x}, ${d.y}), radius ${d.radius}`}</title>}
          </circle>
        );
      })}

      <style>{`@keyframes bc-appear{from{opacity:0;transform:scale(0)}to{opacity:0.6;transform:scale(1)}}`}</style>
    </svg>
  );
};

BubbleChart.displayName = 'BubbleChart';
export default BubbleChart;
