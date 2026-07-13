import React from 'react';

export interface NumberFormatterProps {
  value: number;
  compact?: boolean;
  precision?: number;
  prefix?: string;
  suffix?: string;
  className?: string;
  style?: React.CSSProperties;
}

function formatCompact(value: number, precision: number): string {
  if (Math.abs(value) >= 1_000_000_000) {
    return (value / 1_000_000_000).toFixed(precision) + 'B';
  }
  if (Math.abs(value) >= 1_000_000) {
    return (value / 1_000_000).toFixed(precision) + 'M';
  }
  if (Math.abs(value) >= 1_000) {
    return (value / 1_000).toFixed(precision) + 'K';
  }
  return value.toFixed(precision);
}

function formatLocale(value: number, precision: number): string {
  return value.toLocaleString(undefined, {
    minimumFractionDigits: precision,
    maximumFractionDigits: precision,
  });
}

export const NumberFormatter: React.FC<NumberFormatterProps> = ({
  value,
  compact = false,
  precision = 0,
  prefix = '',
  suffix = '',
  className = '',
  style,
}) => {
  const formatted = compact ? formatCompact(value, precision) : formatLocale(value, precision);

  return (
    <span
      className={`sk-number-formatter ${className}`.trim()}
      style={{
        fontVariantNumeric: 'tabular-nums',
        ...style,
      }}
    >
      {prefix && <span className="sk-number-formatter__prefix">{prefix}</span>}
      <span className="sk-number-formatter__value">{formatted}</span>
      {suffix && <span className="sk-number-formatter__suffix">{suffix}</span>}
    </span>
  );
};

NumberFormatter.displayName = 'NumberFormatter';
export default NumberFormatter;
