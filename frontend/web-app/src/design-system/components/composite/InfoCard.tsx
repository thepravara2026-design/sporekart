import React from 'react';
import { Card } from './Card';

export interface InfoCardProps {
  title: string;
  children?: React.ReactNode;
  icon?: React.ReactNode;
  loading?: boolean;
  error?: string;
}

export const InfoCard: React.FC<InfoCardProps> = ({
  title,
  children,
  icon,
  loading = false,
  error,
}) => {
  return (
    <Card variant="outlined" padding="md" loading={loading} error={error}>
      <div
        className="sk-infocard__header"
        style={{
          display: 'flex',
          alignItems: 'center',
          gap: 'var(--space-inline-sm)',
          marginBottom: children ? 'var(--space-stack-sm)' : 0,
        }}
      >
        {icon && (
          <span
            className="sk-infocard__icon"
            style={{
              color: 'var(--color-icon-primary)',
              fontSize: 'var(--icon-md)',
              flexShrink: 0,
            }}
          >
            {icon}
          </span>
        )}
        <h3
          className="sk-infocard__title"
          style={{
            fontSize: 'var(--text-body)',
            fontWeight: 'var(--weight-semibold)',
            color: 'var(--color-text-primary)',
            margin: 0,
          }}
        >
          {title}
        </h3>
      </div>
      {children && (
        <div
          className="sk-infocard__body"
          style={{
            fontSize: 'var(--text-body-sm)',
            color: 'var(--color-text-secondary)',
            lineHeight: 'var(--leading-relaxed)',
          }}
        >
          {children}
        </div>
      )}
    </Card>
  );
};

InfoCard.displayName = 'InfoCard';

export default InfoCard;
