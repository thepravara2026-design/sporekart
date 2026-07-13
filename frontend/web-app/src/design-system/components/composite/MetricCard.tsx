import React from 'react';
import { Card } from './Card';

export interface MetricCardProps {
  label: string;
  value: string | number;
  unit?: string;
  previousValue?: string | number;
  changePercent?: number;
  changeType?: 'increase' | 'decrease' | 'neutral';
  icon?: React.ReactNode;
  loading?: boolean;
  error?: string;
}

export const MetricCard: React.FC<MetricCardProps> = ({
  label,
  value,
  unit,
  previousValue,
  changePercent,
  changeType = 'neutral',
  icon,
  loading = false,
  error,
}) => {
  const changeColor = changeType === 'increase'
    ? 'var(--color-success)'
    : changeType === 'decrease'
    ? 'var(--color-danger)'
    : 'var(--color-text-secondary)';

  const changeSign = changeType === 'increase' ? '+' : changeType === 'decrease' ? '-' : '';

  return (
    <Card variant="elevated" padding="md" loading={loading} error={error}>
      <div
        className="sk-metriccard__top"
        style={{
          display: 'flex',
          alignItems: 'flex-start',
          justifyContent: 'space-between',
          marginBottom: 'var(--space-stack-sm)',
        }}
      >
        <span
          className="sk-metriccard__label"
          style={{
            fontSize: 'var(--text-body-sm)',
            color: 'var(--color-text-secondary)',
            fontWeight: 'var(--weight-medium)',
          }}
        >
          {label}
        </span>
        {icon && (
          <span
            className="sk-metriccard__icon"
            style={{
              color: 'var(--color-icon-default)',
              fontSize: 'var(--icon-sm)',
              flexShrink: 0,
            }}
          >
            {icon}
          </span>
        )}
      </div>
      <div
        className="sk-metriccard__value-row"
        style={{
          display: 'flex',
          alignItems: 'baseline',
          gap: 'var(--space-inline-xs)',
          marginBottom: previousValue || changePercent != null ? 'var(--space-stack-xs)' : 0,
        }}
      >
        <span
          className="sk-metriccard__value"
          style={{
            fontSize: 'var(--text-h2)',
            fontWeight: 'var(--weight-bold)',
            color: 'var(--color-text-primary)',
            lineHeight: 'var(--leading-tight)',
          }}
        >
          {value}
        </span>
        {unit && (
          <span
            className="sk-metriccard__unit"
            style={{
              fontSize: 'var(--text-body)',
              color: 'var(--color-text-secondary)',
              fontWeight: 'var(--weight-normal)',
            }}
          >
            {unit}
          </span>
        )}
      </div>
      {(previousValue != null || changePercent != null) && (
        <div
          className="sk-metriccard__comparison"
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: 'var(--space-inline-sm)',
            fontSize: 'var(--text-caption)',
            color: 'var(--color-text-secondary)',
          }}
        >
          {previousValue != null && (
            <span className="sk-metriccard__previous">
              {'vs '}{previousValue}
            </span>
          )}
          {changePercent != null && (
            <span
              className="sk-metriccard__change"
              style={{
                color: changeColor,
                fontWeight: 'var(--weight-semibold)',
              }}
            >
              {changeSign}{changePercent}%
            </span>
          )}
        </div>
      )}
    </Card>
  );
};

MetricCard.displayName = 'MetricCard';

export default MetricCard;
