import React from 'react';

export interface RadialProgressProps {
  value: number;
  size?: number;
  strokeWidth?: number;
  color?: string;
  trackColor?: string;
  label?: string;
  showValue?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

export const RadialProgress: React.FC<RadialProgressProps> = ({
  value, size = 120, strokeWidth = 8,
  color, trackColor,
  label, showValue = true,
  className = '', style,
}) => {
  const clamped = Math.min(100, Math.max(0, value));
  const r = (size - strokeWidth) / 2;
  const circ = 2 * Math.PI * r;
  const offset = circ * (1 - clamped / 100);
  const c = size / 2;

  const resolvedColor = color ?? 'var(--color-data-viz-1)';
  const resolvedTrack = trackColor ?? 'var(--color-neutral-200)';

  return (
    <svg
      className={className}
      style={{ width: size, height: size, ...style }}
      role="progressbar"
      aria-valuenow={clamped}
      aria-valuemin={0}
      aria-valuemax={100}
      aria-label={label ?? `Progress: ${clamped}%`}
      viewBox={`0 0 ${size} ${size}`}
    >
      <desc>{`Progress: ${clamped}%${label ? ` - ${label}` : ''}`}</desc>

      <circle
        cx={c} cy={c} r={r}
        fill="none"
        stroke={resolvedTrack}
        strokeWidth={strokeWidth}
      />

      <circle
        cx={c} cy={c} r={r}
        fill="none"
        stroke={resolvedColor}
        strokeWidth={strokeWidth}
        strokeLinecap="round"
        strokeDasharray={circ}
        strokeDashoffset={offset}
        transform={`rotate(-90 ${c} ${c})`}
        style={{
          animation: 'rp-fill 1s var(--easing-ease-out) forwards',
        }}
      />

      {showValue && (
        <text
          x={c} y={c}
          textAnchor="middle"
          dominantBaseline="central"
          fontFamily="var(--font-family-sans)"
          fontSize={size * 0.18}
          fontWeight="var(--weight-bold)"
          fill="var(--color-text-primary)"
        >
          {`${Math.round(clamped)}%`}
        </text>
      )}

      <style>{`@keyframes rp-fill{from{stroke-dashoffset:${circ}}to{stroke-dashoffset:${offset}}}`}</style>
    </svg>
  );
};

RadialProgress.displayName = 'RadialProgress';
export default RadialProgress;
