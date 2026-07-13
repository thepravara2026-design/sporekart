import React from 'react';

export interface PercentageFormatterProps {
  value: number;
  precision?: number;
  showSign?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

export const PercentageFormatter: React.FC<PercentageFormatterProps> = ({
  value,
  precision = 1,
  showSign = false,
  className = '',
  style,
}) => {
  const isPositive = value > 0;
  const sign = showSign ? (isPositive ? '+' : value < 0 ? '\u2212' : '') : '';
  const formatted = Math.abs(value).toFixed(precision);

  return (
    <span
      className={`sk-percentage-formatter ${className}`.trim()}
      style={{
        fontVariantNumeric: 'tabular-nums',
        color: showSign
          ? isPositive
            ? 'var(--color-text-success)'
            : value < 0
              ? 'var(--color-text-danger)'
              : 'var(--color-text-secondary)'
          : undefined,
        ...style,
      }}
    >
      {sign}{formatted}%
    </span>
  );
};

PercentageFormatter.displayName = 'PercentageFormatter';
export default PercentageFormatter;
