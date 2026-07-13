import React from 'react';

export interface CurrencyFormatterProps {
  value: number;
  currency?: string;
  compact?: boolean;
  precision?: number;
  className?: string;
  style?: React.CSSProperties;
}

function formatCompact(value: number, currency: string, precision: number): string {
  const abs = Math.abs(value);
  let amount: number;
  let suffix: string;

  if (abs >= 1_000_000_000_000) {
    amount = value / 1_000_000_000_000;
    suffix = 'T';
  } else if (abs >= 1_000_000_000) {
    amount = value / 1_000_000_000;
    suffix = 'B';
  } else if (abs >= 1_000_000) {
    amount = value / 1_000_000;
    suffix = 'M';
  } else if (abs >= 1_000) {
    amount = value / 1_000;
    suffix = 'K';
  } else {
    amount = value;
    suffix = '';
  }

  const formatted = amount.toLocaleString(undefined, {
    style: 'currency',
    currency,
    minimumFractionDigits: precision,
    maximumFractionDigits: precision,
  });

  return formatted.replace(/\d[\d,.]*/, amount.toFixed(precision)) + suffix;
}

export const CurrencyFormatter: React.FC<CurrencyFormatterProps> = ({
  value,
  currency = 'USD',
  compact = false,
  precision = 2,
  className = '',
  style,
}) => {
  let formatted: string;

  if (compact) {
    formatted = formatCompact(value, currency, precision);
  } else {
    formatted = value.toLocaleString(undefined, {
      style: 'currency',
      currency,
      minimumFractionDigits: precision,
      maximumFractionDigits: precision,
    });
  }

  return (
    <span
      className={`sk-currency-formatter ${className}`.trim()}
      style={{
        fontVariantNumeric: 'tabular-nums',
        ...style,
      }}
    >
      {formatted}
    </span>
  );
};

CurrencyFormatter.displayName = 'CurrencyFormatter';
export default CurrencyFormatter;
