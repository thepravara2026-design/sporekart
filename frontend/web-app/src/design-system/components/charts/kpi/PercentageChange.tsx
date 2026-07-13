import React from 'react';

export interface PercentageChangeProps {
  current: number;
  previous: number;
  precision?: number;
  size?: 'sm' | 'md';
  showLabel?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

const sizeMap: Record<string, { fontSize: string; iconSize: number; gap: number }> = {
  sm: { fontSize: 'var(--text-body-sm)', iconSize: 10, gap: 2 },
  md: { fontSize: 'var(--text-body)', iconSize: 14, gap: 4 },
};

export const PercentageChange: React.FC<PercentageChangeProps> = ({
  current,
  previous,
  precision = 1,
  size = 'md',
  showLabel = false,
  className = '',
  style,
}) => {
  const dims = sizeMap[size];

  let change: number;
  let isFiniteChange = true;

  if (previous === 0) {
    change = current === 0 ? 0 : 100;
    isFiniteChange = current !== 0;
  } else {
    change = ((current - previous) / Math.abs(previous)) * 100;
  }

  const isPositive = change > 0;
  const isNegative = change < 0;

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

  const formatted = (isFiniteChange ? Math.abs(change) : 0).toFixed(precision);

  return (
    <span
      className={`sk-percentage-change ${className}`.trim()}
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
      aria-label={`${isPositive ? 'increase' : isNegative ? 'decrease' : 'no change'} of ${formatted} percent`}
    >
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
      <span>
        {isPositive ? '+' : isNegative ? '\u2212' : ''}
        {formatted}%
      </span>
      {showLabel && (
        <span style={{ color: 'var(--color-text-disabled)', fontWeight: 'var(--weight-normal)' }}>
          vs previous
        </span>
      )}
    </span>
  );
};

PercentageChange.displayName = 'PercentageChange';
export default PercentageChange;
