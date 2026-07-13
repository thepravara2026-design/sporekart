import React from 'react';

export interface PieChartDataItem {
  label: string;
  value: number;
  color?: string;
}

export interface PieChartProps {
  data: PieChartDataItem[];
  variant?: 'pie' | 'donut' | 'semi-circle';
  innerRadius?: number;
  showLegend?: boolean;
  width?: number;
  height?: number;
  showTooltip?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

const VIZ = [
  'var(--color-data-viz-1)', 'var(--color-data-viz-2)', 'var(--color-data-viz-3)',
  'var(--color-data-viz-4)', 'var(--color-data-viz-5)', 'var(--color-data-viz-6)',
  'var(--color-data-viz-7)', 'var(--color-data-viz-8)',
];

function c(idx: number, custom?: string): string { return custom ?? VIZ[idx % VIZ.length]; }

function polar(cx: number, cy: number, r: number, deg: number) {
  const rad = ((deg - 90) * Math.PI) / 180;
  return { x: cx + r * Math.cos(rad), y: cy + r * Math.sin(rad) };
}

function describeArc(cx: number, cy: number, or: number, ir: number, sa: number, ea: number) {
  const os = polar(cx, cy, or, ea);
  const oe = polar(cx, cy, or, sa);
  const is_ = polar(cx, cy, ir, ea);
  const ie = polar(cx, cy, ir, sa);
  const large = ea - sa > 180 ? '1' : '0';
  if (ir <= 0) {
    return `M ${os.x} ${os.y} A ${or} ${or} 0 ${large} 0 ${oe.x} ${oe.y} L ${cx} ${cy} Z`;
  }
  return [
    `M ${os.x} ${os.y}`,
    `A ${or} ${or} 0 ${large} 0 ${oe.x} ${oe.y}`,
    `L ${ie.x} ${ie.y}`,
    `A ${ir} ${ir} 0 ${large} 1 ${is_.x} ${is_.y}`,
    'Z',
  ].join(' ');
}

export const PieChart: React.FC<PieChartProps> = ({
  data, width = 400, height = 400,
  variant = 'pie', innerRadius, showLegend = false,
  showTooltip = false, className = '', style,
}) => {
  if (!data.length) return null;

  const total = data.reduce((s, d) => s + Math.abs(d.value), 0);
  if (total === 0) return null;

  const isSemi = variant === 'semi-circle';
  const isDonut = variant === 'donut';
  const ir = innerRadius ?? (isDonut ? 60 : 0);

  const cx = width / 2;
  const cy = isSemi ? height * 0.7 : height / 2;
  const maxR = Math.min(cx, isSemi ? cy : cy) - 20;
  const or = maxR;

  const startAngleBase = isSemi ? 180 : 0;

  const legendY = isSemi ? height - 30 : height - 10;

  const descText = data.map(d => `${d.label}: ${d.value} (${((d.value / total) * 100).toFixed(1)}%)`).join('; ');

  let curAngle = startAngleBase;
  const slices = data.map((d, i) => {
    const sliceAngle = (Math.abs(d.value) / total) * (isSemi ? 180 : 360);
    const sa = curAngle;
    const ea = curAngle + sliceAngle;
    curAngle = ea;
    return {
      label: d.label,
      value: d.value,
      color: c(i, d.color),
      startAngle: sa,
      endAngle: ea,
      percent: (Math.abs(d.value) / total) * 100,
    };
  });

  return (
    <svg
      className={className}
      style={{ width, height, overflow: 'visible', ...style }}
      role="img"
      aria-label={`${variant === 'semi-circle' ? 'Semi-circle' : variant === 'donut' ? 'Donut' : 'Pie'} chart: ${data.map(d => `${d.label}: ${d.value}`).join(', ')}`}
      viewBox={`0 0 ${width} ${height}`}
    >
      <desc>{descText}</desc>

      {slices.map((sl, i) => {
        const path = describeArc(cx, cy, or, ir, sl.startAngle, sl.endAngle);
        return (
          <path
            key={`sl${i}`}
            d={path}
            fill={sl.color}
            stroke="var(--color-bg-surface-default)"
            strokeWidth="1"
            role="graphics-symbol"
            aria-label={`${sl.label}: ${sl.value} (${sl.percent.toFixed(1)}%)`}
            style={{
              strokeDasharray: 'var(--pl,2000)',
              animation: 'pc-draw 1s var(--easing-ease-out) forwards',
            }}
          >
            {showTooltip && <title>{`${sl.label}: ${sl.value} (${sl.percent.toFixed(1)}%)`}</title>}
          </path>
        );
      })}

      {showLegend && (
        <g aria-label="Legend" fontFamily="var(--font-family-sans)" fontSize="var(--text-caption)" fill="var(--color-text-secondary)">
          {slices.map((sl, i) => {
            const cols = Math.min(slices.length, 4);
            const row = Math.floor(i / cols);
            const col = i % cols;
            const lx = 20 + col * (width / cols);
            const ly = legendY + row * 22;
            return (
              <g key={`lg${i}`}>
                <rect x={lx} y={ly - 8} width="10" height="10" rx="2" fill={sl.color} />
                <text x={lx + 16} y={ly}>{`${sl.label} (${sl.percent.toFixed(0)}%)`}</text>
              </g>
            );
          })}
        </g>
      )}

      <style>{`@keyframes pc-draw{from{stroke-dashoffset:var(--pl,2000)}to{stroke-dashoffset:0}}`}</style>
    </svg>
  );
};

PieChart.displayName = 'PieChart';
export default PieChart;
