import React from 'react';
import { Card } from './Card';

export interface StatCardProps {
  title: string;
  value: string | number;
  trend?: 'up' | 'down' | 'neutral';
  icon?: React.ReactNode;
  description?: string;
  loading?: boolean;
  error?: string;
}

export const StatCard: React.FC<StatCardProps> = ({
  title,
  value,
  trend,
  icon,
  description,
  loading = false,
  error,
}) => {
  const trendColor = trend === 'up'
    ? 'var(--color-success)'
    : trend === 'down'
    ? 'var(--color-danger)'
    : 'var(--color-text-secondary)';

  const trendArrow = trend === 'up' ? '\u2191' : trend === 'down' ? '\u2193' : '\u2192';

  return (
    <Card variant="default" padding="md" loading={loading} error={error}>
      <div
        className="sk-statcard__header"
        style={{
          display: 'flex',
          alignItems: 'flex-start',
          justifyContent: 'space-between',
          marginBottom: 'var(--space-stack-xs)',
        }}
      >
        <span
          className="sk-statcard__title"
          style={{
            fontSize: 'var(--text-caption)',
            color: 'var(--color-text-secondary)',
            fontWeight: 'var(--weight-medium)',
            textTransform: 'uppercase',
            letterSpacing: 'var(--tracking-wide)',
          }}
        >
          {title}
        </span>
        {icon && (
          <span
            className="sk-statcard__icon"
            style={{
              color: 'var(--color-icon-default)',
              fontSize: 'var(--icon-md)',
              flexShrink: 0,
            }}
          >
            {icon}
          </span>
        )}
      </div>
      <div
        className="sk-statcard__value-row"
        style={{
          display: 'flex',
          alignItems: 'baseline',
          gap: 'var(--space-inline-sm)',
          marginBottom: description ? 'var(--space-stack-xs)' : 0,
        }}
      >
        <span
          className="sk-statcard__value"
          style={{
            fontSize: 'var(--text-h3)',
            fontWeight: 'var(--weight-bold)',
            color: 'var(--color-text-primary)',
            lineHeight: 'var(--leading-tight)',
          }}
        >
          {value}
        </span>
        {trend && (
          <span
            className="sk-statcard__trend"
            style={{
              fontSize: 'var(--text-body-sm)',
              color: trendColor,
              fontWeight: 'var(--weight-semibold)',
              display: 'inline-flex',
              alignItems: 'center',
              gap: '2px',
            }}
          >
            {trendArrow}
          </span>
        )}
      </div>
      {description && (
        <p
          className="sk-statcard__description"
          style={{
            fontSize: 'var(--text-body-sm)',
            color: 'var(--color-text-secondary)',
            margin: 0,
            lineHeight: 'var(--leading-normal)',
          }}
        >
          {description}
        </p>
      )}
    </Card>
  );
};

StatCard.displayName = 'StatCard';

export default StatCard;
