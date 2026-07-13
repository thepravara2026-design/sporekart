import React from 'react';

export interface SpinnerProps {
  size?: 'sm' | 'md' | 'lg' | 'xl' | number;
  color?: string;
  speed?: 'fast' | 'normal' | 'slow';
  label?: string;
  className?: string;
  style?: React.CSSProperties;
}

const presetSizes = { sm: 16, md: 24, lg: 32, xl: 48 };
const speedDurations = { fast: 0.4, normal: 0.8, slow: 1.2 };

const keyframes = `
  @keyframes sk-spinner-rotate {
    from { transform: rotate(0deg); }
    to { transform: rotate(360deg); }
  }
`;

export const Spinner: React.FC<SpinnerProps> = ({
  size = 'md',
  color,
  speed = 'normal',
  label,
  className = '',
  style,
}) => {
  const pxSize = typeof size === 'number' ? size : presetSizes[size];
  const duration = speedDurations[speed];
  const strokeColor = color || 'var(--color-bg-primary-default)';
  const strokeW = Math.max(2, Math.round(pxSize / 8));
  const radius = (pxSize - strokeW * 2) / 2;
  const circumference = 2 * Math.PI * radius;

  const svgStyle: React.CSSProperties = {
    animation: `sk-spinner-rotate ${duration}s linear infinite`,
    ...style,
  };

  return (
    <span
      className={`sk-spinner sk-spinner--${typeof size === 'string' ? size : 'custom'} ${className}`.trim()}
      role="status"
      aria-label={label || 'Loading'}
      style={{ display: 'inline-flex', alignItems: 'center', lineHeight: 0 }}
    >
      <style>{keyframes}</style>
      <svg width={pxSize} height={pxSize} viewBox={`0 0 ${pxSize} ${pxSize}`} style={svgStyle} aria-hidden="true">
        <circle
          cx={pxSize / 2}
          cy={pxSize / 2}
          r={radius}
          fill="none"
          stroke="var(--color-bg-skeleton-base)"
          strokeWidth={strokeW}
        />
        <circle
          cx={pxSize / 2}
          cy={pxSize / 2}
          r={radius}
          fill="none"
          stroke={strokeColor}
          strokeWidth={strokeW}
          strokeLinecap="round"
          strokeDasharray={`${circumference * 0.75} ${circumference * 0.25}`}
          strokeDashoffset={circumference * 0.25}
        />
      </svg>
      {label && (
        <span style={{ marginLeft: 'var(--space-inline-sm)', fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>
          {label}
        </span>
      )}
    </span>
  );
};

Spinner.displayName = 'Spinner';
export default Spinner;
