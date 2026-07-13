import React from 'react';

export interface TrendIndicatorProps {
  direction: 'up' | 'down' | 'flat';
  value?: string;
  color?: string;
  size?: 'sm' | 'md';
  className?: string;
  style?: React.CSSProperties;
}

const sizeMap: Record<string, { icon: number; text: string; gap: number }> = {
  sm: { icon: 12, text: 'var(--text-caption)', gap: 2 },
  md: { icon: 16, text: 'var(--text-body-sm)', gap: 4 },
};

const ArrowUp: React.FC<{ size: number }> = ({ size }) => (
  <svg width={size} height={size} viewBox="0 0 16 16" fill="currentColor" aria-hidden="true">
    <path d="M8 2l6 6h-4v6H6V8H2l6-6z" />
  </svg>
);

const ArrowDown: React.FC<{ size: number }> = ({ size }) => (
  <svg width={size} height={size} viewBox="0 0 16 16" fill="currentColor" aria-hidden="true">
    <path d="M8 14l6-6h-4V2H6v6H2l6 6z" />
  </svg>
);

const ArrowFlat: React.FC<{ size: number }> = ({ size }) => (
  <svg width={size} height={size} viewBox="0 0 16 16" fill="currentColor" aria-hidden="true">
    <path d="M2 7h12v2H2V7z" />
  </svg>
);

const iconMap: Record<string, React.FC<{ size: number }>> = {
  up: ArrowUp,
  down: ArrowDown,
  flat: ArrowFlat,
};

const colorMap: Record<string, string> = {
  up: 'var(--color-icon-success)',
  down: 'var(--color-icon-danger)',
  flat: 'var(--color-icon-default)',
};

export const TrendIndicator: React.FC<TrendIndicatorProps> = ({
  direction,
  value,
  color,
  size = 'md',
  className = '',
  style,
}) => {
  const dims = sizeMap[size];
  const Icon = iconMap[direction];
  const arrowColor = color || colorMap[direction];

  return (
    <span
      className={`sk-trend-indicator ${className}`.trim()}
      style={{
        display: 'inline-flex',
        alignItems: 'center',
        gap: dims.gap,
        color: arrowColor,
        fontSize: dims.text,
        fontWeight: 'var(--weight-medium)',
        lineHeight: 1,
        ...style,
      }}
      aria-label={`trend ${direction}`}
    >
      <Icon size={dims.icon} />
      {value != null && <span>{value}</span>}
    </span>
  );
};

TrendIndicator.displayName = 'TrendIndicator';
export default TrendIndicator;
