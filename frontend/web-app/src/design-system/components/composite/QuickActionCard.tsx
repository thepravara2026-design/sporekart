import React from 'react';
import { Card } from './Card';

export interface QuickActionCardProps {
  title: string;
  description?: string;
  icon?: React.ReactNode;
  onClick?: () => void;
  loading?: boolean;
}

export const QuickActionCard: React.FC<QuickActionCardProps> = ({
  title,
  description,
  icon,
  onClick,
  loading = false,
}) => {
  return (
    <Card
      variant="default"
      padding="md"
      onClick={onClick}
      hoverable={!!onClick}
      loading={loading}
    >
      <div
        className="sk-quickactioncard__body"
        style={{
          display: 'flex',
          alignItems: 'center',
          gap: 'var(--space-inline-md)',
        }}
      >
        {icon && (
          <div
            className="sk-quickactioncard__icon"
            style={{
              width: 44,
              height: 44,
              borderRadius: 'var(--radius-md)',
              background: 'var(--color-bg-primary-weak)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              flexShrink: 0,
              color: 'var(--color-icon-primary)',
              fontSize: 'var(--icon-md)',
            }}
          >
            {icon}
          </div>
        )}
        <div
          className="sk-quickactioncard__content"
          style={{ flex: 1, minWidth: 0 }}
        >
          <h4
            className="sk-quickactioncard__title"
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
              className="sk-quickactioncard__description"
              style={{
                fontSize: 'var(--text-body-sm)',
                color: 'var(--color-text-secondary)',
                margin: 'var(--space-1) 0 0 0',
                lineHeight: 'var(--leading-normal)',
              }}
            >
              {description}
            </p>
          )}
        </div>
        {onClick && (
          <span
            className="sk-quickactioncard__arrow"
            style={{
              color: 'var(--color-text-disabled)',
              fontSize: 'var(--text-body)',
              flexShrink: 0,
            }}
          >
            {'\u2192'}
          </span>
        )}
      </div>
    </Card>
  );
};

QuickActionCard.displayName = 'QuickActionCard';

export default QuickActionCard;
