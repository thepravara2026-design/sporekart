import { memo } from 'react';
import { Card } from '../../../../design-system/components/composite/Card';
import { Icon } from '../../../../design-system/icons/Icon';

export interface StatWidgetProps {
  label: string;
  value: number;
  trend?: 'up' | 'down' | 'neutral';
  icon: string;
}

export const StatWidget = memo(function StatWidget({
  label,
  value,
  trend,
  icon,
}: StatWidgetProps) {
  const trendColor = trend === 'up'
    ? 'var(--color-success)'
    : trend === 'down'
      ? 'var(--color-danger)'
      : 'var(--color-text-secondary)';

  const trendArrow = trend === 'up' ? '\u2191' : trend === 'down' ? '\u2193' : '\u2192';

  const formattedValue = value >= 1000
    ? `${(value / 1000).toFixed(1)}k`
    : String(value);

  return (
    <Card variant="default" padding="md" as="div">
      <div style={{
        display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between',
        marginBottom: 'var(--space-stack-xs)',
      }}>
        <span style={{
          fontSize: 'var(--text-caption)',
          color: 'var(--color-text-secondary)',
          fontWeight: 'var(--weight-medium)',
          textTransform: 'uppercase',
          letterSpacing: 'var(--tracking-wide)',
        }}>
          {label}
        </span>
        <span style={{
          color: 'var(--color-icon-default)',
          fontSize: 'var(--icon-md)',
          flexShrink: 0,
          opacity: 0.6,
        }}>
          <Icon name={icon} size={20} color="currentColor" />
        </span>
      </div>
      <div style={{
        display: 'flex', alignItems: 'baseline', gap: 'var(--space-inline-sm)',
      }}>
        <span style={{
          fontSize: 'var(--text-h3)',
          fontWeight: 'var(--weight-bold)',
          color: 'var(--color-text-primary)',
          lineHeight: 'var(--leading-tight)',
        }}>
          {formattedValue}
        </span>
        {trend && (
          <span style={{
            fontSize: 'var(--text-body-sm)',
            color: trendColor,
            fontWeight: 'var(--weight-semibold)',
            display: 'inline-flex', alignItems: 'center', gap: '2px',
          }} aria-label={`${trend === 'up' ? 'Increasing' : trend === 'down' ? 'Decreasing' : 'Stable'}`}>
            {trendArrow}
          </span>
        )}
      </div>
    </Card>
  );
});
