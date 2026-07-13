import React from 'react';

export interface GaugeSegment {
  label: string;
  color: string;
  threshold: number;
}

export interface GaugeProps {
  value: number;
  min?: number;
  max?: number;
  segments?: GaugeSegment[];
  size?: number;
  label?: string;
  showValue?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

function polar(cx: number, cy: number, r: number, deg: number) {
  const rad = ((deg - 180) * Math.PI) / 180;
  return { x: cx + r * Math.cos(rad), y: cy + r * Math.sin(rad) };
}

function arcPath(cx: number, cy: number, r: number, sa: number, ea: number) {
  const s = polar(cx, cy, r, sa);
  const e = polar(cx, cy, r, ea);
  const large = ea - sa > 180 ? '1' : '0';
  return `M ${s.x} ${s.y} A ${r} ${r} 0 ${large} 0 ${e.x} ${e.y}`;
}

export const Gauge: React.FC<GaugeProps> = ({
  value, min = 0, max = 100, size = 200,
  segments, label, showValue = true,
  className = '', style,
}) => {
  const clamped = Math.min(max, Math.max(min, value));
  const pct = (clamped - min) / (max - min);
  const angleSpan = 270;
  const startAngle = 45;
  const endAngle = startAngle + angleSpan;
  const needleAngle = startAngle + pct * angleSpan;

  const cx = size / 2;
  const r = (size - 20) / 2;
  const cy = size * 0.8;

  const defaultSegments: GaugeSegment[] = [
    { label: 'Low', color: 'var(--color-data-viz-1)', threshold: 33 },
    { label: 'Medium', color: 'var(--color-data-viz-3)', threshold: 66 },
    { label: 'High', color: 'var(--color-data-viz-4)', threshold: 100 },
  ];
  const segs = segments ?? defaultSegments;

  const segPaths = segs.map((s, i) => {
    const prevThreshold = i === 0 ? 0 : segs[i - 1].threshold;
    const sa = startAngle + (prevThreshold / 100) * angleSpan;
    const ea = startAngle + (s.threshold / 100) * angleSpan;
    return { ...s, path: arcPath(cx, cy, r, sa, ea) };
  });

  const needleLen = r * 0.8;
  const needleTip = polar(cx, cy, needleLen, needleAngle);
  const needleBase = polar(cx, cy, -r * 0.12, needleAngle);

  const descText = `Gauge: ${label ?? ''} value ${clamped} of ${min}-${max}`;

  return (
    <svg
      className={className}
      style={{ width: size, height: size, overflow: 'visible', ...style }}
      role="img"
      aria-label={`Gauge: ${label ?? ''} ${clamped} (range ${min}-${max})`}
      viewBox={`0 0 ${size} ${size}`}
    >
      <desc>{descText}</desc>

      {/* Track */}
      <path
        d={arcPath(cx, cy, r, startAngle, endAngle)}
        fill="none"
        stroke="var(--color-neutral-200)"
        strokeWidth="12"
        strokeLinecap="round"
      />

      {/* Segments */}
      {segPaths.map((s, i) => (
        <path
          key={`seg${i}`}
          d={s.path}
          fill="none"
          stroke={s.color}
          strokeWidth="12"
          strokeLinecap="round"
          opacity="0.85"
        >
          {s.label && <title>{s.label}</title>}
        </path>
      ))}

      {/* Needle */}
      <g
        style={{
          animation: 'g-needle 1.2s var(--easing-ease-out) forwards',
        }}
        aria-label={`Needle at ${clamped}`}
      >
        <line
          x1={needleBase.x}
          y1={needleBase.y}
          x2={needleTip.x}
          y2={needleTip.y}
          stroke="var(--color-text-primary)"
          strokeWidth="2.5"
          strokeLinecap="round"
        />
        <circle cx={cx} cy={cy} r="4" fill="var(--color-text-primary)" />
        <circle cx={cx} cy={cy} r="2" fill="var(--color-bg-surface-default)" />
      </g>

      {/* Value label */}
      {showValue && (
        <text
          x={cx} y={cy + r * 0.45}
          textAnchor="middle"
          fontFamily="var(--font-family-sans)"
          fontSize={size * 0.1}
          fontWeight="var(--weight-bold)"
          fill="var(--color-text-primary)"
        >
          {clamped}
        </text>
      )}

      {label && (
        <text
          x={cx} y={cy + r * 0.55}
          textAnchor="middle"
          fontFamily="var(--font-family-sans)"
          fontSize={size * 0.07}
          fill="var(--color-text-secondary)"
        >
          {label}
        </text>
      )}

      <style>{`
        @keyframes g-needle {
          from { transform: rotate(${-pct * angleSpan}deg); }
          to { transform: rotate(0deg); }
        }
      `}</style>
    </svg>
  );
};

Gauge.displayName = 'Gauge';
export default Gauge;
