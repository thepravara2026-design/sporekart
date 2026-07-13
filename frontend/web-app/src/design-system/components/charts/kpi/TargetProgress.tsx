import React from 'react';

export interface TargetProgressProps {
  current: number;
  target: number;
  label?: string;
  showPercentage?: boolean;
  variant?: 'bar' | 'circle';
  size?: 'sm' | 'md' | 'lg';
  color?: string;
  className?: string;
  style?: React.CSSProperties;
}

function getProgressColor(percentage: number): string {
  if (percentage < 25) return 'var(--color-icon-danger)';
  if (percentage < 50) return 'var(--color-icon-warning)';
  if (percentage < 75) return 'var(--color-icon-success)';
  return 'var(--color-icon-info)';
}

const barHeightMap: Record<string, number> = {
  sm: 6,
  md: 10,
  lg: 14,
};

const circleSizeMap: Record<string, number> = {
  sm: 72,
  md: 96,
  lg: 120,
};

const circleStrokeMap: Record<string, number> = {
  sm: 6,
  md: 8,
  lg: 10,
};

const labelSizeMap: Record<string, string> = {
  sm: 'var(--text-body-sm)',
  md: 'var(--text-body)',
  lg: 'var(--text-h4)',
};

const percentSizeMap: Record<string, string> = {
  sm: 'var(--text-body-sm)',
  md: 'var(--text-h4)',
  lg: 'var(--text-h2)',
};

export const TargetProgress: React.FC<TargetProgressProps> = ({
  current,
  target,
  label,
  showPercentage = true,
  variant = 'bar',
  size = 'md',
  color,
  className = '',
  style,
}) => {
  const percentage = target > 0 ? Math.min((current / target) * 100, 100) : 0;
  const progressColor = color || getProgressColor(percentage);
  const roundedPercentage = Math.round(percentage);

  const keyframes = `
    @keyframes sk-progress-fill {
      from { width: 0%; }
      to { width: ${percentage}%; }
    }
    @keyframes sk-circle-fill {
      from { stroke-dashoffset: ${2 * Math.PI * (circleSizeMap[size] / 2 - circleStrokeMap[size] / 2)}; }
      to { stroke-dashoffset: ${2 * Math.PI * (circleSizeMap[size] / 2 - circleStrokeMap[size] / 2) * (1 - percentage / 100)}; }
    }
  `;

  if (variant === 'circle') {
    const circleSize = circleSizeMap[size];
    const strokeWidth = circleStrokeMap[size];
    const radius = circleSize / 2 - strokeWidth / 2;
    const circumference = 2 * Math.PI * radius;
    const offset = circumference * (1 - percentage / 100);

    return (
      <div
        className={`sk-target-progress sk-target-progress--circle ${className}`.trim()}
        style={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          gap: 'var(--space-2)',
          ...style,
        }}
      >
        <style>{keyframes}</style>
        {label && (
          <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)' }}>{label}</span>
        )}
        <div style={{ position: 'relative', width: circleSize, height: circleSize }}>
          <svg width={circleSize} height={circleSize} viewBox={`0 0 ${circleSize} ${circleSize}`} aria-label={`${roundedPercentage}% progress`} role="progressbar" aria-valuenow={current} aria-valuemax={target}>
            <circle
              cx={circleSize / 2}
              cy={circleSize / 2}
              r={radius}
              fill="none"
              stroke="var(--color-bg-skeleton-base)"
              strokeWidth={strokeWidth}
            />
            <circle
              cx={circleSize / 2}
              cy={circleSize / 2}
              r={radius}
              fill="none"
              stroke={progressColor}
              strokeWidth={strokeWidth}
              strokeLinecap="round"
              strokeDasharray={circumference}
              strokeDashoffset={offset}
              transform={`rotate(-90 ${circleSize / 2} ${circleSize / 2})`}
              style={{
                transition: 'stroke-dashoffset var(--duration-slow) var(--easing-ease-out)',
              }}
            />
          </svg>
          {showPercentage && (
            <div
              style={{
                position: 'absolute',
                inset: 0,
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
              }}
            >
              <span
                style={{
                  fontSize: percentSizeMap[size],
                  fontWeight: 'var(--weight-bold)',
                  color: progressColor,
                  lineHeight: 1,
                }}
              >
                {roundedPercentage}%
              </span>
            </div>
          )}
        </div>
        <span style={{ fontSize: labelSizeMap[size], color: 'var(--color-text-secondary)' }}>
          {current.toLocaleString()} / {target.toLocaleString()}
        </span>
      </div>
    );
  }

  return (
    <div
      className={`sk-target-progress sk-target-progress--bar ${className}`.trim()}
      style={{
        display: 'flex',
        flexDirection: 'column',
        gap: 'var(--space-2)',
        ...style,
      }}
    >
      <style>{keyframes}</style>
      {(label || showPercentage) && (
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          {label && (
            <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', fontWeight: 'var(--weight-medium)' }}>
              {label}
            </span>
          )}
          {showPercentage && (
            <span style={{ fontSize: 'var(--text-body-sm)', color: progressColor, fontWeight: 'var(--weight-semibold)' }}>
              {roundedPercentage}%
            </span>
          )}
        </div>
      )}
      <div
        style={{
          height: barHeightMap[size],
          borderRadius: 'var(--radius-full)',
          background: 'var(--color-bg-skeleton-base)',
          overflow: 'hidden',
        }}
        role="progressbar"
        aria-valuenow={current}
        aria-valuemax={target}
        aria-label={`${roundedPercentage}% progress toward ${target}`}
      >
        <div
          style={{
            height: '100%',
            width: `${percentage}%`,
            borderRadius: 'var(--radius-full)',
            background: progressColor,
            transition: 'width var(--duration-slow) var(--easing-ease-out)',
            animation: 'sk-progress-fill var(--duration-slower) var(--easing-ease-out)',
          }}
        />
      </div>
      <span style={{ fontSize: labelSizeMap[size], color: 'var(--color-text-secondary)' }}>
        {current.toLocaleString()} / {target.toLocaleString()}
      </span>
    </div>
  );
};

TargetProgress.displayName = 'TargetProgress';
export default TargetProgress;
