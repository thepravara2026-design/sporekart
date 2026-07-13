import React from 'react';

export interface CircularProgressProps {
  value?: number;
  size?: number;
  strokeWidth?: number;
  color?: string;
  label?: string;
  showValue?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

const keyframes = `
  @keyframes circularRotate {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
  }
  @keyframes circularDash {
    0% { stroke-dashoffset: 999; }
    50% { stroke-dashoffset: 200; }
    100% { stroke-dashoffset: 999; }
  }
`;

export const CircularProgress: React.FC<CircularProgressProps> = ({
  value,
  size = 48,
  strokeWidth = 4,
  color,
  label,
  showValue = false,
  className = '',
  style,
}) => {
  const isIndeterminate = value === undefined || value === null;
  const radius = (size - strokeWidth) / 2;
  const circumference = 2 * Math.PI * radius;
  const offset = isIndeterminate ? 0 : circumference - (Math.max(0, Math.min(100, value)) / 100) * circumference;
  const strokeColor = color || 'var(--color-bg-primary-default)';

  const containerStyle: React.CSSProperties = {
    display: 'inline-flex',
    flexDirection: 'column',
    alignItems: 'center',
    gap: 'var(--space-stack-xs)',
    ...style,
  };

  const svgStyle: React.CSSProperties = {
    transform: isIndeterminate ? undefined : 'rotate(-90deg)',
    animation: isIndeterminate ? 'circularRotate 2s linear infinite' : undefined,
  };

  const circleStyle: React.CSSProperties = {
    fill: 'none',
    stroke: strokeColor,
    strokeWidth,
    strokeLinecap: 'round',
    strokeDasharray: circumference,
    strokeDashoffset: isIndeterminate ? 0 : offset,
    transition: isIndeterminate ? undefined : `stroke-dashoffset var(--duration-slower) var(--easing-standard)`,
    animation: isIndeterminate ? 'circularDash 1.5s ease-in-out infinite' : undefined,
  };

  const trackStyle: React.CSSProperties = {
    fill: 'none',
    stroke: 'var(--color-bg-skeleton-base)',
    strokeWidth,
  };

  return (
    <div
      className={`sk-circular-progress ${className}`.trim()}
      role="progressbar"
      aria-valuenow={isIndeterminate ? undefined : value}
      aria-valuemin={0}
      aria-valuemax={100}
      aria-label={label || 'Progress'}
      aria-valuetext={isIndeterminate ? 'indeterminate' : undefined}
      style={containerStyle}
    >
      <style>{keyframes}</style>
      <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`} style={svgStyle}>
        <circle cx={size / 2} cy={size / 2} r={radius} style={trackStyle} />
        <circle cx={size / 2} cy={size / 2} r={radius} style={circleStyle} />
      </svg>
      {showValue && !isIndeterminate && (
        <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
          {Math.round(value!)}%
        </span>
      )}
      {label && !showValue && (
        <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
          {label}
        </span>
      )}
    </div>
  );
};

CircularProgress.displayName = 'CircularProgress';
export default CircularProgress;
