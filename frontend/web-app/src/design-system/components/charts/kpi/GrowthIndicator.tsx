import React from 'react';

export interface GrowthIndicatorProps {
  value: number;
  suffix?: string;
  precision?: number;
  size?: 'sm' | 'md' | 'lg';
  showIcon?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

const sizeMap: Record<string, { fontSize: string; iconSize: number; gap: number }> = {
  sm: { fontSize: 'var(--text-body-sm)', iconSize: 12, gap: 2 },
  md: { fontSize: 'var(--text-body)', iconSize: 14, gap: 4 },
  lg: { fontSize: 'var(--text-h4)', iconSize: 18, gap: 4 },
};

export const GrowthIndicator: React.FC<GrowthIndicatorProps> = ({
  value,
  suffix = '%',
  precision = 1,
  size = 'md',
  showIcon = true,
  className = '',
  style,
}) => {
  const dims = sizeMap[size];
  const isPositive = value > 0;
  const isNegative = value < 0;

  let direction: 'up' | 'down' | 'flat';
  let color: string;

  if (isPositive) {
    direction = 'up';
    color = 'var(--color-text-success)';
  } else if (isNegative) {
    direction = 'down';
    color = 'var(--color-text-danger)';
  } else {
    direction = 'flat';
    color = 'var(--color-text-secondary)';
  }

  const absValue = Math.abs(value);
  const formatted = absValue.toFixed(precision);

  return (
    <span
      className={`sk-growth-indicator ${className}`.trim()}
      style={{
        display: 'inline-flex',
        alignItems: 'center',
        gap: dims.gap,
        color,
        fontSize: dims.fontSize,
        fontWeight: 'var(--weight-semibold)',
        lineHeight: 1,
        whiteSpace: 'nowrap',
        ...style,
      }}
      aria-label={`${isPositive ? 'positive' : isNegative ? 'negative' : 'zero'} growth of ${formatted}${suffix}`}
    >
      {showIcon && (
        <span style={{ display: 'inline-flex', alignItems: 'center', lineHeight: 0 }}>
          {direction === 'up' && (
            <svg width={dims.iconSize} height={dims.iconSize} viewBox="0 0 16 16" fill="currentColor" aria-hidden="true">
              <path d="M8 2l6 6h-4v6H6V8H2l6-6z" />
            </svg>
          )}
          {direction === 'down' && (
            <svg width={dims.iconSize} height={dims.iconSize} viewBox="0 0 16 16" fill="currentColor" aria-hidden="true">
              <path d="M8 14l6-6h-4V2H6v6H2l6 6z" />
            </svg>
          )}
          {direction === 'flat' && (
            <svg width={dims.iconSize} height={dims.iconSize} viewBox="0 0 16 16" fill="currentColor" aria-hidden="true">
              <path d="M2 7h12v2H2V7z" />
            </svg>
          )}
        </span>
      )}
      <span>
        {isPositive ? '+' : isNegative ? '-' : ''}{formatted}{suffix}
      </span>
    </span>
  );
};

GrowthIndicator.displayName = 'GrowthIndicator';
export default GrowthIndicator;
