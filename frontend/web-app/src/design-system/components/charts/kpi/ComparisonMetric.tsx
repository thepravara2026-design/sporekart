import React from 'react';

export interface ComparisonMetricProps {
  label: string;
  current: { label: string; value: string | number };
  previous: { label: string; value: string | number };
  change?: number;
  showChange?: boolean;
  className?: string;
  style?: React.CSSProperties;
}

export const ComparisonMetric: React.FC<ComparisonMetricProps> = ({
  label,
  current,
  previous,
  change,
  showChange = true,
  className = '',
  style,
}) => {
  const changeValue = change ?? (
    previous.value !== 0
      ? ((Number(current.value) - Number(previous.value)) / Math.abs(Number(previous.value))) * 100
      : Number(current.value) === 0
        ? 0
        : 100
  );

  const isPositive = changeValue > 0;
  const isNegative = changeValue < 0;

  const changeColor = isPositive
    ? 'var(--color-text-success)'
    : isNegative
      ? 'var(--color-text-danger)'
      : 'var(--color-text-secondary)';

  return (
    <div
      className={`sk-comparison-metric ${className}`.trim()}
      style={{
        display: 'flex',
        flexDirection: 'column',
        gap: 'var(--space-3)',
        padding: 'var(--space-4)',
        background: 'var(--color-bg-surface-default)',
        borderRadius: 'var(--radius-card)',
        border: 'var(--border-width-thin) solid var(--color-border-default)',
        ...style,
      }}
    >
      <span style={{ fontSize: 'var(--text-body-sm)', color: 'var(--color-text-secondary)', fontWeight: 'var(--weight-medium)' }}>
        {label}
      </span>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr auto 1fr', gap: 'var(--space-3)', alignItems: 'center' }}>
        <div style={{ textAlign: 'center' }}>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-disabled)', display: 'block', marginBottom: 'var(--space-1)' }}>
            {previous.label}
          </span>
          <span style={{ fontSize: 'var(--text-h4)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', fontVariantNumeric: 'tabular-nums' }}>
            {previous.value}
          </span>
        </div>

        {showChange && (
          <div style={{ textAlign: 'center' }}>
            <span
              style={{
                display: 'inline-flex',
                alignItems: 'center',
                gap: 2,
                color: changeColor,
                fontSize: 'var(--text-body-sm)',
                fontWeight: 'var(--weight-semibold)',
                whiteSpace: 'nowrap',
              }}
            >
              {isPositive ? (
                <svg width={12} height={12} viewBox="0 0 16 16" fill="currentColor" aria-hidden="true">
                  <path d="M8 2l6 6h-4v6H6V8H2l6-6z" />
                </svg>
              ) : isNegative ? (
                <svg width={12} height={12} viewBox="0 0 16 16" fill="currentColor" aria-hidden="true">
                  <path d="M8 14l6-6h-4V2H6v6H2l6 6z" />
                </svg>
              ) : (
                <svg width={12} height={12} viewBox="0 0 16 16" fill="currentColor" aria-hidden="true">
                  <path d="M2 7h12v2H2V7z" />
                </svg>
              )}
              <span>{isPositive ? '+' : isNegative ? '\u2212' : ''}{Math.abs(changeValue).toFixed(1)}%</span>
            </span>
          </div>
        )}

        <div style={{ textAlign: 'center' }}>
          <span style={{ fontSize: 'var(--text-caption)', color: 'var(--color-text-disabled)', display: 'block', marginBottom: 'var(--space-1)' }}>
            {current.label}
          </span>
          <span style={{ fontSize: 'var(--text-h4)', fontWeight: 'var(--weight-bold)', color: 'var(--color-text-primary)', fontVariantNumeric: 'tabular-nums' }}>
            {current.value}
          </span>
        </div>
      </div>
    </div>
  );
};

ComparisonMetric.displayName = 'ComparisonMetric';
export default ComparisonMetric;
