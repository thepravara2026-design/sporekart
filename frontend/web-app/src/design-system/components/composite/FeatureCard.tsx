import React from 'react';
import { Card } from './Card';

export interface FeatureCardProps {
  title: string;
  description: string;
  icon?: React.ReactNode;
  image?: string;
  loading?: boolean;
}

export const FeatureCard: React.FC<FeatureCardProps> = ({
  title,
  description,
  icon,
  image,
  loading = false,
}) => {
  return (
    <Card variant="ghost" padding="md" loading={loading}>
      {image && (
        <div
          className="sk-featurecard__image-wrapper"
          style={{
            marginBottom: 'var(--space-stack-sm)',
            borderRadius: 'var(--radius-image)',
            overflow: 'hidden',
          }}
        >
          <img
            src={image}
            alt={title}
            className="sk-featurecard__image"
            style={{
              width: '100%',
              height: 'auto',
              display: 'block',
            }}
          />
        </div>
      )}
      <div
        className="sk-featurecard__body"
        style={{
          display: 'flex',
          flexDirection: 'column',
          gap: 'var(--space-stack-xs)',
        }}
      >
        {icon && (
          <span
            className="sk-featurecard__icon"
            style={{
              color: 'var(--color-icon-primary)',
              fontSize: 'var(--icon-xl)',
              marginBottom: 'var(--space-stack-xs)',
            }}
          >
            {icon}
          </span>
        )}
        <h3
          className="sk-featurecard__title"
          style={{
            fontSize: 'var(--text-h5)',
            fontWeight: 'var(--weight-semibold)',
            color: 'var(--color-text-primary)',
            margin: 0,
          }}
        >
          {title}
        </h3>
        <p
          className="sk-featurecard__description"
          style={{
            fontSize: 'var(--text-body-sm)',
            color: 'var(--color-text-secondary)',
            lineHeight: 'var(--leading-relaxed)',
            margin: 0,
          }}
        >
          {description}
        </p>
      </div>
    </Card>
  );
};

FeatureCard.displayName = 'FeatureCard';

export default FeatureCard;
