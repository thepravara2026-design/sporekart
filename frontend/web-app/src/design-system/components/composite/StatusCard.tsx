import React from 'react';
import { Card } from './Card';

export interface StatusCardProps {
  title: string;
  status: 'success' | 'warning' | 'error' | 'info' | 'neutral';
  description?: string;
  icon?: React.ReactNode;
  loading?: boolean;
}

const statusConfig: Record<string, { bg: string; color: string }> = {
  success: { bg: 'var(--color-success-50)', color: 'var(--color-success)' },
  warning: { bg: 'var(--color-warning-50)', color: 'var(--color-warning)' },
  error: { bg: 'var(--color-danger-50)', color: 'var(--color-danger)' },
  info: { bg: 'var(--color-info-50)', color: 'var(--color-info)' },
  neutral: { bg: 'var(--color-bg-surface-default)', color: 'var(--color-text-secondary)' },
};

export const StatusCard: React.FC<StatusCardProps> = ({
  title,
  status,
  description,
  icon,
  loading = false,
}) => {
  const config = statusConfig[status];

  return (
    <Card variant="outlined" padding="md" loading={loading}>
      <div
        className="sk-statuscard__body"
        style={{
          display: 'flex',
          alignItems: 'flex-start',
          gap: 'var(--space-inline-md)',
        }}
      >
        <div
          className="sk-statuscard__icon-wrapper"
          style={{
            width: 40,
            height: 40,
            borderRadius: 'var(--radius-full)',
            background: config.bg,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            flexShrink: 0,
            color: config.color,
            fontSize: 'var(--icon-md)',
          }}
        >
          {icon || (
            <span style={{ fontWeight: 'var(--weight-bold)' }}>
              {status === 'success' ? '\u2713' : status === 'error' ? '\u2717' : '\u2139'}
            </span>
          )}
        </div>
        <div
          className="sk-statuscard__content"
          style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-stack-xs)' }}
        >
          <h4
            className="sk-statuscard__title"
            style={{
              fontSize: 'var(--text-body)',
              fontWeight: 'var(--weight-semibold)',
              color: 'var(--color-text-primary)',
              margin: 0,
            }}
          >
            {title}
          </h4>
          {description && (
            <p
              className="sk-statuscard__description"
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
        </div>
      </div>
    </Card>
  );
};

StatusCard.displayName = 'StatusCard';

export default StatusCard;
